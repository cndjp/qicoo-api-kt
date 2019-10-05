package domain.model.reply

import domain.model.question.question
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.UUIDTable

object reply : IntIdTable() {
    val question_id = (entityId("question_id",
        question
    ) references question.id)
    val comment = varchar("comment", 255).default("")
    val created = datetime("created")
    val updated = datetime("updated")
}
