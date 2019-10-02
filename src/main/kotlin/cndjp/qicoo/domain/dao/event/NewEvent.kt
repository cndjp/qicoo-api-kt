package domain.dao.event

import domain.model.event.event
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.*

class NewEvent(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<NewEvent>(event)

    var start_at by event.start_at
    var end_at by event.end_at
    var created by event.created
    var updated by event.updated
}