package domain.model.program

import domain.model.event.event
import org.jetbrains.exposed.dao.UUIDTable

object program : UUIDTable() {
    val name = varchar("name", 255)
    val event_id = (entityId("event_id",
        event
    ) references event.id)
    val start_at = datetime("start_at")
    val end_at = datetime("end_at")
    val created = datetime("created")
    val updated = datetime("updated")
}
