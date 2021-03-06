package test.cndjp.qicoo.infrastructure

import cndjp.qicoo.domain.dao.question.toQuestion
import cndjp.qicoo.domain.model.question.question
import cndjp.qicoo.infrastructure.repository.QuestionAggrRepositoryImpl
import test.cndjp.qicoo.infrastructure.support.migration_run.RepositorySpecSupport
import test.cndjp.qicoo.infrastructure.support.migration_run.dropDummyData
import test.cndjp.qicoo.infrastructure.support.migration_run.insertDummyData
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.jetbrains.exposed.sql.transactions.transaction
import org.spekframework.spek2.Spek
import cndjp.qicoo.utils.toJST
import com.github.michaelbull.result.get
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

object QuestionAggrRepositorySpec : Spek({
    val questionAggrRepositoryImpl = QuestionAggrRepositoryImpl()
    val ss = RepositorySpecSupport

    beforeGroup {
        ss.insertDummyData()
    }
    afterGroup {
        ss.dropDummyData()
    }
    group("QuestionAggrRepositoryのテスト") {
        test("findAll()のテスト") {
            transaction {
                val list1 = questionAggrRepositoryImpl.findAll(2, 1, "desc")
                assertEquals(ss.q1comment, list1.list[0].comment)
                assertEquals(ss.e1name, list1.list[0].event_name)
                assertEquals(ss.p1name, list1.list[0].program_name)
                assertEquals(ss.q1comment, list1.list[0].comment)
                assertEquals(ss.q1dname, list1.list[0].display_name)
                assertEquals(ss.q1date, list1.list[0].created.toJST())
                assertEquals(ss.q1date, list1.list[0].updated.toJST())
                assertEquals(ss.q2comment, list1.list[1].comment)
                assertEquals(ss.e2name, list1.list[1].event_name)
                assertEquals(ss.p2name, list1.list[1].program_name)
                assertEquals(ss.q2comment, list1.list[1].comment)
                assertEquals(ss.q2dname, list1.list[1].display_name)
                assertEquals(ss.q2date, list1.list[1].created.toJST())
                assertEquals(ss.q2date, list1.list[1].updated.toJST())
                val list2 = questionAggrRepositoryImpl.findAll(1, 1, "asc")
                assertEquals(ss.q5comment, list2.list[0].comment)
                assertEquals(ss.e5name, list2.list[0].event_name)
                assertEquals(ss.p5name, list2.list[0].program_name)
                assertEquals(ss.q5comment, list2.list[0].comment)
                assertEquals(ss.q5dname, list2.list[0].display_name)
                assertEquals(ss.q5date, list2.list[0].created.toJST())
                assertEquals(ss.q5date, list2.list[0].updated.toJST())
                val list3 = questionAggrRepositoryImpl.findAll(1, 3, "desc")
                assertEquals(ss.q3comment, list3.list[0].comment)
                assertEquals(ss.e3name, list3.list[0].event_name)
                assertEquals(ss.p3name, list3.list[0].program_name)
                assertEquals(ss.q3comment, list3.list[0].comment)
                assertEquals(ss.q3dname, list3.list[0].display_name)
                assertEquals(ss.q3date, list3.list[0].created.toJST())
                assertEquals(ss.q3date, list3.list[0].updated.toJST())
            }
        }
        test("findByIds()のテスト") {
            val result1 = questionAggrRepositoryImpl.findByIds(listOf(2, 3, 4)).get()!!
            val list1 = result1.list.sortedByDescending { it.created.toJST() }
            assertEquals(ss.q2comment, list1[0].comment)
            assertEquals(ss.e2name, list1[0].event_name)
            assertEquals(ss.p2name, list1[0].program_name)
            assertEquals(ss.q2comment, list1[0].comment)
            assertEquals(ss.q2dname, list1[0].display_name)
            assertEquals(ss.q2date, list1[0].created.toJST())
            assertEquals(ss.q2date, list1[0].updated.toJST())
            assertEquals(ss.q3comment, list1[1].comment)
            assertEquals(ss.e3name, list1[1].event_name)
            assertEquals(ss.p3name, list1[1].program_name)
            assertEquals(ss.q3comment, list1[1].comment)
            assertEquals(ss.q3dname, list1[1].display_name)
            assertEquals(ss.q3date, list1[1].created.toJST())
            assertEquals(ss.q3date, list1[1].updated.toJST())
            assertEquals(ss.q4comment, list1[2].comment)
            assertEquals(ss.e4name, list1[2].event_name)
            assertEquals(ss.p4name, list1[2].program_name)
            assertEquals(ss.q4comment, list1[2].comment)
            assertEquals(ss.q4dname, list1[2].display_name)
            assertEquals(ss.q4date, list1[2].created.toJST())
            assertEquals(ss.q4date, list1[2].updated.toJST())
            val result2 = questionAggrRepositoryImpl.findByIds(listOf(1, 4)).get()!!
            val list2 = result2.list.sortedByDescending { it.created.toJST() }
            assertEquals(ss.q1comment, list2[0].comment)
            assertEquals(ss.e1name, list2[0].event_name)
            assertEquals(ss.p1name, list2[0].program_name)
            assertEquals(ss.q1comment, list2[0].comment)
            assertEquals(ss.q1dname, list2[0].display_name)
            assertEquals(ss.q1date, list2[0].created.toJST())
            assertEquals(ss.q1date, list2[0].updated.toJST())
            assertEquals(ss.q4comment, list2[1].comment)
            assertEquals(ss.e4name, list2[1].event_name)
            assertEquals(ss.p4name, list2[1].program_name)
            assertEquals(ss.q4comment, list2[1].comment)
            assertEquals(ss.q4dname, list2[1].display_name)
            assertEquals(ss.q4date, list2[1].created.toJST())
            assertEquals(ss.q4date, list2[1].updated.toJST())
        }
        test("insert()のテスト") {
            val newQuestionComment1 = "what is puyopuyo?"
            val newQuestionComment2 = "what is pokemon?"
            transaction {
                val result1 = questionAggrRepositoryImpl.insert(newQuestionComment1)
                val result2 = questionAggrRepositoryImpl.insert(newQuestionComment2)
                assertNotNull(result1)
                assertNotNull(result2)
                assertNotNull(question.select { question.comment eq newQuestionComment1 }.map { it.toQuestion() }.firstOrNull())
                assertNotNull(question.select { question.comment eq newQuestionComment2 }.map { it.toQuestion() }.firstOrNull())
            }
        }
        test("todo2done()のテスト") {
            transaction {
                assertNull(question.select { (question.id eq 5) and (question.done_flag eq true ) }.map { it.toQuestion() }.firstOrNull())
                val result5 = questionAggrRepositoryImpl.todo2done(5)
                assertNotNull(result5)
                assertNotNull(question.select { (question.id eq 5) and (question.done_flag eq true ) }.map { it.toQuestion() }.firstOrNull())
            }
        }
    }
})
