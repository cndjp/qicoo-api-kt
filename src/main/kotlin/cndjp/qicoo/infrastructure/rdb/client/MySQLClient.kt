package main.kotlin.cndjp.qicoo.infrastructure.rdb.client

import org.jetbrains.exposed.sql.Database

fun initMysqlClient() {
    val mysqlUser = System.getenv("MYSQL_USER") ?: "root"
    val mysqlPassword = System.getenv("MYSQL_PASSWORD") ?: "my-pw"
    val mysqlHost = System.getenv("MYSQL_HOST") ?: "127.0.0.1"
    val mysqlDB = System.getenv("MYSQL_DB") ?: "qicoo2db"
    val mysqlPort = System.getenv("MYSQL_PORT") ?: "3306"
    Database.connect("jdbc:mysql://$mysqlHost:$mysqlPort/$mysqlDB?useSSL=false", "com.mysql.cj.jdbc.Driver", mysqlUser, mysqlPassword)
}