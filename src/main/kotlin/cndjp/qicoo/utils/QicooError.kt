package cndjp.qicoo.utils

import mu.KotlinLogging

data class QicooError(
    val reason: QicooErrorReason
)

enum class QicooErrorReason {
    EmptyPagenationFailure,
    ArrayIndexOutOfBoundsFailure,
    InvalidConvertFailure,
    MismatchDataStoreFailure,
    CannotCreateEntityFailure,
    NotFoundEntityFailure;
}

fun QicooErrorReason.withLog(reason: String = this.name): QicooErrorReason =
    this.also {
        KotlinLogging.logger {}.error("error: $reason")
    }
