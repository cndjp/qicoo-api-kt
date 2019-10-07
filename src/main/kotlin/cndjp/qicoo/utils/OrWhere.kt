package cndjp.qicoo.utils

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.or

fun Query.orWhere(andPart: SqlExpressionBuilder.() -> Op<Boolean>) = adjustWhere {
    val expr = Op.build { andPart() }
    if (this == null) expr
    else this or expr
}
