package com.malomnogo.exchangerates.data


data class Result<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T?): Result<T> =
            Result(Status.SUCCESS, data, null)


        fun <T> error(message: String?): Result<T> =
            Result(Status.ERROR, null, message ?: "Error")

    }

    override fun toString(): String =
        "Result(status=$status, data=$data, message=$message)"

}