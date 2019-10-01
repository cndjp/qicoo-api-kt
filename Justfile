NAME := "qicoo-api-kt"

DOTENV_EXISTS := `if [ -f ./.env ]; then echo 0; else echo 1; fi`

MYSQL_DOCKER := "qicoo-test-mysql"
MYSQL_DOCKER_EXISTS_FLAG := `if [ ! -z ${CIRCLECI:-} ]; then echo 1; exit 0; fi; docker ps --format "{{ .Names }}" --filter "name=qicoo-test-mysql" | wc -l`
MYSQL_VERSION := "8.0.11"

version:
    #!/bin/bash
    ./gradlew --version

test: load_dotenv
    #!/bin/bash
    ./gradlew test

build: load_dotenv
    #!/bin/bash
    ./gradlew build

create_dotenv:
    #!/bin/bash
    if [ ! {{ DOTENV_EXISTS }} = 0 ]; then
        echo 'MYSQL_USER="root"' >> ./.env
        echo 'MYSQL_PASSWORD=""' >> ./.env
        echo 'MYSQL_PORT="3306"' >> ./.env
        echo 'MYSQL_DB="qicoo2db"' >> ./.env
        echo 'MYSQL_OPTS="prefer_socket=false&timeout=30s&parseTime=true&loc=Asia%2FTokyo"' >> ./.env
    fi

load_dotenv: create_dotenv
    #!/bin/bash
    if [ ! -z ${TRAVIS:-} ]; then
        echo 'Do not load dotenv'
        exit 0
    fi

    if [ {{ DOTENV_EXISTS }} = 0 ]; then
        export $(sed 's/=.*//' ./.env)
    else
        echo '.env is not found'
    fi

mysql-db-is-exist:
    #!/bin/bash
    echo MYSQL_DOCKER_EXISTS_FLAG is {{ MYSQL_DOCKER_EXISTS_FLAG }}
    if [ ! {{ MYSQL_DOCKER_EXISTS_FLAG }} = 0 ]; then
        echo "{{ MYSQL_DOCKER }} is exists"
    else
        echo "{{ MYSQL_DOCKER }} is not exists"
    fi

check-mysql-db: load_dotenv
    #!/bin/bash
    STATUS=1
    COUNT=0

    while [ ${STATUS} = 1 ]
    do
        mysqladmin ping -u root -h ${MYSQL_HOST} -p${MYSQL_PORT} > /dev/null 2>&1 && STATUS=0
        if [ ${STATUS} = 1 ]; then
            echo -n '.'
        fi
        sleep 1
        (( COUNT++ ))
        if [ ${COUNT} = 50 ]; then
            echo "Sorry, Cannot connect the mysql docker."
            exit 1
        fi
    done

    echo '!'
    echo {{ MYSQL_DOCKER }} is alive

lunch-mysql-db: load_dotenv
    #!/bin/bash
    if [ ! {{ MYSQL_DOCKER_EXISTS_FLAG }} = 0 ]; then
        echo "{{ MYSQL_DOCKER }} is exists. Not work."
    else
        echo 'set up the mysql docker'
        docker run --name {{ MYSQL_DOCKER }} \
            --rm \
            -d \
            -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
            -p ${MYSQL_PORT}:${MYSQL_PORT} mysql:{{ MYSQL_VERSION }} \
            --character-set-server=utf8mb4 \
            --collation-server=utf8mb4_unicode_ci
    fi

setup-mysql-db: load_dotenv
    echo "CREATE DATABASE IF NOT EXISTS ${MYSQL_DB} DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci" | mysql -h${MYSQL_HOST} -u${MYSQL_USER} 2>/dev/null
    echo 'set global time_zone = "Asia/Tokyo"' | mysql -h${MYSQL_HOST} -u${MYSQL_USER} 2>/dev/null

run-mysql-db: lunch-mysql-db check-mysql-db setup-mysql-db

clean-mysql-db:
    #!/bin/bash
    if [ {{ MYSQL_DOCKER_EXISTS_FLAG }} = 0 ]; then
        echo "{{ MYSQL_DOCKER }} is not exists. Not work."
    else
        docker kill {{ MYSQL_DOCKER }} > /dev/null && echo 'clean up the mysql docker'
    fi