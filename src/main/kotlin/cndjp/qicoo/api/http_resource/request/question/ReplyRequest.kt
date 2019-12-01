package cndjp.qicoo.api.http_resource.request.question

data class ReplyRequest(
    val question_id: Int,
    val comment: String
)
