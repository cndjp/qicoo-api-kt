package cndjp.qicoo.api

import mu.KotlinLogging

data class QicooError(
    val reason: QicooErrorReason
)

enum class QicooErrorReason {
    ArrayIndexOutOfBoundsFailure,
    ParseRequestFailure,
    MismatchDataStoreFailure,
    CouldNotCreateEntityFailure,
    NotFoundEntityFailure;
}

fun QicooErrorReason.withLog(reason: String = this.name): QicooErrorReason {
    var stackTrace = ""
    Thread.currentThread().stackTrace.forEach { stackTrace += it }
    return this.also {
        KotlinLogging.logger {}.error("$reason - $stackTrace")
    }
}
