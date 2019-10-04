package domain

import domain.dao.done_question.NewDoneQuestion
import domain.dao.todo_question.NewTodoQuestion
import domain.dao.done_question.toDoneQuestion
import domain.dao.event.Event
import domain.dao.event.NewEvent
import domain.dao.event.toEvent
import domain.dao.linked_user.LinkedUser
import domain.dao.linked_user.NewLinkedUser
import domain.dao.linked_user.toLinkedUser
import domain.dao.program.NewProgram
import domain.dao.program.Program
import domain.dao.program.toProgram
import domain.dao.question.NewQuestion
import domain.dao.question.Question
import domain.dao.question.toQuestion
import domain.dao.reply.NewReply
import domain.dao.reply.Reply
import domain.dao.reply.toReply
import domain.dao.todo_question.toTodoQuestion
import domain.dao.unlinked_user.NewUnlinkedUser
import domain.dao.unlinked_user.UnlinkedUser
import domain.dao.unlinked_user.toUnlinkedUser
import domain.dao.user.NewUser
import domain.dao.user.User
import domain.dao.user.toUser
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.linked_user.linked_user
import domain.model.program.program
import domain.model.question.question
import domain.model.reply.reply
import domain.model.todo_question.todo_question
import domain.model.unlinked_user.unlinked_user
import domain.model.user.user
import infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.Duration
import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import utils.getNowDateTimeJst
import utils.toDateTimeJst
import utils.toJST

object MySQLDomainSpec : Spek({
    group("mysqlのテスト") {
        test("questionのCRUDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)

            transaction {
                SchemaUtils.create(
                    question
                )

                NewQuestion.new {
                    created = now
                    updated = now
                }

                NewQuestion.new {
                    created = yesterday
                    updated = yesterday
                }

                val r1 = question.select { question.created eq now }.map { it.toQuestion() }.first()
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = question.update({ question.created eq now }) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(updatedCount, 1)

                val rl: MutableList<Question> = mutableListOf()
                question.selectAll().orderBy(question.created to SortOrder.DESC).forEach {
                    rl.add(it.toQuestion())
                }
                assertEquals(now, rl[0].created.toJST())
                assertEquals(oneMilliSecondAgo, rl[0].updated.toJST())
                assertEquals(yesterday, rl[1].created.toJST())
                assertEquals(yesterday, rl[1].updated.toJST())

                val deleteCount1 = question.deleteWhere { question.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = question.deleteWhere { question.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    question
                )
            }
        }
        test("done_questionのCRDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val yesterday = now.minusDays(1)
            val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event1EndAt = "2018-01-02 19:00:00".toDateTimeJst()
            val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val event2EndAt = "2018-02-02 19:00:00".toDateTimeJst()
            val program1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val program1EndAt = "2018-01-01 19:30:00".toDateTimeJst()
            val program2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val program2EndAt = "2018-02-01 19:30:00".toDateTimeJst()
            val e1name = "poe"
            val e2name = "fae"
            val p1name = "poke"
            val p2name = "moka"
            val d1name = "nyan"
            val d1comment = "what is qicoo"
            val d2name = "hyon"
            val d2comment = "what is mayo"

            transaction {
                SchemaUtils.create(
                    event,
                    question,
                    done_question,
                    program
                )

                val e1 = NewEvent.new {
                    name = e1name
                    start_at = event1StartAt
                    end_at = event1EndAt
                    created = now
                    updated = now
                }

                val e2 = NewEvent.new {
                    name = e2name
                    start_at = event2StartAt
                    end_at = event2EndAt
                    created = yesterday
                    updated = yesterday
                }

                val p1 = NewProgram.new {
                    name = p1name
                    event_id = e1.id
                    start_at = program1StartAt
                    end_at = program1EndAt
                    created = now
                    updated = now
                }

                val p2 = NewProgram.new {
                    name = p2name
                    event_id = e2.id
                    start_at = program2StartAt
                    end_at = program2EndAt
                    created = yesterday
                    updated = yesterday
                }
                val q1 = NewQuestion.new {
                    created = now
                    updated = now
                }

                val q2 = NewQuestion.new {
                    created = yesterday
                    updated = yesterday
                }

                NewDoneQuestion(question_id = q1.id, program_id = p1.id, display_name = d1name, comment = d1comment)
                NewDoneQuestion(question_id = q2.id, program_id = p2.id, display_name = d2name, comment = d2comment)

                val r1 = done_question.select { done_question.question_id eq q1.id }.map { it.toDoneQuestion() }.first()
                assertEquals(p1.id, r1.program_id)
                assertEquals(d1name, r1.display_name)
                assertEquals(d1comment, r1.comment)

                val r2 = done_question.select { done_question.question_id eq q2.id }.map { it.toDoneQuestion() }.first()
                assertEquals(p2.id, r2.program_id)
                assertEquals(d2name, r2.display_name)
                assertEquals(d2comment, r2.comment)

                val deleteCount1 = done_question.deleteWhere { done_question.question_id eq q1.id }
                assertEquals(1, deleteCount1)
                val deleteCount2 = done_question.deleteWhere { done_question.question_id eq q2.id }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    event,
                    question,
                    done_question,
                    program
                )
            }
        }
        test("todo_questionのCRDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val yesterday = now.minusDays(1)
            val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event1EndAt = "2018-01-02 19:00:00".toDateTimeJst()
            val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val event2EndAt = "2018-02-02 19:00:00".toDateTimeJst()
            val program1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val program1EndAt = "2018-01-01 19:30:00".toDateTimeJst()
            val program2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val program2EndAt = "2018-02-01 19:30:00".toDateTimeJst()
            val e1name = "poe"
            val e2name = "fae"
            val p1name = "poke"
            val p2name = "moka"
            val t1name = "nyan"
            val t1comment = "what is qicoo"
            val t2name = "hyon"
            val t2comment = "what is mayo"

            transaction {
                SchemaUtils.create(
                    event,
                    question,
                    todo_question,
                    program
                )

                val e1 = NewEvent.new {
                    name = e1name
                    start_at = event1StartAt
                    end_at = event1EndAt
                    created = now
                    updated = now
                }

                val e2 = NewEvent.new {
                    name = e2name
                    start_at = event2StartAt
                    end_at = event2EndAt
                    created = yesterday
                    updated = yesterday
                }

                val p1 = NewProgram.new {
                    name = p1name
                    event_id = e1.id
                    start_at = program1StartAt
                    end_at = program1EndAt
                    created = now
                    updated = now
                }

                val p2 = NewProgram.new {
                    name = p2name
                    event_id = e2.id
                    start_at = program2StartAt
                    end_at = program2EndAt
                    created = yesterday
                    updated = yesterday
                }
                val q1 = NewQuestion.new {
                    created = now
                    updated = now
                }

                val q2 = NewQuestion.new {
                    created = yesterday
                    updated = yesterday
                }

                NewTodoQuestion(question_id = q1.id, program_id = p1.id, display_name = t1name, comment = t1comment)
                NewTodoQuestion(question_id = q2.id, program_id = p2.id, display_name = t2name, comment = t2comment)

                val r1 = todo_question.select { todo_question.question_id eq q1.id }.map { it.toTodoQuestion() }.first()
                assertEquals(p1.id, r1.program_id)
                assertEquals(t1name, r1.display_name)
                assertEquals(t1comment, r1.comment)

                val r2 = todo_question.select { todo_question.question_id eq q2.id }.map { it.toTodoQuestion() }.first()
                assertEquals(p2.id, r2.program_id)
                assertEquals(t2name, r2.display_name)
                assertEquals(t2comment, r2.comment)

                val deleteCount1 = todo_question.deleteWhere { todo_question.question_id eq q1.id }
                assertEquals(1, deleteCount1)
                val deleteCount2 = todo_question.deleteWhere { todo_question.question_id eq q2.id }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    event,
                    question,
                    todo_question,
                    program
                )
            }
        }
        test("replyのCRUDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)
            val comment1 = "what is this?"
            val comment2 = "where is from?"

            transaction {
                SchemaUtils.create(
                    question,
                    reply
                )

                val q1 = NewQuestion.new {
                    created = now
                    updated = now
                }

                val q2 = NewQuestion.new {
                    created = yesterday
                    updated = yesterday
                }

                NewReply.new {
                    question_id = q1.id
                    comment = comment1
                    created = now
                    updated = now
                }

                NewReply.new {
                    question_id = q2.id
                    comment = comment2
                    created = yesterday
                    updated = yesterday
                }

                val r1 = reply.select { reply.created eq now }.map { it.toReply() }.first()
                assertEquals(q1.id, r1.question_id)
                assertEquals(comment1, r1.comment)
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = reply.update({ reply.created eq now }) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(updatedCount, 1)

                val rl: MutableList<Reply> = mutableListOf()
                reply.selectAll().orderBy(reply.created to SortOrder.DESC).forEach {
                    rl.add(it.toReply())
                }
                assertEquals(q1.id, rl[0].question_id)
                assertEquals(comment1, rl[0].comment)
                assertEquals(now, rl[0].created.toJST())
                assertEquals(oneMilliSecondAgo, rl[0].updated.toJST())
                assertEquals(q2.id, rl[1].question_id)
                assertEquals(comment2, rl[1].comment)
                assertEquals(yesterday, rl[1].created.toJST())
                assertEquals(yesterday, rl[1].updated.toJST())

                val deleteCount1 = reply.deleteWhere { reply.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = reply.deleteWhere { reply.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    question,
                    reply
                )
            }
        }
        test("eventのCRUDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)
            val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event1EndAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val event2EndAt = "2018-02-01 19:00:00".toDateTimeJst()
            val e1name = "poe"
            val e2name = "fae"

            transaction {
                SchemaUtils.create(
                    event
                )

                NewEvent.new {
                    name = e1name
                    start_at = event1StartAt
                    end_at = event1EndAt
                    created = now
                    updated = now
                }

                NewEvent.new {
                    name = e2name
                    start_at = event2StartAt
                    end_at = event2EndAt
                    created = yesterday
                    updated = yesterday
                }

                val r1 = event.select { event.created eq now }.map { it.toEvent() }.first()
                assertEquals(event1StartAt, r1.start_at.toJST())
                assertEquals(event1EndAt, r1.end_at.toJST())
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = event.update({ event.created eq now }) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(1, updatedCount)

                val rl: MutableList<Event> = mutableListOf()
                event.selectAll().orderBy(event.created to SortOrder.DESC).forEach {
                    rl.add(it.toEvent())
                }
                assertEquals(event1StartAt, rl[0].start_at.toJST())
                assertEquals(event1EndAt, rl[0].end_at.toJST())
                assertEquals(now, rl[0].created.toJST())
                assertEquals(oneMilliSecondAgo, rl[0].updated.toJST())
                assertEquals(event2StartAt, rl[1].start_at.toJST())
                assertEquals(event2EndAt, rl[1].end_at.toJST())
                assertEquals(yesterday, rl[1].created.toJST())
                assertEquals(yesterday, rl[1].updated.toJST())

                val deleteCount1 = event.deleteWhere { event.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = event.deleteWhere { event.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    event
                )
            }
        }
        test("programのCRUDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)
            val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event1EndAt = "2018-01-02 19:00:00".toDateTimeJst()
            val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val event2EndAt = "2018-02-02 19:00:00".toDateTimeJst()
            val program1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val program1EndAt = "2018-01-01 19:30:00".toDateTimeJst()
            val program2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val program2EndAt = "2018-02-01 19:30:00".toDateTimeJst()
            val e1name = "poe"
            val e2name = "fae"
            val p1name = "poke"
            val p2name = "moka"

            transaction {
                SchemaUtils.create(
                    event,
                    program
                )

                val e1 = NewEvent.new {
                    name = e1name
                    start_at = event1StartAt
                    end_at = event1EndAt
                    created = now
                    updated = now
                }

                val e2 = NewEvent.new {
                    name = e2name
                    start_at = event2StartAt
                    end_at = event2EndAt
                    created = yesterday
                    updated = yesterday
                }

                NewProgram.new {
                    name = p1name
                    event_id = e1.id
                    start_at = program1StartAt
                    end_at = program1EndAt
                    created = now
                    updated = now
                }

                NewProgram.new {
                    name = p2name
                    event_id = e2.id
                    start_at = program2StartAt
                    end_at = program2EndAt
                    created = yesterday
                    updated = yesterday
                }

                val r1 = program.select { program.created eq now }.map { it.toProgram() }.first()
                assertEquals(e1.id, r1.event_id)
                assertEquals(program1StartAt, r1.start_at.toJST())
                assertEquals(program1EndAt, r1.end_at.toJST())
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = program.update({ program.created eq now }) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(1, updatedCount)

                val srl: MutableList<Program> = mutableListOf()
                program.selectAll().orderBy(program.created to SortOrder.DESC).forEach {
                    srl.add(it.toProgram())
                }
                assertEquals(e1.id, srl[0].event_id)
                assertEquals(program1StartAt, srl[0].start_at.toJST())
                assertEquals(program1EndAt, srl[0].end_at.toJST())
                assertEquals(now, srl[0].created.toJST())
                assertEquals(oneMilliSecondAgo, srl[0].updated.toJST())
                assertEquals(e2.id, srl[1].event_id)
                assertEquals(program2StartAt, srl[1].start_at.toJST())
                assertEquals(program2EndAt, srl[1].end_at.toJST())
                assertEquals(yesterday, srl[1].created.toJST())
                assertEquals(yesterday, srl[1].updated.toJST())

                val deleteCount1 = program.deleteWhere { program.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = program.deleteWhere { program.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    event,
                    program
                )
            }
        }
        test("userのCRDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)

            transaction {
                SchemaUtils.create(
                    user
                )

                NewUser.new {
                    created = now
                }

                NewUser.new {
                    created = yesterday
                }

                val r1 = user.select { user.created eq now }.map { it.toUser() }.first()
                assertEquals(now, r1.created.toJST())

                val rl: MutableList<User> = mutableListOf()
                user.selectAll().orderBy(user.created to SortOrder.DESC).forEach {
                    rl.add(it.toUser())
                }
                assertEquals(now, rl[0].created.toJST())
                assertEquals(yesterday, rl[1].created.toJST())

                val deleteCount1 = user.deleteWhere { user.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = user.deleteWhere { user.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    user
                )
            }
        }
        test("linked_userのCRDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val yesterday = now.minusDays(1)
            val tid1 = "@hogehoge"
            val tname1 = "pikapika"
            val tid2 = "@hugahuga"
            val tname2 = "nyanya"

            transaction {
                SchemaUtils.create(
                    user,
                    linked_user
                )

                val u1 = NewUser.new {
                    created = now
                }

                val u2 = NewUser.new {
                    created = yesterday
                }

                NewLinkedUser(user_id = u1.id, twitter_account_id = tid1, twitter_account_name = tname1)
                NewLinkedUser(user_id = u2.id, twitter_account_id = tid2, twitter_account_name = tname2)

                val r1 = linked_user.select { linked_user.twitter_account_id eq tid1 }.map { it.toLinkedUser() }.first()
                assertEquals(u1.id, r1.user_id)
                assertEquals(tname1, r1.twitter_account_name)

                val rl: MutableList<LinkedUser> = mutableListOf()
                linked_user.selectAll().orderBy(linked_user.twitter_account_id to SortOrder.ASC).forEach {
                    rl.add(it.toLinkedUser())
                }
                assertEquals(u1.id, rl[0].user_id)
                assertEquals(tname1, rl[0].twitter_account_name)
                assertEquals(u2.id, rl[1].user_id)
                assertEquals(tname2, rl[1].twitter_account_name)

                val deleteCount1 = linked_user.deleteWhere { linked_user.twitter_account_id eq tid1 }
                assertEquals(1, deleteCount1)
                val deleteCount2 = linked_user.deleteWhere { linked_user.twitter_account_id eq tid2 }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    user,
                    linked_user
                )
            }
        }
        test("unlinked_userのCRDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val yesterday = now.minusDays(1)
            val tid1 = "@hogehoge"
            val tname1 = "pikapika"
            val tid2 = "@hugahuga"
            val tname2 = "nyanya"

            transaction {
                SchemaUtils.create(
                    user,
                    unlinked_user
                )

                val u1 = NewUser.new {
                    created = now
                }

                val u2 = NewUser.new {
                    created = yesterday
                }

                NewUnlinkedUser(user_id = u1.id, twitter_account_id = tid1, twitter_account_name = tname1)
                NewUnlinkedUser(user_id = u2.id, twitter_account_id = tid2, twitter_account_name = tname2)

                val r1 = unlinked_user.select { unlinked_user.twitter_account_id eq tid1 }.map { it.toUnlinkedUser() }.first()
                assertEquals(u1.id, r1.user_id)
                assertEquals(tname1, r1.twitter_account_name)

                val rl: MutableList<UnlinkedUser> = mutableListOf()
                unlinked_user.selectAll().orderBy(unlinked_user.twitter_account_id to SortOrder.ASC).forEach {
                    rl.add(it.toUnlinkedUser())
                }
                assertEquals(u1.id, rl[0].user_id)
                assertEquals(tname1, rl[0].twitter_account_name)
                assertEquals(u2.id, rl[1].user_id)
                assertEquals(tname2, rl[1].twitter_account_name)

                val deleteCount1 = unlinked_user.deleteWhere { unlinked_user.twitter_account_id eq tid1 }
                assertEquals(1, deleteCount1)
                val deleteCount2 = unlinked_user.deleteWhere { unlinked_user.twitter_account_id eq tid2 }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    user,
                    unlinked_user
                )
            }
        }
    }
})
