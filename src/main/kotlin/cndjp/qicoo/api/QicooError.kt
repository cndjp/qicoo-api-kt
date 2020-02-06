package cndjp.qicoo.api

import cndjp.qicoo.infrastructure.logger.QicooLogger

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
        QicooLogger().logger.error("$reason - $stackTrace")
    }
}
