package cndjp.qicoo.api

import mu.KotlinLogging

enum class QicooError {
    ArrayIndexOutOfBoundsFailure,
    ParseRequestFailure,
    MismatchDataStoreFailure,
    CouldNotCreateEntityFailure,
    NotFoundEntityFailure;
}

fun QicooError.withLog(reason: String = this.name): QicooError {
    var stackTrace = ""
    Thread.currentThread().stackTrace.forEach { stackTrace += it }
    return this.also {
        KotlinLogging.logger {}.error("$reason - $stackTrace")
    }
}
