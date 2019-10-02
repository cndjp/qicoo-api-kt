package domain.dto.question

import domain.dao.question_context.QuestionContext
import domain.dto.done_question.DoneQuestionDTO
import domain.dto.todo_question.TodoQuestionDTO

class QuestionDTO(
    val questionContext: List<QuestionContext>
) {
    companion object
}