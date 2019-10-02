package domain.repository.event

import domain.dto.program.Program
import org.w3c.dom.events.Event
import java.util.*

interface ProgramRepository {
    fun findAll(): List<Program>
    fun findById(id: UUID): Program?
}