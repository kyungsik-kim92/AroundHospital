package com.example.presenter.base

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

open class BaseCoroutineScope : CoroutineScope {
    private var job: Job = SupervisorJob().apply {
        invokeOnCompletion { cause -> handleJobCompletion(cause) }
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    override val coroutineContext: CoroutineContext
        get() = job + errorHandler

    open fun handleJobCompletion(cause: Throwable?) {

    }

    open fun handleException(exception: Throwable) {
        exception.printStackTrace()
    }
}