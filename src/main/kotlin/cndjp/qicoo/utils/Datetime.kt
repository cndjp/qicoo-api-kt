package utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

fun getNowDateTimeJst(): DateTime = DateTime.now(DateTimeZone.forID("Asia/Tokyo"))


fun String.toDateTimeUtc(): DateTime {
    return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC().parseDateTime(this)
}

fun String.toDateTimeJst(): DateTime {
    return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZone(DateTimeZone.forID("Asia/Tokyo")).parseDateTime(this)
}

fun String.toDateTimeLocal(): DateTime {
    val localDateTime = LocalDateTime.parse(this, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
    return localDateTime.toDateTime()
}

fun DateTime.toJST(): DateTime = this.toDateTime(DateTimeZone.forID("Asia/Tokyo"))

fun DateTime.toFomatString(): String = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(this)