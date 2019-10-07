package domain.repository.support

import domain.dao.done_question.NewDoneQuestion
import domain.dao.event.NewEvent
import domain.dao.program.NewProgram
import domain.dao.question.NewQuestion
import domain.dao.todo_question.NewTodoQuestion
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.linked_user.linked_user
import domain.model.program.program
import domain.model.question.question
import domain.model.reply.reply
import domain.model.todo_question.todo_question
import domain.model.unlinked_user.unlinked_user
import domain.model.user.user
import domain.repository.like_count.LikeCountRepositoryImpl
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import utils.getNowDateTimeJst
import utils.toDateTimeJst

object RepositorySpecSupport {
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
            name = ss.e1name
            start_at = ss.event1StartAt
            end_at = ss.event1EndAt
            created = ss.q1date
            updated = ss.q1date
        }

        val e2 = NewEvent.new {
            name = ss.e2name
            start_at = ss.event2StartAt
            end_at = ss.event2EndAt
            created = ss.q2date
            updated = ss.q2date
        }

        val e3 = NewEvent.new {
            name = ss.e3name
            start_at = ss.event3StartAt
            end_at = ss.event3EndAt
            created = ss.q3date
            updated = ss.q3date
        }

        val e4 = NewEvent.new {
            name = ss.e4name
            start_at = ss.event4StartAt
            end_at = ss.event4EndAt
            created = ss.q4date
            updated = ss.q4date
        }

        val e5 = NewEvent.new {
            name = ss.e5name
            start_at = ss.event5StartAt
            end_at = ss.event5EndAt
            created = ss.q5date
            updated = ss.q5date
        }

        val p1 = NewProgram.new {
            name = ss.p1name
            event_id = e1.id
            start_at = ss.program1StartAt
            end_at = ss.program1EndAt
            created = ss.q1date
            updated = ss.q1date
        }
        val p2 = NewProgram.new {
            name = ss.p2name
            event_id = e2.id
            start_at = ss.program2StartAt
            end_at = ss.program2EndAt
            created = ss.q2date
            updated = ss.q2date
        }
        val p3 = NewProgram.new {
            name = ss.p3name
            event_id = e3.id
            start_at = ss.program3StartAt
            end_at = ss.program3EndAt
            created = ss.q3date
            updated = ss.q3date
        }
        val p4 = NewProgram.new {
            name = ss.p4name
            event_id = e4.id
            start_at = ss.program4StartAt
            end_at = ss.program4EndAt
            created = ss.q4date
            updated = ss.q4date
        }
        val p5 = NewProgram.new {
            name = ss.p5name
            event_id = e5.id
            start_at = ss.program5StartAt
            end_at = ss.program5EndAt
            created = ss.q5date
            updated = ss.q5date
        }
        val q1 = NewQuestion.new {
            created = ss.q1date
            updated = ss.q1date
        }

        val q2 = NewQuestion.new {
            created = ss.q2date
            updated = ss.q2date
        }
        val q3 = NewQuestion.new {
            created = ss.q3date
            updated = ss.q3date
        }

        val q4 = NewQuestion.new {
            created = ss.q4date
            updated = ss.q4date
        }

        val q5 = NewQuestion.new {
            created = ss.q5date
            updated = ss.q5date
        }

        NewDoneQuestion(
            question_id = q1.id,
            program_id = p1.id,
            display_name = ss.q1dname,
            comment = ss.q1comment
        )
        NewDoneQuestion(
            question_id = q2.id,
            program_id = p2.id,
            display_name = ss.q2dname,
            comment = ss.q2comment
        )
        NewTodoQuestion(
            question_id = q3.id,
            program_id = p3.id,
            display_name = ss.q3dname,
            comment = ss.q3comment
        )
        NewTodoQuestion(
            question_id = q4.id,
            program_id = p4.id,
            display_name = ss.q4dname,
            comment = ss.q4comment
        )
        NewTodoQuestion(
            question_id = q5.id,
            program_id = p5.id,
            display_name = ss.q5dname,
            comment = ss.q5comment
        )
        RedisContext.zadd(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, mapOf(Pair("1", ss.q1like), Pair("2", ss.q2like), Pair("3", ss.q3like), Pair("4", ss.q4like), Pair("5", ss.q5like)))
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
