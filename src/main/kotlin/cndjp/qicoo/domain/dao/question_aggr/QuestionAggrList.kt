package cndjp.qicoo.domain.dao.question_aggr

import cndjp.qicoo.domain.dao.question_aggr.QuestionAggr

data class QuestionAggrList(
    val list: List<QuestionAggr>,
    val total: Int
)
