package cndjp.qicoo.domain.model.question

import cndjp.qicoo.domain.model.program.program
import org.jetbrains.exposed.dao.IntIdTable

object question : IntIdTable() {
    val program_id = (entityId(
        "program_id",
        program
    ) references program.id)
    val done_flag = bool("done_flag") .default(false)
    val display_name = varchar("display_name", 255).default("anonymous")
    val comment = varchar("comment", 255).default("")
    val created = datetime("created")
    val updated = datetime("updated")
}
