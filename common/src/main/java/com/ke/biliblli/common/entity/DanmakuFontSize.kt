package com.ke.biliblli.common.entity

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class DanmakuFontSize(val ratio: Float, val displayName: String, val textSize: TextUnit) {
    Smaller(0.5f, "小小", 20.sp),
    Small(0.8f, "小", 25.sp),
    Medium(1.0f, "标准", 30.sp),
    Large(1.2f, "大", 35.sp)
}