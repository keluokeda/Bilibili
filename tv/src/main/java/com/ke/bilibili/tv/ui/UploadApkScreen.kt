package com.ke.bilibili.tv.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import com.ke.bilibili.tv.viewmodel.UploadApkViewModel
import com.king.zxing.util.CodeUtils

private val qrSize = 480.dp

@Composable
internal fun UploadApkRoute() {

    val viewModel = hiltViewModel<UploadApkViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap =
                CodeUtils.createQRCode(uiState.url, qrSize.value.toInt())
                    .asImageBitmap(),
            contentDescription = null, contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(80.dp))
        ListItem(
            selected = false, onClick = {}, headlineContent = {
                Text("请使用手机浏览器扫描二维码上传安装包文件")
            }, supportingContent = {
                Text("文件上传后会弹出安装提示，请点击允许")
            }
        )
    }
}