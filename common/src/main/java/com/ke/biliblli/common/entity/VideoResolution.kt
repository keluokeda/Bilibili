package com.ke.biliblli.common.entity

//
//val videoResolutionPairList = listOf<Pair<Int, String>>(
//    6 to "240P 极速",
//    16 to "360P 流畅",
//    32 to "480P 清晰",
//    64 to "720P 高清",
//    74 to "720P60 高帧率",
//    80 to "1080P 高清",
//    112 to "1080P+ 高码率",
//    116 to "1080P60 高帧率",
//    120 to "4K 超清",
//    125 to "HDR 真彩色",
//    126 to "杜比视界",
//    127 to "8K 超高清"
//)

enum class VideoResolution(val code: Int, val displayName: String) {
    P240(6, "240P 极速"),
    P360(16, "360P 流畅"),
    P480(32, "480P 清晰"),
    P720(64, "720P 高清"),
    P720F60(74, "720P60 高帧率"),
    P1080(80, "1080P 高清"),
    P1080P(112, "1080P+ 高码率"),
    P1080F60(116, "1080P60 高帧率"),
    K4(120, "4K 超清"),
    Hdr(125, "HDR 真彩色"),
    Dolby(126, "杜比视界"),
    K8(127, "8K 超高清")
}