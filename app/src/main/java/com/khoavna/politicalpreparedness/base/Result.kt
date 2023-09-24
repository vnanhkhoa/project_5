package com.khoavna.politicalpreparedness.base

sealed class Result<out T: Any> {
    data class SUCCESS<out T: Any>(val data: T) : Result<T>()
    data class ERROR(val message: String?) : Result<Nothing>()

    companion object {
        fun  <T: Any>Result<T>.onSuccess(callBack: (t: T) -> Unit) {
            if (this is SUCCESS) {
                callBack.invoke(this.data)
            }
        }

        fun  <T: Any>Result<T>.onError(callBack: (message: String) -> Unit) {
            if (this is ERROR) {
                message?.let {
                    callBack.invoke(it)
                }
            }
        }
    }
}