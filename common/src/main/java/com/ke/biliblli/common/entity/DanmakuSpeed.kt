package com.ke.biliblli.common.entity

enum class DanmakuSpeed(val duration: Long, val displayName: String) {
    Fast(5000, "快"),
    Normal(10000, "标准"),
    Slow(15000, "慢")
}