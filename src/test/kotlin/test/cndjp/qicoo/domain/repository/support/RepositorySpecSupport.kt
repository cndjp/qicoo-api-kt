package test.cndjp.qicoo.domain.repository.support

import cndjp.qicoo.domain.dao.done_question.NewDoneQuestion
import cndjp.qicoo.domain.dao.event.NewEvent
import cndjp.qicoo.domain.dao.program.NewProgram
import cndjp.qicoo.domain.dao.question.NewQuestion
import cndjp.qicoo.domain.dao.todo_question.NewTodoQuestion
import cndjp.qicoo.domain.model.done_question.done_question
import cndjp.qicoo.domain.model.event.event
import cndjp.qicoo.domain.model.linked_user.linked_user
import cndjp.qicoo.domain.model.program.program
import cndjp.qicoo.domain.model.question.question
import cndjp.qicoo.domain.model.reply.reply
import cndjp.qicoo.domain.model.todo_question.todo_question
import cndjp.qicoo.domain.model.unlinked_user.unlinked_user
import cndjp.qicoo.domain.model.user.user
import cndjp.qicoo.domain.repository.like_count.LikeCountRepositoryImpl
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import cndjp.qicoo.utils.toDateTimeJst
import cndjp.qicoo.utils.toDateTimeJstForMySQL

object RepositorySpecSupport {
    val q1date = "2020-10-10 19:00:00.11110000".toDateTimeJstForMySQL()
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
    val p6name = "program_name6"
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
    val q6comment = "what is kiali"
    val q1like = 4.0
    val q2like = 1.0
    val q3like = 10.0
    val q4like = 8.0
    val q5like = 6.0
}

fun RepositorySpecSupport.insertDummyData() {
    val ss = this
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
        RedisContext.zadd(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, mapOf(Pair("1", q1like), Pair("2", q2like), Pair("3", q3like), Pair("4", q4like), Pair("5", q5like)))
    }
}

fun RepositorySpecSupport.dropDummyData() {
    initMysqlClient()
    transaction {
        SchemaUtils.drop(
            question,
            reply,
            done_question,
            todo_question,
            event,
            program,
            user,
            linked_user,
            unlinked_user
        )
    }
    RedisContext.flushAll(qicooGlobalJedisPool.resource)
}