package test.cndjp.qicoo.api.service

import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetOrderParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.api.service.question.QuestionServiceImpl
import cndjp.qicoo.domain.dao.question.toQuestion
import cndjp.qicoo.domain.model.question.question
import test.cndjp.qicoo.domain.repository.LikeCountRepositoryMock
import test.cndjp.qicoo.domain.repository.QuestionAggrRepositoryMock
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.infrastructure.repository.LikeCountRepositoryImpl
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.infrastructure.repository.QuestionAggrRepositoryImpl
import cndjp.qicoo.domain.repository.reply.ReplyRepository
import cndjp.qicoo.infrastructure.repository.ReplyRepositoryImpl
import test.cndjp.qicoo.infrastructure.support.migration_run.RepositorySpecSupport
import test.cndjp.qicoo.infrastructure.support.migration_run.dropDummyData
import test.cndjp.qicoo.infrastructure.support.migration_run.insertDummyData
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.assertEquals
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.spekframework.spek2.Spek
import cndjp.qicoo.utils.toJST
import com.github.michaelbull.result.get
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import test.cndjp.qicoo.domain.repository.ReplyRepositoryMock
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object QuestionServiceMockSpec : Spek({
    val ss = RepositorySpecSupport

    group("QuestionServiceのmockテスト") {
        val kodein1 = Kodein {
            bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryMock() }
            bind<LikeCountRepository>() with singleton { LikeCountRepositoryMock() }
            bind<ReplyRepository>() with singleton { ReplyRepositoryMock() }
        }
        val questionServiceImpl1 = QuestionServiceImpl(kodein1)

        test("getAll()でのmockテスト") {
            val param1 = QuestionGetParameter(
                per = 2,
                page = 1,
                order = QuestionGetOrderParameter.desc,
                sort = QuestionGetSortParameter.created
            )
            val param2 = QuestionGetParameter(
                per = 3,
                page = 1,
                order = QuestionGetOrderParameter.desc,
                sort = QuestionGetSortParameter.like
            )
            val dto1 = questionServiceImpl1.getAll(param1).get()!!
            assertEquals(5, dto1.count)
            assertEquals(1, dto1.list[0].question_id)
            assertEquals(ss.p1name, dto1.list[0].program_name)
            assertEquals(ss.e1name, dto1.list[0].event_name)
            assertEquals(ss.q1dname, dto1.list[0].display_name)
            assertEquals(ss.q1date, dto1.list[0].created)
            assertEquals(ss.q1date, dto1.list[0].updated)
            assertEquals(ss.q1like.toInt(), dto1.list[0].like_count)
            assertEquals(0, dto1.list[0].reply_total)
            assertEquals(2, dto1.list[1].question_id)
            assertEquals(ss.p2name, dto1.list[1].program_name)
            assertEquals(ss.e2name, dto1.list[1].event_name)
            assertEquals(ss.q2dname, dto1.list[1].display_name)
            assertEquals(ss.q2date, dto1.list[1].created)
            assertEquals(ss.q2date, dto1.list[1].updated)
            assertEquals(ss.q2like.toInt(), dto1.list[1].like_count)
            assertEquals(0, dto1.list[1].reply_total)
            val dto2 = questionServiceImpl1.getAll(param2).get()!!
            assertEquals(5, dto2.count)
            assertEquals(3, dto2.list[0].question_id)
            assertEquals(ss.p3name, dto2.list[0].program_name)
            assertEquals(ss.e3name, dto2.list[0].event_name)
            assertEquals(ss.q3dname, dto2.list[0].display_name)
            assertEquals(ss.q3date, dto2.list[0].created)
            assertEquals(ss.q3date, dto2.list[0].updated)
            assertEquals(3, dto2.list[0].reply_total)
            assertEquals(ss.q3like.toInt(), dto2.list[0].like_count)
            assertEquals(4, dto2.list[1].question_id)
            assertEquals(ss.p4name, dto2.list[1].program_name)
            assertEquals(ss.e4name, dto2.list[1].event_name)
            assertEquals(ss.q4dname, dto2.list[1].display_name)
            assertEquals(ss.q4date, dto2.list[1].created)
            assertEquals(ss.q4date, dto2.list[1].updated)
            assertEquals(ss.q4like.toInt(), dto2.list[1].like_count)
            assertEquals(0, dto2.list[1].reply_total)
            assertEquals(5, dto2.list[2].question_id)
            assertEquals(ss.p5name, dto2.list[2].program_name)
            assertEquals(ss.e5name, dto2.list[2].event_name)
            assertEquals(ss.q5dname, dto2.list[2].display_name)
            assertEquals(ss.q5date, dto2.list[2].created)
            assertEquals(ss.q5date, dto2.list[2].updated)
            assertEquals(ss.q5like.toInt(), dto2.list[2].like_count)
            assertEquals(0, dto2.list[2].reply_total)
        }

        test("createQuestion()のmockテスト") {
            questionServiceImpl1.createQuestion("dummy").get()!!
        }

        test("incr()のmockテスト") {
            questionServiceImpl1.incrLike(1)
        }

        test("answer()のmockテスト") {
            questionServiceImpl1.answer(6).get()!!
        }

        test("addReply()のmockテスト") {
            val result = questionServiceImpl1.addReply(6, ss.q6reply1).get()!!
            println(result)
            assertEquals(ss.q6reply1, result.reply_list[0].comment)
            assertEquals(ss.q6reply1date, result.reply_list[0].created.millis.toDouble())
        }
    }
})

object QuestionServiceRealSpec: Spek({
    val ss = RepositorySpecSupport

    beforeGroup {
        ss.insertDummyData()
    }
    afterGroup {
        ss.dropDummyData()
    }

    group("QuestionServiceの実データテスト") {
        val kodein2 = Kodein {
            bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryImpl() }
            bind<LikeCountRepository>() with singleton { LikeCountRepositoryImpl() }
            bind<ReplyRepository>() with singleton { ReplyRepositoryImpl() }
        }
        val questionServiceImpl2 = QuestionServiceImpl(kodein2)

        test("getAll()での実データテスト") {
            val param1 = QuestionGetParameter(
                per = 2,
                page = 2,
                order = QuestionGetOrderParameter.desc,
                sort = QuestionGetSortParameter.created
            )
            val param2 = QuestionGetParameter(
                per = 3,
                page = 1,
                order = QuestionGetOrderParameter.asc,
                sort = QuestionGetSortParameter.like
            )
            val dto1 = questionServiceImpl2.getAll(param1).get()!!
            assertEquals(5, dto1.count)
            assertEquals(3, dto1.list[0].question_id)
            assertEquals(ss.p3name, dto1.list[0].program_name)
            assertEquals(ss.e3name, dto1.list[0].event_name)
            assertEquals(ss.q3dname, dto1.list[0].display_name)
            assertEquals(ss.q3date, dto1.list[0].created.toJST())
            assertEquals(ss.q3date, dto1.list[0].updated.toJST())
            assertEquals(ss.q3like.toInt(), dto1.list[0].like_count)
            assertEquals(3, dto1.list[0].reply_total)
            assertEquals(4, dto1.list[1].question_id)
            assertEquals(ss.p4name, dto1.list[1].program_name)
            assertEquals(ss.e4name, dto1.list[1].event_name)
            assertEquals(ss.q4dname, dto1.list[1].display_name)
            assertEquals(ss.q4date, dto1.list[1].created.toJST())
            assertEquals(ss.q4date, dto1.list[1].updated.toJST())
            assertEquals(ss.q4like.toInt(), dto1.list[1].like_count)
            assertEquals(0, dto1.list[1].reply_total)
            val dto2 = questionServiceImpl2.getAll(param2).get()!!
            assertEquals(5, dto2.count)
            assertEquals(2, dto2.list[0].question_id)
            assertEquals(ss.p2name, dto2.list[0].program_name)
            assertEquals(ss.e2name, dto2.list[0].event_name)
            assertEquals(ss.q2dname, dto2.list[0].display_name)
            assertEquals(ss.q2date, dto2.list[0].created.toJST())
            assertEquals(ss.q2date, dto2.list[0].updated.toJST())
            assertEquals(ss.q2like.toInt(), dto2.list[0].like_count)
            assertEquals(0, dto2.list[0].reply_total)
            assertEquals(1, dto2.list[1].question_id)
            assertEquals(ss.p1name, dto2.list[1].program_name)
            assertEquals(ss.e1name, dto2.list[1].event_name)
            assertEquals(ss.q1dname, dto2.list[1].display_name)
            assertEquals(ss.q1date, dto2.list[1].created.toJST())
            assertEquals(ss.q1date, dto2.list[1].updated.toJST())
            assertEquals(ss.q1like.toInt(), dto2.list[1].like_count)
            assertEquals(0, dto2.list[1].reply_total)
            assertEquals(5, dto2.list[2].question_id)
            assertEquals(ss.p5name, dto2.list[2].program_name)
            assertEquals(ss.e5name, dto2.list[2].event_name)
            assertEquals(ss.q5dname, dto2.list[2].display_name)
            assertEquals(ss.q5date, dto2.list[2].created.toJST())
            assertEquals(ss.q5date, dto2.list[2].updated.toJST())
            assertEquals(ss.q5like.toInt(), dto2.list[2].like_count)
            assertEquals(0, dto2.list[2].reply_total)
        }

        test("createQuestion()の実データテスト") {
            transaction {
                val dumComment = "dummy"
                val q = questionServiceImpl2.createQuestion(dumComment).get()!!
                assertNotNull(dumComment, q.comment)
            }
        }

        test("incr()の実データテスト") {
            questionServiceImpl2.incrLike(1)
            assertEquals(5.0, RedisContext.zscore(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, "1"))
        }

        test("answer()の実データテスト") {
            transaction {
                val before_todo_5 =
                    question.select { (question.id eq 5) and (question.done_flag eq true) }.map { it.toQuestion() }.firstOrNull()
                questionServiceImpl2.answer(5).get()!!
                val after_todo_5 = question.select { (question.id eq 5) and (question.done_flag eq true) }.map { it.toQuestion() }.firstOrNull()
                assertNull(before_todo_5)
                assertNotNull(after_todo_5)
            }
        }
    }
})