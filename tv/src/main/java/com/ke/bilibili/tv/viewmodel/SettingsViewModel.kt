package com.ke.bilibili.tv.viewmodel

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val bilibiliStorage: BilibiliStorage,
    private val bilibiliRepository: BilibiliRepository,
) : BaseViewModel<SettingsState, SettingsAction, SettingsEvent>(
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
            bilibiliStorage.danmakuColorful,
            defaultTab = MainTab.entries.first { it.index == bilibiliStorage.mainDefaultTab }
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
    val danmakuColorful: Boolean,
    val loading: Boolean = false,
    val defaultTab: MainTab = MainTab.Recommend
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

}

sealed interface SettingsEvent {
    data object ToSplash : SettingsEvent

    data class ShowMessage(val message: String) : SettingsEvent
}