package com.ke.biliblli.common

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object Splash : Screen

    @Serializable
    data class Main(val userId: Long) : Screen

    @Serializable
    data class VideoDetail(val bvid: String, val cid: Long) : Screen

    @Serializable
    data class VideoInfo(val bvid: String) : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data class Comment(val id: Long, val type: Int) : Screen

//    @Serializable
//    data class Convert(val bvid: String) : Screen

    @Serializable
    data class UserDetail(val id: Long, val name: String, val avatar: String, val sign: String) :
        Screen

    @Serializable
    data object UploadApk : Screen

    @Serializable
    data class Search(val defaultKeywords: String = "") : Screen
}