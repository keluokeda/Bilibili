package com.ke.biliblli.common.entity

enum class DanmakuDensity(val density: Float, val displayName: String, val code: Int) {
    Normal(1.0f, "正常", 1),
    Half(0.5f, "一半", 2),
    Less(0.3f, "较少", 3)
}