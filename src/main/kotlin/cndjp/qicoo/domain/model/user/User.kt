package domain.model.user

import org.jetbrains.exposed.dao.UUIDTable

object user: UUIDTable() {
    val created = datetime("created")
}