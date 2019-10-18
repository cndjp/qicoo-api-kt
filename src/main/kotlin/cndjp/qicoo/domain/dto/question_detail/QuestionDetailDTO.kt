package cndjp.qicoo.domain.dto.question_detail

import cndjp.qicoo.domain.dto.question.QuestionDTO
import cndjp.qicoo.domain.dto.reply.ReplyListDTO

data class QuestionDetailDTO(
    val questionDTO: QuestionDTO,
    val replyListDTO: ReplyListDTO
)