package main.kotlin.cndjp.qicoo.domain.dao.program

import main.kotlin.cndjp.qicoo.domain.dao.event.event
import org.jetbrains.exposed.dao.UUIDTable

object program: UUIDTable() {
    val event_id = (entityId("event_id",
        event
    ) references event.id)
    val start_at = datetime("start_at")
    val end_at = datetime("end_at")
    val created = datetime("created")
    val updated = datetime("updated")
}