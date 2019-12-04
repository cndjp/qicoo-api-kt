package cndjp.qicoo.infrastructure.repository

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.linked_user.LinkedUser
import cndjp.qicoo.domain.dao.user.NewUser
import cndjp.qicoo.domain.repository.user.UserRepository
import com.github.michaelbull.result.Result

class UserRepositoryImpl: UserRepository {
    override fun findLinedUserByGitHubAccountId(gitHubAccountId: Int): Result<LinkedUser, QicooError> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findLinedUserById(id: Int): Result<LinkedUser, QicooError> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(newUser: NewUser): Result<Unit, QicooError> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}