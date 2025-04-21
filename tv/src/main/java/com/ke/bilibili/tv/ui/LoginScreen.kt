package com.ke.bilibili.tv.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.ke.bilibili.tv.R
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.bilibili.tv.ui.theme.BilibiliTheme
import com.ke.biliblli.viewmodel.LoginAction
import com.ke.biliblli.viewmodel.LoginEvent
import com.ke.biliblli.viewmodel.LoginState
import com.ke.biliblli.viewmodel.LoginViewModel
import com.king.zxing.util.CodeUtils

@Composable
internal fun LoginRoute(
    toSplash: () -> Unit
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    viewModel.event.observeWithLifecycle {
        when (it) {
            is LoginEvent.ShowToast -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            LoginEvent.ToSplash -> {
                toSplash()
            }
        }
    }

    LoginScreen(state, refresh = {
        viewModel.handleAction(LoginAction.Refresh)
    }, login = {
        viewModel.handleAction(LoginAction.CheckLogin)
    })
}

private val qrSize = 640.dp
private val buttonWidth = 150.dp

@Composable
private fun LoginScreen(
    state: LoginState,
    refresh: () -> Unit = {},
    login: () -> Unit = {}
) {


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Row {
            Box(modifier = Modifier.size(qrSize), contentAlignment = Alignment.Center) {
                if (state.url == null) {
                    Text(stringResource(R.string.loading))
                } else if (state.url?.isEmpty() == true) {
                    Text(state.errorMessage)
                } else {
                    Image(
                        bitmap =
                            CodeUtils.createQRCode(state.url!!, qrSize.value.toInt())
                                .asImageBitmap(),
                        contentDescription = null, contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.width(32.dp))

            val focusRequester = remember {
                FocusRequester()
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }


            Column(modifier = Modifier.height(qrSize), verticalArrangement = Arrangement.Center) {
                Button(
                    onClick = login,
                    enabled = !state.loading && state.url?.isNotEmpty() == true,
                    modifier = Modifier.focusRequester(focusRequester)
                ) {
                    Text(
                        "我已扫码", textAlign = TextAlign.Center,
                        modifier = Modifier.width(buttonWidth),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(

                    onClick = refresh,
                    enabled = !state.loading && state.url?.isNotEmpty() == true
                ) {
                    Text(
                        "刷新二维码",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(buttonWidth),
                    )
                }
            }
        }
    }
}

@Composable
@Preview(device = Devices.TV_1080p, showBackground = true)
private fun LoginScreenIdlePreview() {
    BilibiliTheme {
        LoginScreen(LoginState("baidu.com", false, "")) { }
    }
}

@Composable
@Preview(device = Devices.TV_1080p, showBackground = true)
private fun LoginScreenLoadingPreview() {
    BilibiliTheme {
        LoginScreen(LoginState(null, false, "")) { }
    }
}

@Composable
@Preview(device = Devices.TV_1080p, showBackground = true)
private fun LoginScreenErrorPreview() {
    BilibiliTheme {
        LoginScreen(LoginState("", false, "网络故障")) { }
    }
}