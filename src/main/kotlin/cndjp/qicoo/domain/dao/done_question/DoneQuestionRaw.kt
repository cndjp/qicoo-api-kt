package domain.dao.done_question

data class DoneQuestionRow(
    val question_id: Int,
    val program_id: Int,
    val display_name: String,
    val comment: String
)

fun NewDoneQuestion.toRaw(): DoneQuestionRow =
    DoneQuestionRow(
        this.question_id.value,
        this.program_id.value,
        this.display_name,
        this.comment
    )
