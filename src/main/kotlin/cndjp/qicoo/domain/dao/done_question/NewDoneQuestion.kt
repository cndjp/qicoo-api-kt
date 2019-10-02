package domain.dao.done_question

import domain.model.done_question.done_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert
import java.util.*

class NewDoneQuestion(
    val question_id: EntityID<UUID>,
    val program_id: EntityID<UUID>,
    val display_name: String,
    val like_count: Int,
    val comment: String
) {
    private val f1 = question_id
    private val f2 = program_id
    private val f3 = display_name
    private val f4 = like_count
    private val f5 = comment
    init {
        done_question.insert {
            it[question_id] = f1
            it[program_id] = f2
            it[display_name] = f3
            it[like_count] = f4
            it[comment] = f5
        }
    }
    companion object
}