package test.cndjp.qicoo.domain.repository

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.repository.reply.ReplyRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

class ReplyRepositoryMock: ReplyRepository {
    override fun add(id: Int, comment: String): Result<Unit, QicooError> = Ok(Unit)
}