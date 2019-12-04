package cndjp.qicoo.domain.repository.user

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.linked_user.LinkedUser
import cndjp.qicoo.domain.dao.user.NewUser
import com.github.michaelbull.result.Result

interface UserRepository {
    fun findLinedUserById(id: Int): Result<LinkedUser, QicooError>
    fun findLinedUserByGitHubAccountId(gitHubAccountId: Int): Result<LinkedUser, QicooError>
    fun insert(newUser: NewUser): Result<Unit, QicooError>
}