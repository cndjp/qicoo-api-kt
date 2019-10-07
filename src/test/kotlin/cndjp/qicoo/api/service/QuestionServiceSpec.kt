package api.service

import api.http_resource.paramater.question.QuestionGetOrderParameter
import api.http_resource.paramater.question.QuestionGetParameter
import api.http_resource.paramater.question.QuestionGetSortParameter
import api.service.question.QuestionServiceImpl
import domain.dao.done_question.toDoneQuestion
import domain.dao.todo_question.toTodoQuestion
import domain.model.done_question.done_question
import domain.model.todo_question.todo_question
import domain.repository.LikeCountRepositoryMock
import domain.repository.QuestionAggrRepositoryMock
import domain.repository.like_count.LikeCountRepository
import domain.repository.like_count.LikeCountRepositoryImpl
import domain.repository.question_aggr.QuestionAggrRepository
import domain.repository.question_aggr.QuestionAggrRepositoryImpl
import domain.repository.support.RepositorySpecSupport
import domain.repository.support.dropDummyData
import domain.repository.support.insertDummyData
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.assertEquals
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.spekframework.spek2.Spek
import utils.EntityResult
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object QuestionServiceMockSpec : Spek({
    val ss = RepositorySpecSupport

    group("QuestionServiceのmockテスト") {
        val kodein1 = Kodein {
            bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryMock() }
            bind<LikeCountRepository>() with singleton { LikeCountRepositoryMock() }
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
            val dto1 = questionServiceImpl1.getAll(param1)
            assertEquals(5, dto1.count)
            assertEquals(1, dto1.list[0].qustion_id)
            assertEquals(ss.p1name, dto1.list[0].program_name)
            assertEquals(ss.e1name, dto1.list[0].event_name)
            assertEquals(ss.q1dname, dto1.list[0].display_name)
            assertEquals(ss.q1date, dto1.list[0].created)
            assertEquals(ss.q1date, dto1.list[0].updated)
            assertEquals(ss.q1like.toInt(), dto1.list[0].like_count)
            assertEquals(2, dto1.list[1].qustion_id)
            assertEquals(ss.p2name, dto1.list[1].program_name)
            assertEquals(ss.e2name, dto1.list[1].event_name)
            assertEquals(ss.q2dname, dto1.list[1].display_name)
            assertEquals(ss.q2date, dto1.list[1].created)
            assertEquals(ss.q2date, dto1.list[1].updated)
            assertEquals(ss.q2like.toInt(), dto1.list[1].like_count)
            val dto2 = questionServiceImpl1.getAll(param2)
            assertEquals(5, dto2.count)
            assertEquals(3, dto2.list[0].qustion_id)
            assertEquals(ss.p3name, dto2.list[0].program_name)
            assertEquals(ss.e3name, dto2.list[0].event_name)
            assertEquals(ss.q3dname, dto2.list[0].display_name)
            assertEquals(ss.q3date, dto2.list[0].created)
            assertEquals(ss.q3date, dto2.list[0].updated)
            assertEquals(ss.q3like.toInt(), dto2.list[0].like_count)
            assertEquals(4, dto2.list[1].qustion_id)
            assertEquals(ss.p4name, dto2.list[1].program_name)
            assertEquals(ss.e4name, dto2.list[1].event_name)
            assertEquals(ss.q4dname, dto2.list[1].display_name)
            assertEquals(ss.q4date, dto2.list[1].created)
            assertEquals(ss.q4date, dto2.list[1].updated)
            assertEquals(ss.q4like.toInt(), dto2.list[1].like_count)
            assertEquals(5, dto2.list[2].qustion_id)
            assertEquals(ss.p5name, dto2.list[2].program_name)
            assertEquals(ss.e5name, dto2.list[2].event_name)
            assertEquals(ss.q5dname, dto2.list[2].display_name)
            assertEquals(ss.q5date, dto2.list[2].created)
            assertEquals(ss.q5date, dto2.list[2].updated)
            assertEquals(ss.q5like.toInt(), dto2.list[2].like_count)
        }

        test("createQuestion()のmockテスト") {
            assertEquals(EntityResult.Success, questionServiceImpl1.createQuestion("dummy"))
        }

        test("incr()のmockテスト") {
            assertEquals(EntityResult.Success, questionServiceImpl1.incr(1))
        }

        test("answer()のmockテスト") {
            assertEquals(EntityResult.Success, questionServiceImpl1.answer(6))
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
            val dto1 = questionServiceImpl2.getAll(param1)
            assertEquals(5, dto1.count)
            assertEquals(3, dto1.list[0].qustion_id)
            assertEquals(ss.p3name, dto1.list[0].program_name)
            assertEquals(ss.e3name, dto1.list[0].event_name)
            assertEquals(ss.q3dname, dto1.list[0].display_name)
            assertEquals(ss.q3date, dto1.list[0].created)
            assertEquals(ss.q3date, dto1.list[0].updated)
            assertEquals(ss.q3like.toInt(), dto1.list[0].like_count)
            assertEquals(4, dto1.list[1].qustion_id)
            assertEquals(ss.p4name, dto1.list[1].program_name)
            assertEquals(ss.e4name, dto1.list[1].event_name)
            assertEquals(ss.q4dname, dto1.list[1].display_name)
            assertEquals(ss.q4date, dto1.list[1].created)
            assertEquals(ss.q4date, dto1.list[1].updated)
            assertEquals(ss.q4like.toInt(), dto1.list[1].like_count)
            val dto2 = questionServiceImpl2.getAll(param2)
            assertEquals(5, dto2.count)
            assertEquals(2, dto2.list[0].qustion_id)
            assertEquals(ss.p2name, dto2.list[0].program_name)
            assertEquals(ss.e2name, dto2.list[0].event_name)
            assertEquals(ss.q2dname, dto2.list[0].display_name)
            assertEquals(ss.q2date, dto2.list[0].created)
            assertEquals(ss.q2date, dto2.list[0].updated)
            assertEquals(ss.q2like.toInt(), dto2.list[0].like_count)
            assertEquals(1, dto2.list[1].qustion_id)
            assertEquals(ss.p1name, dto2.list[1].program_name)
            assertEquals(ss.e1name, dto2.list[1].event_name)
            assertEquals(ss.q1dname, dto2.list[1].display_name)
            assertEquals(ss.q1date, dto2.list[1].created)
            assertEquals(ss.q1date, dto2.list[1].updated)
            assertEquals(ss.q1like.toInt(), dto2.list[1].like_count)
            assertEquals(5, dto2.list[2].qustion_id)
            assertEquals(ss.p5name, dto2.list[2].program_name)
            assertEquals(ss.e5name, dto2.list[2].event_name)
            assertEquals(ss.q5dname, dto2.list[2].display_name)
            assertEquals(ss.q5date, dto2.list[2].created)
            assertEquals(ss.q5date, dto2.list[2].updated)
            assertEquals(ss.q5like.toInt(), dto2.list[2].like_count)
        }

        test("createQuestion()の実データテスト") {
            transaction {
                assertEquals(EntityResult.Success, questionServiceImpl2.createQuestion("dummy"))
                val todo =
                    todo_question.select { todo_question.question_id eq 6 }.map { it.toTodoQuestion() }.firstOrNull()
                assertNotNull(todo)
                assertEquals(todo.question_id.value, 6)
                assertEquals(todo.comment, "dummy")
            }
        }

        test("incr()の実データテスト") {
            assertEquals(EntityResult.Success, questionServiceImpl2.incr(1))
            assertEquals(5.0, RedisContext.zscore(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, "1"))
        }

        test("answer()の実データテスト") {
            transaction {
                val before_todo_5 =
                    todo_question.select { todo_question.question_id eq 5 }.map { it.toTodoQuestion() }.firstOrNull()
                val before_todo_6 =
                    done_question.select { done_question.question_id eq 5 }.map { it.toDoneQuestion() }.firstOrNull()
                assertEquals(EntityResult.Success, questionServiceImpl2.answer(5))
                val after_todo_5 =
                    todo_question.select { todo_question.question_id eq 5 }.map { it.toTodoQuestion() }.firstOrNull()
                val after_todo_6 =
                    done_question.select { done_question.question_id eq 5 }.map { it.toDoneQuestion() }.firstOrNull()
                assertNull(after_todo_5)
                assertNotNull(before_todo_5)
                assertNull(before_todo_6)
                assertNotNull(after_todo_6)
            }
        }
    }
})