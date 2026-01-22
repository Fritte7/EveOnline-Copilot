package com.fritte.eveonline.data.util

import android.util.Log

class AndroidLogger(
    private val enabled: Boolean
) : Logger {

    override fun i(tag: String, msg: String) {
        if (enabled) Log.i(tag, msg)
    }

    override fun d(tag: String, msg: String) {
        if (enabled) Log.d(tag, msg)
    }

    override fun v(tag: String, msg: String) {
        if (enabled) Log.v(tag, msg)
    }

    override fun w(tag: String, msg: String) {
        if (enabled) Log.w(tag, msg)
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        if (enabled) Log.e(tag, msg, tr)
    }
}