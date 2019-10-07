package utils

enum class EntityResult {
    Success,
    NotFoundEntityFailure;

    fun returnReason(): String = this.name
}

fun Any?.checkNull(): EntityResult =
    when (this) {
        null -> EntityResult.NotFoundEntityFailure
        else -> EntityResult.Success
    }

fun Double.checkCreate(): EntityResult =
    when (this) {
        0.0 -> EntityResult.NotFoundEntityFailure
        else -> EntityResult.Success
    }

fun Long.checkCreate(): EntityResult =
    when (this) {
        0L -> EntityResult.NotFoundEntityFailure
        else -> EntityResult.Success
    }
