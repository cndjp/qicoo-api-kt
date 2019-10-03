package utils

enum class RetResult(msg: String) {
    Success("success!!"),
    GeneralFailure("fail!!"),
    NotFoundEntityFailure("not found entity")
}