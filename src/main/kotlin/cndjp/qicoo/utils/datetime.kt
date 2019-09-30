package main.kotlin.cndjp.qicoo.utils

import java.text.ParseException
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.Date

fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm:ss"): Date? {
    val sdFormat = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = sdFormat?.let {
        try {
            it.parse(this)
        } catch (e: ParseException){
            null
        }
    }
    return date
}