package api.response.quesion

data class QuestionListResponse(
    val value: List<QuestionResponse>,
    val total: Int
)
