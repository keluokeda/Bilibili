package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<SplashState, SplashAction, SplashEvent>(SplashState.Loading) {
    override fun handleAction(action: SplashAction) {
        when (action) {
            SplashAction.Retry -> {
                checkLogin()
            }
        }
    }

    init {
        checkLogin()
    }

    private fun checkLogin() {
        _uiState.value = SplashState.Loading

        viewModelScope.launch {
            try {

                bilibiliRepository.initBuvid()
                val data = bilibiliRepository.loginInfo().data
                if (data!!.isLogin) {
                    _event.send(SplashEvent.ToMain(data.mid!!))
                } else {
                    _event.send(SplashEvent.ToLogin)
                }
            } catch (e: Exception) {
//                e.printStackTrace()
                CrashHandler.handler(e)
                _uiState.value = SplashState.Error
            }
        }
    }

}

sealed interface SplashState {
    data object Loading : SplashState
    data object Error : SplashState
}

sealed interface SplashAction {
    data object Retry : SplashAction
}

sealed interface SplashEvent {
    data class ToMain(val userId: Long) : SplashEvent
    data object ToLogin : SplashEvent
}