package com.ke.biliblli.repository

import com.ke.biliblli.api.BilibiliApi
import com.ke.biliblli.api.response.BaseResponse
import com.ke.biliblli.api.response.CommentResponse
import com.ke.biliblli.api.response.HomeRecommendListResponse
import com.ke.biliblli.api.response.LoginInfoResponse
import com.ke.biliblli.api.response.PollQrcodeResponse
import com.ke.biliblli.api.response.QrCodeResponse
import com.ke.biliblli.api.response.VideoDetailResponse
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.api.response.VideoUrlResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.entity.WbiParams
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.TreeMap
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BilibiliRepositoryImpl @Inject constructor(
    private val bilibiliApi: BilibiliApi,
    private val bilibiliStorage: BilibiliStorage
) :
    BilibiliRepository {

    override suspend fun comments(
        index: Int,
        oid: Long,
        type: Int,
        sort: Int
    ): BaseResponse<CommentResponse> {
        return bilibiliApi.comments(oid, index, type, sort)
    }

    override suspend fun videoInfo(bvid: String): BaseResponse<VideoInfoResponse> {
        var current = bilibiliStorage.wbiParams

        val wbiParams = if (current?.canUse() != true) {
            createAndSaveWbiParams()
        } else {
            current
        }


        val now = System.currentTimeMillis() / 1000
        val treeMap = TreeMap<String, Any>()
        treeMap.put("bvid", bvid)

        val sign = WbiUtil.enc(treeMap, wbiParams.image, wbiParams.sub)

        return bilibiliApi.videoInfo(bvid, now, sign!!)
    }

    override suspend fun loginIngo(): BaseResponse<LoginInfoResponse> {
        val loginInfo = bilibiliApi.loginInfo()
        val params = loginInfo.data!!
        bilibiliStorage.wbiParams = WbiParams(
            params.json.image.substring(params.json.image.lastIndexOf("/")).split(".")[0],
            params.json.sub.substring(params.json.sub.lastIndexOf("/")).split(".")[0],
            System.currentTimeMillis()
        )
        return loginInfo
    }


    override suspend fun homeRecommendVideos(index: Int): BaseResponse<HomeRecommendListResponse> {
        return bilibiliApi.homeRecommendVideoList(index, index)
    }

    override suspend fun videoUrl(cid: Long, bvid: String): BaseResponse<VideoUrlResponse> {
        var current = bilibiliStorage.wbiParams

        val wbiParams = if (current?.canUse() != true) {
            createAndSaveWbiParams()
        } else {
            current
        }


        val now = System.currentTimeMillis() / 1000
        val treeMap = TreeMap<String, Any>()
        treeMap.put("cid", cid)
        treeMap.put("bvid", bvid)
        treeMap.put("qn", 80)
        treeMap.put("fnval", 4048)
        treeMap.put("fourk", 1)
        treeMap.put("voice_balance", 1)
        treeMap.put("gaia_source", "pre-load")
        treeMap.put("web_location", "1550101")
        treeMap.put("wts", now)

        val sign = WbiUtil.enc(treeMap, wbiParams.image, wbiParams.sub)

        return bilibiliApi.videoUrl(cid, bvid = bvid, wts = now, sign = sign ?: "")

    }

    override suspend fun loginQrCode(): BaseResponse<QrCodeResponse> {
        return bilibiliApi.generateQrcode()
    }

    override suspend fun checkLogin(key: String): BaseResponse<PollQrcodeResponse> {
        return bilibiliApi.pollQrcode(key)
    }


    private suspend fun createAndSaveWbiParams(): WbiParams {
        val params = bilibiliApi.loginInfo().data!!

        val now = System.currentTimeMillis()

        val wbiParams = WbiParams(
            params.json.image.substring(params.json.image.lastIndexOf("/")).split(".")[0],
            params.json.sub.substring(params.json.sub.lastIndexOf("/")).split(".")[0],
            now
        )
        bilibiliStorage.wbiParams = wbiParams

        return wbiParams
    }

}


object WbiUtil {
    private val mixinKeyEncTab = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
        33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
        61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
        36, 20, 34, 44, 52
    )

    private val hexDigits = "0123456789abcdef".toCharArray()

    private fun md5(input: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(input.toByteArray(StandardCharsets.UTF_8))
            val result = CharArray(messageDigest.size * 2)
            for (i in messageDigest.indices) {
                result[i * 2] = hexDigits[(messageDigest[i].toInt() shr 4) and 0xF]
                result[i * 2 + 1] = hexDigits[messageDigest[i].toInt() and 0xF]
            }
            return String(result)
        } catch (e: NoSuchAlgorithmException) {
            return null
        }
    }

    private fun getMixinKey(imgKey: String?, subKey: String): String {
        val s = imgKey + subKey
        val key = StringBuilder()
        for (i in 0..31) key.append(s.get(mixinKeyEncTab[i]))
        return key.toString()
    }

    private fun encodeURIComponent(o: Any): String {

        return URLEncoder.encode(o.toString()).replace("+", "%20")
    }


    fun enc(
        map: TreeMap<String, Any>,
        imgKey: String,
        subKey: String,
    ): String? {
        val mixinKey = getMixinKey(imgKey, subKey)


        val param = map.entries.stream()
            .map<String> { it: MutableMap.MutableEntry<String, Any>? ->
                String.format(
                    "%s=%s",
                    it!!.key,
                    encodeURIComponent(it.value)
                )
            }
            .collect(Collectors.joining("&"))
        val s = param + mixinKey

        return md5(s)


    }
}