package cndjp.qicoo.domain.dao.user

import cndjp.qicoo.domain.model.user.user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class NewUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewUser>(user)

    var created by user.created
}
