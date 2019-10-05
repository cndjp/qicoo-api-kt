package domain

import domain.dao.like_count.LikeCount
import domain.model.like_count.LikeCountRow
import java.util.*
import kotlin.test.assertNotNull
import org.spekframework.spek2.Spek

object RedisDomainSpec : Spek({
    group("factoryのテスト") {
        test("LikeCountRowに正常な値が入ってたらnullじゃない") {
            val r = LikeCount(LikeCountRow(UUID.randomUUID().toString(), 1000.0))
            assertNotNull(r.question_id)
            assertNotNull(r.count)
        }
    }
})
