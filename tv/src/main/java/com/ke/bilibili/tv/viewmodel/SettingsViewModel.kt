package com.ke.bilibili.tv.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.entity.DanmakuDensity
import com.ke.biliblli.common.entity.DanmakuFontSize
import com.ke.biliblli.common.entity.DanmakuPosition
import com.ke.biliblli.common.entity.DanmakuSpeed
import com.ke.biliblli.common.entity.VideoResolution
import com.ke.biliblli.common.event.MainTab
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val bilibiliStorage: BilibiliStorage,
    private val bilibiliRepository: BilibiliRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<SettingsState, SettingsAction, SettingsEvent>(
    SettingsState(
        VideoResolution.P1080,
        DanmakuSpeed.Normal,
        DanmakuDensity.Normal,
        DanmakuPosition.Full,
        DanmakuFontSize.Medium,
        false,
        false,
        playerViewShowMiniProgressBar = false,
        directPlay = false,
        danmakuVersion = 1
    )
) {

    init {
        refresh()
    }


    private fun refresh() {
        _uiState.value = SettingsState(
            bilibiliStorage.videoResolution,
            bilibiliStorage.danmakuSpeed,
            bilibiliStorage.danmakuDensity,
            bilibiliStorage.danmakuPosition,
            bilibiliStorage.danmakuFontSize,
            bilibiliStorage.danmakuEnable,
            bilibiliStorage.danmakuColorful,
            defaultTab = MainTab.entries.first { it.index == bilibiliStorage.mainDefaultTab },
            cacheSize = context.cacheSize(),
            playerViewShowMiniProgressBar = bilibiliStorage.playerViewShowMiniProgressBar,
            directPlay = bilibiliStorage.directPlay,
            danmakuVersion = bilibiliStorage.danmakuVersion
        )
    }

    override fun handleAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.UpdateDanmakuDensity -> {
                bilibiliStorage.danmakuDensity = action.value
            }

            is SettingsAction.UpdateDanmakuFontSize -> {
                bilibiliStorage.danmakuFontSize = action.value
            }

            is SettingsAction.UpdateDanmakuPosition -> {
                bilibiliStorage.danmakuPosition = action.position
            }

            is SettingsAction.UpdateDanmakuSpeed -> {
                bilibiliStorage.danmakuSpeed = action.value
            }

            is SettingsAction.UpdateVideoResolution -> {
                bilibiliStorage.videoResolution = action.value
            }

            is SettingsAction.SetDanmakuEnable -> {
                bilibiliStorage.danmakuEnable = action.value
            }

            is SettingsAction.SetDanmakuColorful -> {
                bilibiliStorage.danmakuColorful = action.value
            }

            SettingsAction.Logout -> {
                viewModelScope.launch {
                    bilibiliRepository.logout()
                    _event.send(SettingsEvent.ToSplash)
                }
//                viewModelScope.launch {
//                    _uiState.update {
//                        it.copy(loading = true)
//                    }
//
//                    try {
//                        val response = bilibiliRepository.logout()
//
//                        if (response.success) {
//                            _event.send(SettingsEvent.ToSplash)
//                        } else {
//                            _event.send(SettingsEvent.ShowMessage(response.message))
//                        }
//
//                    } catch (e: Exception) {
//                        _event.send(SettingsEvent.ShowMessage("网络故障"))
//
//                        CrashHandler.handler(e)
//                    }
//                    _uiState.update {
//                        it.copy(loading = false)
//                    }
//
//                }
            }

            is SettingsAction.SetDefaultTab -> {
                bilibiliStorage.mainDefaultTab = action.value.index
            }

            SettingsAction.ClearCache -> {
//                context.cacheDir?.delete()
//                context.externalCacheDir?.delete()
                clearCache(context)
            }

            is SettingsAction.SetPlayerViewShowMiniProgressBar -> {
                bilibiliStorage.playerViewShowMiniProgressBar = action.value
            }

            is SettingsAction.SetDirectPlay -> {
                bilibiliStorage.directPlay = action.value
            }

            is SettingsAction.SetDanmakuVersion -> {
                bilibiliStorage.danmakuVersion = action.value
            }
        }

        refresh()
    }
}

fun clearCache(context: Context): Boolean {
    return try {
        // 清除内部缓存
        deleteRecursive(context.cacheDir)
        // 清除外部缓存（如果有）
        context.externalCacheDir?.let { deleteRecursive(it) }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * 递归删除目录/文件
 */
private fun deleteRecursive(file: File?) {
    if (file == null || !file.exists()) return

    if (file.isDirectory) {
        file.listFiles()?.forEach { deleteRecursive(it) }
    }
    file.delete()
}

data class SettingsState(
    val videoResolution: VideoResolution,
    val danmakuSpeed: DanmakuSpeed,
    val danmakuDensity: DanmakuDensity,
    val danmakuPosition: DanmakuPosition,
    val danmakuFontSize: DanmakuFontSize,
    val danmakuEnable: Boolean,
    val danmakuColorful: Boolean,
    val loading: Boolean = false,
    val defaultTab: MainTab = MainTab.Recommend,
    val cacheSize: String = "0MB",
    val playerViewShowMiniProgressBar: Boolean,
    val directPlay: Boolean,
    val danmakuVersion: Int,

    )

sealed interface SettingsAction {
    data class UpdateVideoResolution(val value: VideoResolution) : SettingsAction

    data class UpdateDanmakuSpeed(val value: DanmakuSpeed) : SettingsAction

    data class UpdateDanmakuDensity(val value: DanmakuDensity) : SettingsAction

    data class UpdateDanmakuPosition(val position: DanmakuPosition) : SettingsAction

    data class UpdateDanmakuFontSize(val value: DanmakuFontSize) : SettingsAction

    data class SetDanmakuEnable(val value: Boolean) : SettingsAction

    data class SetDanmakuColorful(val value: Boolean) : SettingsAction

    data object Logout : SettingsAction


    data class SetDefaultTab(val value: MainTab) : SettingsAction

    data object ClearCache : SettingsAction


    data class SetPlayerViewShowMiniProgressBar(val value: Boolean) : SettingsAction

    data class SetDirectPlay(val value: Boolean) : SettingsAction

    data class SetDanmakuVersion(val value: Int) : SettingsAction
}

sealed interface SettingsEvent {
    data object ToSplash : SettingsEvent

    data class ShowMessage(val message: String) : SettingsEvent
}

fun Context.cacheSize(): String {
    return formatCacheSize(getCacheSize(this))
}

/**
 * 获取应用缓存大小（单位：字节）
 */
private fun getCacheSize(context: Context): Long {
    // 内部缓存目录（/data/data/包名/cache）
    val internalCacheDir = context.cacheDir
    // 外部缓存目录（Android 11+ 后已废弃，但部分应用仍可能使用）
    val externalCacheDir = context.externalCacheDir

    // 计算总缓存大小
    return calculateSize(internalCacheDir) + calculateSize(externalCacheDir)
}

/**
 * 递归计算目录大小
 */
private fun calculateSize(file: File?): Long {
    if (file == null || !file.exists()) return 0

    return if (file.isDirectory) {
        file.listFiles()?.sumOf { calculateSize(it) } ?: 0
    } else {
        file.length()
    }
}

/**
 * 格式化缓存大小（例如：1.23 MB）
 */
private fun formatCacheSize(sizeBytes: Long): String {
    return when {
        sizeBytes >= 1_000_000_000 -> "%.2f GB".format(sizeBytes / 1e9)
        sizeBytes >= 1_000_000 -> "%.2f MB".format(sizeBytes / 1e6)
        sizeBytes >= 1_000 -> "%.2f KB".format(sizeBytes / 1e3)
        else -> "$sizeBytes B"
    }
}