package domain.dao.event

import domain.model.event.event
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class NewEvent(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewEvent>(event)

    var name by event.name
    var start_at by event.start_at
    var end_at by event.end_at
    var created by event.created
    var updated by event.updated
}
