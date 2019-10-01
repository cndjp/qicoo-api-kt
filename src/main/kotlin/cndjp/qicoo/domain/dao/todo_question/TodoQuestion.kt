package domain.dao.todo_question


import domain.dao.program.program
import domain.dao.question.question
import org.jetbrains.exposed.sql.Table

object todo_question: Table() {
    val question_id = (entityId("question_id",
        question
    ) references question.id).primaryKey()
    val program_id = (entityId("program_id",
        program
    ) references program.id)
    val display_name = varchar("display_name", 255).default("anonymous")
    val like_count = integer("like_count").default(0)
    val comment = varchar("comment", 255).default("")
}