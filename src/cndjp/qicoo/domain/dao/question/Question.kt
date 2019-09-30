package cndjp.qicoo.domain.dao.question

import org.jetbrains.exposed.dao.UUIDTable

object question: UUIDTable() {
    val created = datetime("created")
    val updated = datetime("updated")
}