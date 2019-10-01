package main.kotlin.cndjp.qicoo.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

fun String.toDatetimeUtc(): DateTime {
    return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC().parseDateTime(this)
}

fun String.toDatetimeJst(): DateTime {
    return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.forID("Asia/Tokyo")).parseDateTime(this)
}