package domain

import domain.dao.like_count.LikeCount
import domain.model.like_count.LikeCountKey
import domain.model.like_count.LikeCountRow
import org.spekframework.spek2.Spek
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object RedisDomainSpec: Spek({
    group("factoryのテスト"){
        test("LikeCountRowに正常な値が入ってたらnullじゃない") {
            val r = LikeCount(LikeCountRow(LikeCountKey(UUID.randomUUID()), "1000"))
            assertNotNull(r.question_id)
            assertNotNull(r.count)
        }
    }
})