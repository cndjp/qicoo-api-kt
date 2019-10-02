package domain.dto.question

import domain.dao.question_context.QuestionContext

class QuestionDTO(
    val questionContext: List<QuestionContext>
) {
    companion object
}