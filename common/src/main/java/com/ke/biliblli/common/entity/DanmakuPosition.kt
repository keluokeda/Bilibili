package com.ke.biliblli.common.entity

enum class DanmakuPosition(val code: Int, val displayName: String) {
    Full(0, "全屏"),
    Top2(1, "上半部分"),

    //    Bottom2(2, "下半部分"),
    Top3(4, "三分之一上"),

    //    Center3(5, "三分之一中间"),
//    Bottom3(6, "三分之一下"),
    Top4(7, "顶部四分之一"),
    Top5(8, "顶部五分之一"),
    Top6(9, "顶部六分之一")
}