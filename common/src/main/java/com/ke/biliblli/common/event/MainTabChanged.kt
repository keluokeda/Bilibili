package com.ke.biliblli.common.event

data class MainTabChanged(
    val tab: MainTab
)

enum class MainTab(val displayName: String, val index: Int, val canSetDefault: Boolean = false) {

    Search("搜索", 0),
    Recommend("推荐", 1, true),
    Dynamic("动态", 2, true),
    Fav("我的收藏", 3, true),
    LaterWatch("稍后再看", 4, true),
    History("历史记录", 5),
    Settings("设置", 6),
}
