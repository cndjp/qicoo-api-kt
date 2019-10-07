package domain.dao.todo_question

data class TodoQuestionRow(
    val question_id: Int,
    val program_id: Int,
    val display_name: String,
    val comment: String
)

fun NewTodoQuestion.toRaw(): TodoQuestionRow =
    TodoQuestionRow(
        this.question_id.value,
        this.program_id.value,
        this.display_name,
        this.comment
    )
