package com.ke.biliblli.common


fun Long.format(): String {
    return when (this) {

        in 10000..Long.MAX_VALUE -> {
            (this / 10000).toString() + "万"
        }

        else -> {
            toString()
        }
    }
}

fun Long.duration(): String {
    val second = this % 60
    val minute = this / 60

    return "${minute}:$second"
}

object CrashHandler {
    var handler: (Exception) -> Unit = {}
}

