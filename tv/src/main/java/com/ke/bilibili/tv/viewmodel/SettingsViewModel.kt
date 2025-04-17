package com.ke.bilibili.tv.viewmodel

import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.entity.DanmakuDensity
import com.ke.biliblli.common.entity.DanmakuFontSize
import com.ke.biliblli.common.entity.DanmakuPosition
import com.ke.biliblli.common.entity.DanmakuSpeed
import com.ke.biliblli.common.entity.VideoResolution
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val bilibiliStorage: BilibiliStorage
) : BaseViewModel<SettingsState, SettingsAction, Unit>(
    SettingsState(
        VideoResolution.P1080, DanmakuSpeed.Normal, DanmakuDensity.Normal,
        DanmakuPosition.Full, DanmakuFontSize.Medium, false, false
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
            bilibiliStorage.danmakuColorful
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
        }

        refresh()
    }
}

data class SettingsState(
    val videoResolution: VideoResolution,
    val danmakuSpeed: DanmakuSpeed,
    val danmakuDensity: DanmakuDensity,
    val danmakuPosition: DanmakuPosition,
    val danmakuFontSize: DanmakuFontSize,
    val danmakuEnable: Boolean,
    val danmakuColorful: Boolean
)

sealed interface SettingsAction {
    data class UpdateVideoResolution(val value: VideoResolution) : SettingsAction

    data class UpdateDanmakuSpeed(val value: DanmakuSpeed) : SettingsAction

    data class UpdateDanmakuDensity(val value: DanmakuDensity) : SettingsAction

    data class UpdateDanmakuPosition(val position: DanmakuPosition) : SettingsAction

    data class UpdateDanmakuFontSize(val value: DanmakuFontSize) : SettingsAction

    data class SetDanmakuEnable(val value: Boolean) : SettingsAction

    data class SetDanmakuColorful(val value: Boolean) : SettingsAction


}