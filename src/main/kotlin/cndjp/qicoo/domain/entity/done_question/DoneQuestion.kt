package main.kotlin.cndjp.qicoo.domain.entity.done_question


class DoneQuestion(question_id: ByteArray, program_id: Long, display_name: String, like_count: Long, comment: String) {
    companion object {
        fun new(question_id: ByteArray, program_id: Long, display_name: String, like_count: Long, comment: String): DoneQuestion {
            return DoneQuestion(
                question_id,
                program_id,
                display_name,
                like_count,
                comment
            )
        }
    }
}