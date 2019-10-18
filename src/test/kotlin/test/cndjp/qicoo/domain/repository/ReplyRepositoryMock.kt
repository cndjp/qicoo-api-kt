package test.cndjp.qicoo.domain.repository

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.reply.ReplyList
import cndjp.qicoo.domain.repository.reply.ReplyRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

class ReplyRepositoryMock: ReplyRepository {
    override fun add(id: Int, comment: String): Result<Unit, QicooError> = Ok(Unit)
    override fun findById(id: Int): ReplyList {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}