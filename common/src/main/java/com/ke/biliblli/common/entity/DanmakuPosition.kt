package com.ke.biliblli.common.entity

enum class DanmakuPosition(val code: Int, val displayName: String) {
    Full(1, "全屏"),
    Top2(2, "上半部分"),

    //    Bottom2(2, "下半部分"),
    Top3(3, "三分之一上"),

    //    Center3(5, "三分之一中间"),
//    Bottom3(6, "三分之一下"),
    Top4(4, "顶部四分之一"),
    Top5(5, "顶部五分之一"),
    Top6(6, "顶部六分之一")
}