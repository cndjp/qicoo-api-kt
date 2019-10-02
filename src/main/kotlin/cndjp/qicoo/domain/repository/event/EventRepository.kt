package domain.repository.event

import domain.dao.event.Event
import java.util.*

interface EventRepository {
    fun findAll(): List<Event>
    fun findById(id: UUID): Event?
}