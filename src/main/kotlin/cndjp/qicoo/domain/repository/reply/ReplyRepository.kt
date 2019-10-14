package cndjp.qicoo.domain.repository.reply

import cndjp.qicoo.api.QicooError
import com.github.michaelbull.result.Result

interface ReplyRepository {
    fun add(id: Int, comment: String): Result<Unit, QicooError>
}