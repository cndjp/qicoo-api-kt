package api.http_resource.paramater.question

data class QuestionGetParameter(
    val per: Int,
    val page: Int,
    val sort: QuestionGetSortParameter,
    val order: QuestionGetOrderParameter
)

enum class QuestionGetOrderParameter {
    desc, asc,
}

enum class QuestionGetSortParameter {
    like, updated,
}
