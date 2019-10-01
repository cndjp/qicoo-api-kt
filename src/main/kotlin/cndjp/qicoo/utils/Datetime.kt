package main.kotlin.cndjp.qicoo.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun String.toDatetime(): DateTime {
    return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC().parseDateTime(this)
}