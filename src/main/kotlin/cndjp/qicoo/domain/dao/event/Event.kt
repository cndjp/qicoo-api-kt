package domain.dao.event

import org.jetbrains.exposed.dao.UUIDTable

object event: UUIDTable() {
    val start_at = datetime("start_at")
    val end_at = datetime("end_at")
    val created = datetime("created")
    val updated = datetime("updated")
}