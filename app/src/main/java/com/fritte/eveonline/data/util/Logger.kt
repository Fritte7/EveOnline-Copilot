package com.fritte.eveonline.data.util

interface Logger {
    fun i(tag: String, msg: String)
    fun d(tag: String, msg: String)
    fun v(tag: String, msg: String)
    fun w(tag: String, msg: String)
    fun e(tag: String, msg: String, tr: Throwable? = null)
}

object NoopLogger : Logger {
    override fun i(tag: String, msg: String) = Unit
    override fun d(tag: String, msg: String) = Unit
    override fun v(tag: String, msg: String) = Unit
    override fun w(tag: String, msg: String) = Unit
    override fun e(tag: String, msg: String, tr: Throwable?) = Unit
}

object Logg {
    var logger: Logger = NoopLogger

    fun i(tag: String, msg: String) =
        logger.i(tag, msg)

    fun d(tag: String, msg: String) =
        logger.d(tag, msg)

    fun v(tag: String, msg: String) =
        logger.v(tag, msg)

    fun w(tag: String, msg: String) =
        logger.w(tag, msg)

    fun e(tag: String, msg: String, tr: Throwable? = null) =
        logger.e(tag, msg, tr)
}