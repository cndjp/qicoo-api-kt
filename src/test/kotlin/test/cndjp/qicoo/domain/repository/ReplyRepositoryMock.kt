package test.cndjp.qicoo.domain.repository

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.reply.Reply
import cndjp.qicoo.domain.dao.reply.ReplyList
import cndjp.qicoo.domain.model.reply.ReplyRow
import cndjp.qicoo.domain.repository.reply.ReplyRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import test.cndjp.qicoo.infrastructure.support.migration_run.RepositorySpecSupport

class ReplyRepositoryMock: ReplyRepository {
    private val ss = RepositorySpecSupport

    override fun add(id: Int, comment: String): Result<Unit, QicooError> = Ok(Unit)
    override fun findById(id: Int): ReplyList =
        when (id) {
            1 -> ReplyList(listOf(), 0)
            2 -> ReplyList(listOf(), 0)
            3 -> ReplyList(listOf(
                Reply(
                    ReplyRow(
                        ss.q3reply1date,
                        ss.q3reply1
                    )
                ),
                Reply(
                    ReplyRow(
                        ss.q3reply2date,
                        ss.q3reply2
                    )
                ),
                Reply(
                    ReplyRow(
                        ss.q3reply3date,
                        ss.q3reply3
                    )
                )
            ), 3)
            4 -> ReplyList(listOf(), 0)
            5 -> ReplyList(listOf(), 0)
            6 -> ReplyList(
                listOf(
                    Reply(
                        ReplyRow(
                            ss.q6reply1date,
                            ss.q6reply1
                        )
                    )
                ),
                1
            )
            else -> TODO()
        }

}