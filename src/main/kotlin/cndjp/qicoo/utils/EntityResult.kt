package cndjp.qicoo.utils

data class QicooError(
    val reason: QicooErrorReason
)

enum class QicooErrorReason {
    MismatchDataStoreFailure,
    CannotCreateEntityFailure,
    NotFoundEntityFailure;
}
