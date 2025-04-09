package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.biliblli.viewmodel.SplashEvent
import com.ke.biliblli.viewmodel.SplashViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.ke.biliblli.viewmodel.SplashAction
import com.ke.biliblli.viewmodel.SplashState

@Composable
internal fun SplashRoute(
    toMain: () -> Unit,
    toLogin: () -> Unit
) {

    val viewModel = hiltViewModel<SplashViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.observeWithLifecycle {
        when (it) {
            SplashEvent.ToLogin -> {
                toLogin()
            }

            SplashEvent.ToMain -> {
                toMain()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            SplashState.Error -> {
                OutlinedButton(onClick = {
                    viewModel.handleAction(SplashAction.Retry)
                }) {
                    Text("出错了，点我重试")
                }
            }

            SplashState.Loading -> {
                Text("加载中")
            }
        }
    }

}