package cndjp.qicoo.api.infrastructure.rdb.context

import org.jetbrains.exposed.sql.Database

fun mysqlContext() {
    Database.connect("jdbc:mysql://localhost/qicoo2db", "com.mysql.jdbc.Driver", "root", "password")
}