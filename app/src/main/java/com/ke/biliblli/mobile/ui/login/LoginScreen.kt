package com.ke.biliblli.mobile.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.biliblli.mobile.observeWithLifecycle
import com.ke.biliblli.mobile.ui.theme.BilibiliTheme
import com.ke.biliblli.viewmodel.LoginAction
import com.ke.biliblli.viewmodel.LoginEvent
import com.ke.biliblli.viewmodel.LoginState
import com.ke.biliblli.viewmodel.LoginViewModel
import com.lightspark.composeqr.QrCodeView

@Composable
internal fun LoginRoute(
    toMain: () -> Unit
) {
    val viewModel = hiltViewModel<LoginViewModel>()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    viewModel.event.observeWithLifecycle {
        when (it) {
            is LoginEvent.ShowToast -> {
                Toast.makeText(
                    context, it.message, Toast.LENGTH_SHORT
                ).show()
            }

            LoginEvent.ToSplash -> {
                toMain()
            }
        }
    }

    LoginScreen(uiState.value, {
        viewModel.handleAction(LoginAction.Refresh)
    }, {
        viewModel.handleAction(LoginAction.CheckLogin)
    })
}

@Composable
@PreviewLightDark
private fun LoginScreenErrorPreview() {
    BilibiliTheme {
        LoginScreen(uiState = LoginState("", false, "网络故障"))
    }
}

@Composable
@PreviewLightDark
private fun LoginScreenLoadingPreview() {
    BilibiliTheme {
        LoginScreen(uiState = LoginState(null, false, "网络故障"))
    }
}

@Composable
@PreviewLightDark
private fun LoginScreenIdlePreview() {
    BilibiliTheme {
        LoginScreen(
            uiState = LoginState(
                "https://github.com/lightsparkdev/compose-qr-code",
                false,
                "网络故障"
            )
        )
    }
}

@Composable
@PreviewLightDark
private fun LoginScreenIdleLoadingPreview() {
    BilibiliTheme {
        LoginScreen(
            uiState = LoginState(
                "https://github.com/lightsparkdev/compose-qr-code",
                true,
                "网络故障"
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    uiState: LoginState,
    refresh: () -> Unit = {},
    checkLogin: () -> Unit = {}
) {

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("扫码登录")
            }
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.url == null) {
                    CircularProgressIndicator()
                } else if (uiState.url!!.isEmpty()) {
                    Text(uiState.errorMessage)
                } else {
                    QrCodeView(uiState.url!!, modifier = Modifier.fillMaxSize())
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = checkLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading && ((uiState.url?.length ?: 0) > 0)
            ) {
                Text("我已扫码")
            }




            if ((uiState.url?.length ?: 0) > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = refresh,
                    enabled = !uiState.loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("刷新二维码")
                }
            }

            if (uiState.loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}