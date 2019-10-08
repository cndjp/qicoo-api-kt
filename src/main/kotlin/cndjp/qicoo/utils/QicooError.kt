package cndjp.qicoo.utils

import mu.KotlinLogging

data class QicooError(
    val reason: QicooErrorReason
)

enum class QicooErrorReason {
    InvalidConvertFailure,
    MismatchDataStoreFailure,
    CannotCreateEntityFailure,
    NotFoundEntityFailure;
}

fun QicooErrorReason.withLog(): QicooErrorReason =
    this.also {
        KotlinLogging.logger{}.error("error: ${it.name}")
    }
