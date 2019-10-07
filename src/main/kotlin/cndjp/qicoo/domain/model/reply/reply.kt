package cndjp.qicoo.domain.model.reply

import cndjp.qicoo.domain.model.question.question
import org.jetbrains.exposed.dao.IntIdTable

object reply : IntIdTable() {
    val question_id = (entityId("question_id",
        question
    ) references question.id)
    val comment = varchar("comment", 255).default("")
    val created = datetime("created")
    val updated = datetime("updated")
}
