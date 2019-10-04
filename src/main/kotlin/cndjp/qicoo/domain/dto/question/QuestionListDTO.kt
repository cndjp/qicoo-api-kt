package domain.dto.question

data class QuestionListDTO(
    val list: List<QuestionDTO>,
    val count: Int
)