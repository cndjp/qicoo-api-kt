package domain.dto.todo_question

import domain.dao.todo_question.TodoQuestion
import org.joda.time.DateTime

data class TodoQuestionDTOUnit(
    val dao: TodoQuestion,
    val created: DateTime?,
    val updated: DateTime?
)

class TodoQuestionDTO(
    val value: List<TodoQuestionDTOUnit>
) {
    companion object
}