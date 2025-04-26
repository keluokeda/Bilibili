package com.ke.biliblli.repository

import android.content.Context
import androidx.core.content.edit
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.entity.DanmakuDensity
import com.ke.biliblli.common.entity.DanmakuFontSize
import com.ke.biliblli.common.entity.DanmakuPosition
import com.ke.biliblli.common.entity.DanmakuSpeed
import com.ke.biliblli.common.entity.VideoResolution
import com.ke.biliblli.common.entity.WbiParams
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BilibiliStorageImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : BilibiliStorage {
    private val sharedPreferences = context.getSharedPreferences("bilibili", Context.MODE_PRIVATE)

//    override var uid: Long
//        get() = sharedPreferences.getLong("uid", -1)
//        set(value) {
//            sharedPreferences.edit(commit = true) {
//                putLong("uid", value)
//            }
//        }
//
//    override var eid: String?
//        get() = sharedPreferences.getString("eid", null)
//        set(value) {
//            sharedPreferences.edit(commit = true) {
//                putString("eid", value)
//            }
//        }

    override var directPlay: Boolean
        get() = sharedPreferences.getBoolean("directPlay", false)
        set(value) {
            sharedPreferences.edit(commit = true) {
                putBoolean("directPlay", value)
            }
        }

    override var playerViewShowMiniProgressBar: Boolean
        get() = sharedPreferences.getBoolean("playerViewShowMiniProgressBar", true)
        set(value) {
            sharedPreferences.edit(commit = true) {
                putBoolean("playerViewShowMiniProgressBar", value)
            }
        }

    override var mainDefaultTab: Int
        get() = sharedPreferences.getInt("mainDefaultTab", 1)
        set(value) {
            sharedPreferences.edit(commit = true) {
                putInt("mainDefaultTab", value)
            }
        }

    override var danmakuVersion: Int
        get() = sharedPreferences.getInt("danmakuVersion", 0)
        set(value) {
            sharedPreferences.edit(commit = true) {
                putInt("danmakuVersion", value)
            }
        }

    override var danmakuColorful: Boolean
        get() = sharedPreferences.getBoolean("danmakuColorful", false)
        set(value) {
            sharedPreferences.edit(commit = true) {
                putBoolean("danmakuColorful", value)
            }
        }

    override var wbiParams: WbiParams?
        get() {
            val image = sharedPreferences.getString("wbi_image", null) ?: return null

            val sub = sharedPreferences.getString("wbi_sub", null) ?: return null

            val time = sharedPreferences.getLong("wbi_time", 0)

            return WbiParams(image, sub, time)
        }
        set(value) {
            sharedPreferences.edit(commit = true) {
                putString("wbi_image", value?.image)
                putString("wbi_sub", value?.sub)
                putLong("wbi_time", value?.time ?: 0)
            }
        }
    override var danmakuEnable: Boolean
        get() = sharedPreferences.getBoolean("danmakuEnable", false)
        set(value) {
            sharedPreferences.edit(commit = true) {
                putBoolean("danmakuEnable", value)
            }
        }
    override var danmakuSpeed: DanmakuSpeed
        get() {
            val duration = sharedPreferences.getLong("danmakuSpeed", DanmakuSpeed.Normal.duration)

            return DanmakuSpeed.entries.firstOrNull {
                it.duration == duration
            } ?: DanmakuSpeed.Normal
        }
        set(value) {
            sharedPreferences.edit(commit = true) {
                putLong("danmakuSpeed", value.duration)
            }
        }
    override var danmakuDensity: DanmakuDensity
        get() {
            val density = sharedPreferences.getFloat("danmakuDensity", 1f)

            return DanmakuDensity.entries.firstOrNull {
                it.density == density
            } ?: DanmakuDensity.Normal
        }
        set(value) {
            sharedPreferences.edit(commit = true) {
                putFloat("danmakuDensity", value.density)
            }
        }
    override var danmakuFontSize: DanmakuFontSize
        get() {
            val ratio = sharedPreferences.getFloat("danmakuFontSize", 1f)
            return DanmakuFontSize.entries.firstOrNull {
                it.ratio == ratio
            } ?: DanmakuFontSize.Medium
        }
        set(value) {
            sharedPreferences.edit(commit = true) {
                putFloat("danmakuFontSize", value.ratio)
            }
        }
    override var danmakuPosition: DanmakuPosition
        get() {
            val code = sharedPreferences.getInt("danmakuPosition", DanmakuPosition.Full.code)
            return DanmakuPosition.entries.firstOrNull {
                it.code == code
            } ?: DanmakuPosition.Full
        }
        set(value) {
            sharedPreferences.edit(commit = true) {
                putInt("danmakuPosition", value.code)
            }
        }
    override var videoResolution: VideoResolution
        get() {
            val code = sharedPreferences.getInt("videoResolution", VideoResolution.P1080.code)

            return VideoResolution.entries.firstOrNull {
                it.code == code
            } ?: VideoResolution.P1080
        }
        set(value) {
            sharedPreferences.edit(commit = true) {
                putInt("videoResolution", value.code)
            }
        }
}