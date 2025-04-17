package com.ke.biliblli.common.entity

enum class DanmakuDensity(val density: Float, val displayName: String) {
    Normal(1.0f, "正常"),
    Half(0.5f, "一半"),
    Less(0.3f, "较少")
}