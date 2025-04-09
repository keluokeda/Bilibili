package com.ke.biliblli.common

import okio.ByteString.Companion.encodeUtf8
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.TreeMap
import java.util.stream.Collectors


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

