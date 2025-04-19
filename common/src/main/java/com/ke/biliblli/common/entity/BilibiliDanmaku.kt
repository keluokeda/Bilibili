package com.ke.biliblli.common.entity


data class BilibiliDanmaku(
    val id: Long,
    val progress: Int,
    val mode: Int,
    val fontSize: Int,
    val color: Int,
    val content: String
) {
    fun rgb(): Triple<Int, Int, Int> {
        val rgb = color
        // 提取 RGB 分量（假设 rgb 格式为 0x00RRGGBB）
        val red = (rgb shr 16) and 0xFF
        val green = (rgb shr 8) and 0xFF
        val blue = rgb and 0xFF

        // 格式化为 #RRGGBB
        return Triple(red, green, blue)
    }
}


