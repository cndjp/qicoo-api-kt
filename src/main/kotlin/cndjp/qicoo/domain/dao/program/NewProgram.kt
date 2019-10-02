package domain.dao.program

import domain.model.program.program
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.*

class NewProgram(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<NewProgram>(program)

    var event_id by program.event_id
    var start_at by program.start_at
    var end_at by program.end_at
    var created by program.created
    var updated by program.updated
}