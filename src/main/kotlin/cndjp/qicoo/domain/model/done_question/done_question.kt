package cndjp.qicoo.domain.model.done_question

import cndjp.qicoo.domain.model.program.program
import cndjp.qicoo.domain.model.question.question
import org.jetbrains.exposed.sql.Table

object done_question : Table() {
    val question_id = (entityId("question_id",
        question
    ) references question.id).primaryKey()
    val program_id = (entityId("program_id",
        program
    ) references program.id)
    val display_name = varchar("display_name", 255).default("anonymous")
    val comment = varchar("comment", 255).default("")
}
