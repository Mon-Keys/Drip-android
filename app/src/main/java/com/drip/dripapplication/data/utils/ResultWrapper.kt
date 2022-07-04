package com.drip.dripapplication.data.utils

import java.lang.Exception

sealed class ResultWrapper<out T>{
    data class Success<out T>(val data: T): ResultWrapper<T>()
    data class Error(val exception: Exception): ResultWrapper<Nothing>()
    object Loading: ResultWrapper<Nothing>()

}
