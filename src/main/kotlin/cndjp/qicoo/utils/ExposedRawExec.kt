package cndjp.qicoo.utils

import java.sql.ResultSet
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun <T : Any> String.execAndMap(transform: (ResultSet) -> T): List<T> {
    val result = arrayListOf<T>()
    TransactionManager.current().exec(this) { rs ->
        while (rs.next()) {
            result += transform(rs)
        }
    }
    return result
}
