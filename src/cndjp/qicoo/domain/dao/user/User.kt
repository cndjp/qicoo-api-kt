package cndjp.qicoo.domain.dao.user

import org.jetbrains.exposed.dao.UUIDTable

object user: UUIDTable() {
    val created = datetime("created")
}