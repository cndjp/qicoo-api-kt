package domain.repository.event

import domain.dto.program.Program
import domain.dto.program.toProgram
import domain.model.program.program
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.w3c.dom.events.Event
import java.util.*


class ProgramRepositoryImpl: ProgramRepository {
    override fun findAll(): List<Program> = transaction { program.selectAll().map { it.toProgram() } }
    override fun findById(id: UUID): Program? = transaction { program.select{program.id eq id }.map{it.toProgram()}.firstOrNull() }
}