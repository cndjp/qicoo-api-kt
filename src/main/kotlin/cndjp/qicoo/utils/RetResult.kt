package utils

enum class RetResult {
    Success,
    GeneralFailure,
    NotFoundEntityFailure;

    fun returnReason(): String = this.name
}