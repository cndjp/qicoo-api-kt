package domain

import domain.dao.like_count.LikeCount
import domain.model.like_count.LikeCountRow
import domain.model.like_count.LikeCountRowKey
import java.util.*
import kotlin.test.assertNotNull
import org.spekframework.spek2.Spek

object RedisDomainSpec : Spek({
    group("factoryのテスト") {
        test("LikeCountRowに正常な値が入ってたらnullじゃない") {
            val r = LikeCount(LikeCountRow(LikeCountRowKey(UUID.randomUUID()), "1000"))
            assertNotNull(r.question_id)
            assertNotNull(r.count)
        }
    }
})
