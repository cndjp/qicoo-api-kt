package api.response.quesion

import domain.dto.question_aggr.QuestionListDTO
import utils.toFomatString

data class QuestionListResponse (
    val value: List<QuestionResponse>
    )
