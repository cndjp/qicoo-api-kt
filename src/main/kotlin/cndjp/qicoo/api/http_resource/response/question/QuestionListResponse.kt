package cndjp.qicoo.api.http_resource.response.question

data class QuestionListResponse(
    val value: List<QuestionResponse>,
    val total: Int
)
