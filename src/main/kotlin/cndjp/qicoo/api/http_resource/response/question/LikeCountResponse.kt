package cndjp.qicoo.api.http_resource.response.question

import cndjp.qicoo.domain.dao.like_count.LikeCountValue

data class LikeCountResponse (
    val like_count: LikeCountValue
)