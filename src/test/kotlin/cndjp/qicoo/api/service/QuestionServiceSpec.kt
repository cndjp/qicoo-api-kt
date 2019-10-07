package api.service

import api.http_resource.paramater.question.QuestionGetOrderParameter
import api.http_resource.paramater.question.QuestionGetParameter
import api.http_resource.paramater.question.QuestionGetSortParameter
import api.service.question.QuestionServiceImpl
import domain.repository.LikeCountRepositoryMock
import domain.repository.QuestionAggrRepositoryMock
import domain.repository.like_count.LikeCountRepository
import domain.repository.question_aggr.QuestionAggrRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.spekframework.spek2.Spek
import utils.EntityResult
import utils.toDateTimeJstgForMySQL
import kotlin.test.assertEquals

object QuestionServiceSpec: Spek({
    val qMock = QuestionAggrRepositoryMock()
    val lMock = LikeCountRepositoryMock()
    val kodein = Kodein {
        bind<QuestionAggrRepository>() with singleton { qMock }
        bind<LikeCountRepository>() with singleton { lMock }
    }
    val questionServiceImpl = QuestionServiceImpl(kodein)

    group("QuestionServiceのmockテスト") {
        test("getAll()でのテスト") {
            val param1 = QuestionGetParameter(
                per =  2,
                page = 1,
                order = QuestionGetOrderParameter.desc,
                sort = QuestionGetSortParameter.created
            )
            val param2 = QuestionGetParameter(
                per =  3,
                page = 1,
                order = QuestionGetOrderParameter.desc,
                sort = QuestionGetSortParameter.like
            )
            val dto1 = questionServiceImpl.getAll(param1)
            assertEquals(2, dto1.count)
            assertEquals(1, dto1.list[0].qustion_id)
            assertEquals(qMock.p1name, dto1.list[0].program_name)
            assertEquals(qMock.e1name, dto1.list[0].event_name)
            assertEquals(qMock.q1dname, dto1.list[0].display_name)
            assertEquals(qMock.q1date.toDateTimeJstgForMySQL(), dto1.list[0].created)
            assertEquals(qMock.q1date.toDateTimeJstgForMySQL(), dto1.list[0].updated)
            assertEquals(lMock.q1like.toInt(), dto1.list[0].like_count)
            assertEquals(2, dto1.list[1].qustion_id)
            assertEquals(qMock.p2name, dto1.list[1].program_name)
            assertEquals(qMock.e2name, dto1.list[1].event_name)
            assertEquals(qMock.q2dname, dto1.list[1].display_name)
            assertEquals(qMock.q2date.toDateTimeJstgForMySQL(), dto1.list[1].created)
            assertEquals(qMock.q2date.toDateTimeJstgForMySQL(), dto1.list[1].updated)
            assertEquals(lMock.q2like.toInt(), dto1.list[1].like_count)
            val dto2 = questionServiceImpl.getAll(param2)
            assertEquals(3, dto2.count)
            assertEquals(3, dto2.list[0].qustion_id)
            assertEquals(qMock.p3name, dto2.list[0].program_name)
            assertEquals(qMock.e3name, dto2.list[0].event_name)
            assertEquals(qMock.q3dname, dto2.list[0].display_name)
            assertEquals(qMock.q3date.toDateTimeJstgForMySQL(), dto2.list[0].created)
            assertEquals(qMock.q3date.toDateTimeJstgForMySQL(), dto2.list[0].updated)
            assertEquals(lMock.q3like.toInt(), dto2.list[0].like_count)
            assertEquals(4, dto2.list[1].qustion_id)
            assertEquals(qMock.p4name, dto2.list[1].program_name)
            assertEquals(qMock.e4name, dto2.list[1].event_name)
            assertEquals(qMock.q4dname, dto2.list[1].display_name)
            assertEquals(qMock.q4date.toDateTimeJstgForMySQL(), dto2.list[1].created)
            assertEquals(qMock.q4date.toDateTimeJstgForMySQL(), dto2.list[1].updated)
            assertEquals(lMock.q4like.toInt(), dto2.list[1].like_count)
            assertEquals(5, dto2.list[2].qustion_id)
            assertEquals(qMock.p5name, dto2.list[2].program_name)
            assertEquals(qMock.e5name, dto2.list[2].event_name)
            assertEquals(qMock.q5dname, dto2.list[2].display_name)
            assertEquals(qMock.q5date.toDateTimeJstgForMySQL(), dto2.list[2].created)
            assertEquals(qMock.q5date.toDateTimeJstgForMySQL(), dto2.list[2].updated)
            assertEquals(lMock.q5like.toInt(), dto2.list[2].like_count)
        }

        test("createQuestion()のテスト") {
            assertEquals(EntityResult.Success, questionServiceImpl.createQuestion("dummy"))
        }

        test("incr()のテスト") {
            assertEquals(EntityResult.Success, questionServiceImpl.incr(1))
        }

        test("answer()のテスト") {
            assertEquals(EntityResult.Success, questionServiceImpl.answer(6))
        }
    }
})