package domain.model.question

import org.jetbrains.exposed.dao.IntIdTable

object question : IntIdTable() {
    val created = datetime("created")
    val updated = datetime("updated")
}
