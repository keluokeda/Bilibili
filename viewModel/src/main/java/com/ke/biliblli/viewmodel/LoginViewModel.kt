package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<LoginState, LoginAction, LoginEvent>(LoginState(null, false, "")) {


    init {
        refreshQrUrl()
    }


    private fun checkLogin() {
        viewModelScope.launch {
            if (key.isEmpty()) {
                return@launch
            }
            try {
                val response = bilibiliRepository.checkLogin(key)
                if (response.success) {
                    if (response.data!!.success) {
                        _event.send(LoginEvent.ToSplash)
                    } else {
                        _event.send(LoginEvent.ShowToast(response.data!!.message))
                    }
                } else {
                    _event.send(LoginEvent.ShowToast(response.message))
                }
            } catch (e: Exception) {
                CrashHandler.handler(e)
                _event.send(LoginEvent.ShowToast("网络故障"))
            }
        }
    }

    override fun handleAction(action: LoginAction) {

        when (action) {
            LoginAction.CheckLogin -> {
                checkLogin()
            }

            LoginAction.Refresh -> {
                refreshQrUrl()
            }
        }
    }

    private var key: String = ""

    private fun refreshQrUrl() {
        viewModelScope.launch {
            try {
                _uiState.value = LoginState(null, false, "")
                key = ""

                val response = bilibiliRepository.loginQrCode()

                if (response.success) {
                    val data = response.data!!
                    _uiState.value = LoginState(data.url, false, "")
                    key = data.key
                } else {
                    _uiState.value = LoginState("", false, response.message)
                }


            } catch (e: Exception) {
                CrashHandler.handler(e)
                _uiState.value = LoginState("", false, "错误")
            }
        }
    }
}

data class LoginState(
    val url: String?,
    val loading: Boolean,
    val errorMessage: String
)

sealed interface LoginAction {
    data object Refresh : LoginAction

    data object CheckLogin : LoginAction
}

sealed interface LoginEvent {
    data class ShowToast(val message: String) : LoginEvent

    data object ToSplash : LoginEvent
}