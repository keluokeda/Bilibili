package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.ke.bilibili.tv.R
import com.ke.biliblli.common.CrashHandler
import kotlinx.coroutines.launch

@Composable
internal fun ConvertRoute(
    convert: suspend () -> Unit
) {

    var error by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        try {
            error = false
            convert()
        } catch (e: Exception) {
            CrashHandler.handler(e)
            error = true
        }

    }

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (error) {
            Button(onClick = {
                scope.launch {
                    try {
                        error = false
                        convert()
                    } catch (e: Exception) {
                        CrashHandler.handler(e)
                        error = true
                    }
                }
            }) {
                Text("出错了，点我重试")
            }
        } else {
            Text(stringResource(R.string.loading))
        }

    }
}