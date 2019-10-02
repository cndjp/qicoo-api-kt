package domain.dao.user

import domain.model.user.user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import java.util.*

class NewUser(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<NewUser>(user)

    var created by user.created
}