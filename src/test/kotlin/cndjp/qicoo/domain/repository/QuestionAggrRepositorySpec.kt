package domain.repository

import domain.dao.done_question.NewDoneQuestion
import domain.dao.done_question.toDoneQuestion
import domain.dao.event.NewEvent
import domain.dao.program.NewProgram
import domain.dao.question.NewQuestion
import domain.dao.todo_question.NewTodoQuestion
import domain.dao.todo_question.toTodoQuestion
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import domain.repository.question_aggr.QuestionAggrRepositoryImpl
import infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.spekframework.spek2.Spek
import utils.getNowDateTimeJst
import utils.toDateTimeJst
import utils.toJST
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object QuestionAggrRepositorySpec: Spek ({
    val q1date = getNowDateTimeJst()
    val q2date = q1date.minusDays(1)
    val q3date = q2date.minusDays(1)
    val q4date = q3date.minusDays(1)
    val q5date = q4date.minusDays(1)
    val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
    val event1EndAt = "2018-01-02 19:00:00".toDateTimeJst()
    val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
    val event2EndAt = "2018-02-02 19:00:00".toDateTimeJst()
    val event3StartAt = "2018-03-01 19:00:00".toDateTimeJst()
    val event3EndAt = "2018-03-02 19:00:00".toDateTimeJst()
    val event4StartAt = "2018-04-01 19:00:00".toDateTimeJst()
    val event4EndAt = "2018-04-02 19:00:00".toDateTimeJst()
    val event5StartAt = "2019-09-01 19:00:00".toDateTimeJst()
    val event5EndAt = "2200-04-01 19:30:00".toDateTimeJst()
    val program1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
    val program1EndAt = "2019-01-01 19:30:00".toDateTimeJst()
    val program2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
    val program2EndAt = "2018-02-01 19:30:00".toDateTimeJst()
    val program3StartAt = "2018-03-01 19:00:00".toDateTimeJst()
    val program3EndAt = "2019-03-01 19:30:00".toDateTimeJst()
    val program4StartAt = "2018-04-01 19:00:00".toDateTimeJst()
    val program4EndAt = "2018-04-01 19:30:00".toDateTimeJst()
    val program5StartAt = "2019-09-01 19:00:00".toDateTimeJst()
    val program5EndAt = "2300-04-01 19:30:00".toDateTimeJst()
    val e1name = "event_name1"
    val e2name = "event_name2"
    val e3name = "event_name3"
    val e4name = "event_name4"
    val e5name = "event_name5"
    val p1name = "program_name1"
    val p2name = "program_name2"
    val p3name = "program_name3"
    val p4name = "program_name4"
    val p5name = "program_name5"
    val q1dname = "nyan"
    val q1comment = "what is qicoo"
    val q2dname = "hyon"
    val q2comment = "what is mayo"
    val q3dname = "poe"
    val q3comment = "what is poe"
    val q4dname = "puyo"
    val q4comment = "what is myas"
    val q5dname = "mere"
    val q5comment = "what is hugai"
    val questionAggrRepositoryImpl = QuestionAggrRepositoryImpl()

    beforeGroup {
        initMysqlClient()
        transaction {
            SchemaUtils.drop(
                question,
                done_question,
                todo_question,
                event,
                program
            )

            SchemaUtils.create(
                question,
                done_question,
                todo_question,
                event,
                program
            )

            val e1 = NewEvent.new {
                name = e1name
                start_at = event1StartAt
                end_at = event1EndAt
                created = q1date
                updated = q1date
            }

            val e2 = NewEvent.new {
                name = e2name
                start_at = event2StartAt
                end_at = event2EndAt
                created = q2date
                updated = q2date
            }

            val e3 = NewEvent.new {
                name = e3name
                start_at = event3StartAt
                end_at = event3EndAt
                created = q3date
                updated = q3date
            }

            val e4 = NewEvent.new {
                name = e4name
                start_at = event4StartAt
                end_at = event4EndAt
                created = q4date
                updated = q4date
            }

            val e5 = NewEvent.new {
                name = e5name
                start_at = event5StartAt
                end_at = event5EndAt
                created = q5date
                updated = q5date
            }

            val p1 = NewProgram.new {
                name = p1name
                event_id = e1.id
                start_at = program1StartAt
                end_at = program1EndAt
                created = q1date
                updated = q1date
            }
            val p2 = NewProgram.new {
                name = p2name
                event_id = e2.id
                start_at = program2StartAt
                end_at = program2EndAt
                created = q2date
                updated = q2date
            }
            val p3 = NewProgram.new {
                name = p3name
                event_id = e3.id
                start_at = program3StartAt
                end_at = program3EndAt
                created = q3date
                updated = q3date
            }
            val p4 = NewProgram.new {
                name = p4name
                event_id = e4.id
                start_at = program4StartAt
                end_at = program4EndAt
                created = q4date
                updated = q4date
            }
            val p5 = NewProgram.new {
                name = p5name
                event_id = e5.id
                start_at = program5StartAt
                end_at = program5EndAt
                created = q5date
                updated = q5date
            }
            val q1 = NewQuestion.new {
                created = q1date
                updated = q1date
            }

            val q2 = NewQuestion.new {
                created = q2date
                updated = q2date
            }
            val q3 = NewQuestion.new {
                created = q3date
                updated = q3date
            }

            val q4 = NewQuestion.new {
                created = q4date
                updated = q4date
            }

            val q5 = NewQuestion.new {
                created = q5date
                updated = q5date
            }

            NewDoneQuestion(
                question_id = q1.id,
                program_id = p1.id,
                display_name = q1dname,
                comment = q1comment
            )
            NewDoneQuestion(
                question_id = q2.id,
                program_id = p2.id,
                display_name = q2dname,
                comment = q2comment
            )
            NewTodoQuestion(
                question_id = q3.id,
                program_id = p3.id,
                display_name = q3dname,
                comment = q3comment
            )
            NewTodoQuestion(
                question_id = q4.id,
                program_id = p4.id,
                display_name = q4dname,
                comment = q4comment
            )
            NewTodoQuestion(
                question_id = q5.id,
                program_id = p5.id,
                display_name = q5dname,
                comment = q5comment
            )
        }
    }
    afterGroup {
        transaction {
            SchemaUtils.drop(
                question,
                done_question,
                todo_question,
                event,
                program
            )
        }
    }
    group("QuestionAggrRepositoryのテスト") {
        test("findAll()のテスト") {
            transaction {
                val list1 = questionAggrRepositoryImpl.findAll(2, 1, "desc")
                assertEquals(q1comment, list1.list[0].comment)
                assertEquals(e1name, list1.list[0].event_name)
                assertEquals(p1name, list1.list[0].program_name)
                assertEquals(q1comment, list1.list[0].comment)
                assertEquals(q1dname, list1.list[0].display_name)
                assertEquals(q1date, list1.list[0].created.toJST())
                assertEquals(q1date, list1.list[0].updated.toJST())
                assertEquals(q2comment, list1.list[1].comment)
                assertEquals(e2name, list1.list[1].event_name)
                assertEquals(p2name, list1.list[1].program_name)
                assertEquals(q2comment, list1.list[1].comment)
                assertEquals(q2dname, list1.list[1].display_name)
                assertEquals(q2date, list1.list[1].created.toJST())
                assertEquals(q2date, list1.list[1].updated.toJST())
                val list2 = questionAggrRepositoryImpl.findAll(1, 1, "asc")
                assertEquals(q5comment, list2.list[0].comment)
                assertEquals(e5name, list2.list[0].event_name)
                assertEquals(p5name, list2.list[0].program_name)
                assertEquals(q5comment, list2.list[0].comment)
                assertEquals(q5dname, list2.list[0].display_name)
                assertEquals(q5date, list2.list[0].created.toJST())
                assertEquals(q5date, list2.list[0].updated.toJST())
                val list3 = questionAggrRepositoryImpl.findAll(1, 3, "desc")
                assertEquals(q3comment, list3.list[0].comment)
                assertEquals(e3name, list3.list[0].event_name)
                assertEquals(p3name, list3.list[0].program_name)
                assertEquals(q3comment, list3.list[0].comment)
                assertEquals(q3dname, list3.list[0].display_name)
                assertEquals(q3date, list3.list[0].created.toJST())
                assertEquals(q3date, list3.list[0].updated.toJST())
            }
        }
        test("findByIds()のテスト") {
            val result1 = questionAggrRepositoryImpl.findByIds(listOf(2, 3, 4))
            val list1 = result1.list.sortedByDescending { it.created.toJST() }
            assertEquals(q2comment, list1[0].comment)
            assertEquals(e2name, list1[0].event_name)
            assertEquals(p2name, list1[0].program_name)
            assertEquals(q2comment, list1[0].comment)
            assertEquals(q2dname, list1[0].display_name)
            assertEquals(q2date, list1[0].created.toJST())
            assertEquals(q2date, list1[0].updated.toJST())
            assertEquals(q3comment, list1[1].comment)
            assertEquals(e3name, list1[1].event_name)
            assertEquals(p3name, list1[1].program_name)
            assertEquals(q3comment, list1[1].comment)
            assertEquals(q3dname, list1[1].display_name)
            assertEquals(q3date, list1[1].created.toJST())
            assertEquals(q3date, list1[1].updated.toJST())
            assertEquals(q4comment, list1[2].comment)
            assertEquals(e4name, list1[2].event_name)
            assertEquals(p4name, list1[2].program_name)
            assertEquals(q4comment, list1[2].comment)
            assertEquals(q4dname, list1[2].display_name)
            assertEquals(q4date, list1[2].created.toJST())
            assertEquals(q4date, list1[2].updated.toJST())
            val result2 = questionAggrRepositoryImpl.findByIds(listOf(1, 4))
            val list2 = result2.list.sortedByDescending { it.created.toJST() }
            assertEquals(q1comment, list2[0].comment)
            assertEquals(e1name, list2[0].event_name)
            assertEquals(p1name, list2[0].program_name)
            assertEquals(q1comment, list2[0].comment)
            assertEquals(q1dname, list2[0].display_name)
            assertEquals(q1date, list2[0].created.toJST())
            assertEquals(q1date, list2[0].updated.toJST())
            assertEquals(q4comment, list2[1].comment)
            assertEquals(e4name, list2[1].event_name)
            assertEquals(p4name, list2[1].program_name)
            assertEquals(q4comment, list2[1].comment)
            assertEquals(q4dname, list2[1].display_name)
            assertEquals(q4date, list2[1].created.toJST())
            assertEquals(q4date, list2[1].updated.toJST())
        }
        test("insert()のテスト") {
            val newQuestionComment1 = "what is puyopuyo?"
            val newQuestionComment2 = "what is pokemon?"
            transaction {
                val result1 = questionAggrRepositoryImpl.insert(newQuestionComment1)
                val result2 = questionAggrRepositoryImpl.insert(newQuestionComment2)
                assertNotNull(result1)
                assertNotNull(result2)
                assertNotNull(todo_question.select { todo_question.comment eq newQuestionComment1 }.map { it.toTodoQuestion() }.firstOrNull())
                assertNotNull(todo_question.select { todo_question.comment eq newQuestionComment2 }.map { it.toTodoQuestion() }.firstOrNull())
            }
        }
        test("todo2done()のテスト") {
            transaction {
                assertNotNull(todo_question.select { todo_question.question_id eq 5 }.map { it.toTodoQuestion() }.firstOrNull())
                assertNull(done_question.select { done_question.question_id eq 5 }.map { it.toDoneQuestion() }.firstOrNull())
                val result5 = questionAggrRepositoryImpl.todo2done(5)
                assertNotNull(result5)
                assertNull(todo_question.select { todo_question.question_id eq 5 }.map { it.toTodoQuestion() }.firstOrNull())
                assertNotNull(done_question.select { done_question.question_id eq 5 }.map { it.toDoneQuestion() }.firstOrNull())
            }
        }
    }
})