package test.cndjp.qicoo.domain

import cndjp.qicoo.domain.dao.like_count.LikeCount
import cndjp.qicoo.domain.model.like_count.LikeCountRow
import kotlin.test.assertNotNull
import org.spekframework.spek2.Spek

object RedisDomainSpec : Spek({
    group("factoryのテスト") {
        test("LikeCountRowに正常な値が入ってたらnullじゃない") {
            val r = LikeCount(LikeCountRow("1", 1000.0))
            assertNotNull(r.question_id)
            assertNotNull(r.count)
        }
    }
})