package domain.dao.question_aggr

import org.joda.time.DateTime
import utils.toDateTimeJstgForMySQL
import java.util.*

class QuestionAggr(f1: ByteArray, f2: String, f3: String, f4: String, f5: String, f6: String, f7: String) {
    val question_id: UUID
    val event_name: String
    val program_name: String
    val display_name: String
    val comment: String
    val created: DateTime
    val updated: DateTime

    init {
        question_id = UUID.nameUUIDFromBytes(f1)
        event_name = f2
        program_name = f3
        display_name = f4
        comment = f5
        created = f6.toDateTimeJstgForMySQL()
        updated = f7.toDateTimeJstgForMySQL()
    }
}