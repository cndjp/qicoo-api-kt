package domain.repository.event

import domain.dao.event.Event
import domain.dao.event.toEvent
import domain.dto.program.toProgram
import domain.model.event.event
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class EventRepositoryImpl: EventRepository {
    override fun findAll(): List<Event> = transaction { event.selectAll().map{it.toEvent()} }
    override fun findById(id: UUID): Event? = transaction { event.select{event.id eq id }.map{it.toEvent()}.firstOrNull() }
}