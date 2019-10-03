package domain

import domain.dao.like_count.factory
import domain.model.like_count.LikeCountRow
import org.spekframework.spek2.Spek
import java.util.*
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object RedisDomainSpec: Spek({
    group("factoryのテスト"){
        test("LikeCountRowに不正な値が入ってたらnullを入れて例外にしない") {
            val r = LikeCountRow("???", "???").factory()
            assertNull(r.question_id)
            assertNull(r.count)
        }
        test("LikeCountRowに正常な値が入ってたらnullじゃない") {
            val r = LikeCountRow(UUID.randomUUID().toString(), "1000").factory()
            assertNotNull(r.question_id)
            assertNotNull(r.count)
        }
    }
})