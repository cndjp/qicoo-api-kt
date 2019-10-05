package domain.model.event

import org.jetbrains.exposed.dao.IntIdTable

object event : IntIdTable() {
    val name = varchar("name", 255)
    val start_at = datetime("start_at")
    val end_at = datetime("end_at")
    val created = datetime("created")
    val updated = datetime("updated")
}
