package com.ke.biliblli.mobile.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.biliblli.mobile.observeWithLifecycle
import com.ke.biliblli.viewmodel.SplashAction
import com.ke.biliblli.viewmodel.SplashEvent
import com.ke.biliblli.viewmodel.SplashState
import com.ke.biliblli.viewmodel.SplashViewModel


@Composable
internal fun SplashRoute(
    toMain: () -> Unit, toLogin: () -> Unit
) {

    val viewModel = hiltViewModel<SplashViewModel>()

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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SplashScreen(uiState) {
        viewModel.handleAction(SplashAction.Retry)
    }
}

@Composable
private fun SplashScreen(uiState: SplashState, retry: () -> Unit) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {

            when (uiState) {
                SplashState.Error -> {
                    Button(retry) {
                        Text("出错了，点我重试")
                    }
                }

                SplashState.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }

}