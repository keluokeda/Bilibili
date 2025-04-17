package com.ke.biliblli.common.entity

enum class DanmakuSpeed(val duration: Long, val displayName: String) {
    Fast(3000, "快"),
    Normal(6000, "标准"),
    Slow(9000, "慢")
}