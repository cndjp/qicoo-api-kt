package domain.dao.user

import domain.model.user.user
import java.util.UUID
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass

class NewUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewUser>(user)

    var created by user.created
}
