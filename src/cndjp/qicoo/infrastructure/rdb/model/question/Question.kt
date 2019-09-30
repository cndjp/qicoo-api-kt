package cndjp.qicoo.infrastructure.rdb.model.question

import org.jetbrains.exposed.dao.UUIDTable

object question: UUIDTable() {
    val created = datetime("created")
    val updated = datetime("updated")
}