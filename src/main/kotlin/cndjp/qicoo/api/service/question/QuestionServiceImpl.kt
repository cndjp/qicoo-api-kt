package api.service.question

import domain.dto.question.QuestionDTO
import domain.repository.like_count.LikeCountRepository
import domain.repository.question_aggr.QuestionAggrRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import utils.NotfoundEntityException
import utils.RetResult

class QuestionServiceImpl(override val kodein: Kodein): QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()
    override fun getAll(per: Int, page: Int): Pair<List<QuestionDTO>, Int> {
        val result = questionAggrRepository.findAll(per, page)
        return Pair(
            result.first.map {
                val likeCount = likeCountRepository.findById(it.question_id)?.count ?: 0
                QuestionDTO(
                    it.program_name,
                    it.event_name,
                    it.display_name,
                    likeCount,
                    it.comment,
                    it.created,
                    it.updated
                )
            }, result.second
        )
    }

    override fun create(comment: String): Unit =
        questionAggrRepository.insert(comment)
}