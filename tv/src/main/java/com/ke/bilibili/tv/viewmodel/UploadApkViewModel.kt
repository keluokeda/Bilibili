package com.ke.bilibili.tv.viewmodel

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


private val port = 3000

@HiltViewModel
class UploadApkViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel<UploadApkState, Unit, Unit>(UploadApkState("")) {

    private var fileHttpServer: FileHttpServer? = null

    fun getWifiIpAddress(): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        if (wifiManager == null || !wifiManager.isWifiEnabled) {
            return null // Wi-Fi 未启用
        }

        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress

        // 转换为点分十进制格式
        return String.format(
            "%d.%d.%d.%d",
            (ipAddress and 0xff),
            (ipAddress shr 8 and 0xff),
            (ipAddress shr 16 and 0xff),
            (ipAddress shr 24 and 0xff)
        )
    }

    init {
        viewModelScope.launch {
            try {
                val ip = getWifiIpAddress()!!
                _uiState.update {
                    it.copy(url = "http://${ip}:${port}")
                }
            } catch (e: Exception) {

            }




            withContext(Dispatchers.IO) {
                fileHttpServer = FileHttpServer(context)
                fileHttpServer?.start()
            }


        }
    }

    override fun onCleared() {
        super.onCleared()
        fileHttpServer?.stop()
    }

    override fun handleAction(action: Unit) {

    }
}


data class UploadApkState(
    val url: String
)

class FileHttpServer(
    private val context: Context
) : NanoHTTPD(port) {


    override fun serve(session: IHTTPSession): Response? {
//        var uri = session.uri.replaceFirst("^/", "") // 去除开头的斜杠
//        if (uri.isEmpty()) uri = "index.html" // 默认主页

        if (session.method == Method.GET) {

            val uri = if (session.uri == "/") "index.html" else session.uri.replaceFirst("/", "")
            try {
                // 通过 AssetManager 打开文件
                val assetManager = context.assets
                val inputStream = assetManager.open(uri)

                // 根据文件扩展名确定 MIME 类型
                var mimeType = "text/html"
                if (uri.endsWith(".css")) {
                    mimeType = "text/css"
                } else if (uri.endsWith(".js")) {
                    mimeType = "application/javascript"
                } else if (uri.endsWith(".png")) {
                    mimeType = "image/png"
                }

                // 返回文件流（支持大文件）
                return newChunkedResponse(Response.Status.OK, mimeType, inputStream)
            } catch (e: IOException) {
                // 文件不存在时返回 404
                return newFixedLengthResponse(
                    Response.Status.NOT_FOUND,
                    "text/plain",
                    "File not found"
                )
            }
        } else if (session.method == Method.POST) {
//            return newFixedLengthResponse(
//                Response.Status.NOT_FOUND,
//                "text/plain",
//                "File not found"
//            )

            try {
                // 解析上传的文件
                val files = mutableMapOf<String, String>()
                session.parseBody(files)
                val tmpFilePath = files["file"]!! // 获取临时文件路径
                val uploadedFile = File(tmpFilePath)

                // 将文件移动到应用私有目录（适配 Android 11+）
                val targetDir = context.getExternalFilesDir("apks")!!
                val targetFile = File(targetDir, "update.apk")
                if (targetFile.exists()) {
                    targetFile.delete()
                }

                copyFile(uploadedFile, targetFile)

//                FileUtils.copy(uploadedFile.inputStream(), targetFile.outputStream())
//                FileUtils.copy(File(tmpFilePath), targetFile)
                // 触发安装
                installApk(targetFile)

                return newFixedLengthResponse("Upload successful!")
            } catch (e: java.lang.Exception) {
                return newFixedLengthResponse(
                    Response.Status.INTERNAL_ERROR,
                    "text/plain",
                    "Error: " + e.message
                )
            }
        }

        return newFixedLengthResponse(
            Response.Status.INTERNAL_ERROR,
            "text/plain",
            "Error: "
        )
    }

    private fun installApk(apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(
            FileProvider.getUriForFile(context, context.packageName + ".provider", apkFile),
            "application/vnd.android.package-archive"
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun copyFile(src: File, dest: File): Boolean {
        try {
            FileInputStream(src).use { `in` ->
                FileOutputStream(dest).use { out ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while ((`in`.read(buffer).also { length = it }) > 0) {
                        out.write(buffer, 0, length)
                    }
                    return true
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }
}