package cndjp.qicoo.domain.model.user

import org.jetbrains.exposed.dao.IntIdTable

object user : IntIdTable() {
    val created = datetime("created")
}
