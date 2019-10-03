package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.dao.question_context.QuestionContext
import java.util.*

interface LikeCountRepository {
    fun findAll(): List<LikeCount>
    fun findById(question_id: UUID): LikeCount?
}