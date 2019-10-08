# qicoo-api-kt
[![Travis CI](https://travis-ci.org/cndjp/qicoo-api-kt.svg?branch=master)](https://travis-ci.org/cndjp/qicoo-api-kt)  
qicoo-apiのkotlinバージョン

# 設計方針
- NoTry, NoNull(=OrNull)  
    DBのカラムは全部NOT NULL、コードに例外、?じゃないnullableはいらないです
- No DeleteFlag  
    脳死なdelete_flg bool はいらないです
- Cake Pattern  
    どのレイヤーも単体で切り離して、例えばモック、例えば違う実装で全体のコードが走ります
- Repository Pattern  
    ドメイン層のやりとりで、インフラ層の何を使っているか隠蔽し、柔軟性を保たせます

# テスト環境

## デプロイ

```$xslt
$ just run-db
```

## クリーン

```$xslt
$ just clean-db
```

# ビルド

## コード

```$xslt
$ just build
```

## docker

```$xslt
$ just docker-build
```

# テスト

```$xslt
$ just test
```