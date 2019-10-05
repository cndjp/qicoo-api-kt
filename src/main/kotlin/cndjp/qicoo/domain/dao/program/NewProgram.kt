package domain.dao.program

import domain.model.program.program
import java.util.UUID
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass

class NewProgram(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewProgram>(program)

    var name by program.name
    var event_id by program.event_id
    var start_at by program.start_at
    var end_at by program.end_at
    var created by program.created
    var updated by program.updated
}
