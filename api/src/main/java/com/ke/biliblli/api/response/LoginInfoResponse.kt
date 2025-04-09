package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class LoginInfoResponse(
    val isLogin: Boolean,
    @JsonNames("email_verified")
    val emailVerified: Int? = null,
    val face: String? = null,
    @JsonNames("level_info")
    val levelInfo: UserLevelInfo? = null,
    val mid: Long? = null,
    @JsonNames("mobile_verified")
    val mobileVerified: Int? = null,
    /**
     * 硬币
     */
    val money: Double? = null,

    val moral: Int? = null,

    val official: UserOfficial? = null,
    val officialVerify: UserOfficialVerify? = null,
    val pendant: AvatarPendant? = null,
    val scores: Int? = null,
    val uname: String? = null,
    val vipDueDate: Long? = null,
    val vipStatus: Int? = null,
    /**
     * 0：无
     * 1：月度大会员
     * 2：年度及以上大会员
     */
    val vipType: Int? = null,
    @JsonNames("vip_pay_type")
    val vipPayType: Int? = null,
    @JsonNames("vip_theme_type")
    val vipThemeType: Int? = null,
    @JsonNames("vip_avatar_subscript")
    val vipAvatarSubscript: Int?  = null,
    @JsonNames("vip_nickname_color")
    val vipNicknameColor: String? = null,
    @JsonNames("wbi_img")
    val json: WbiImage
)

@Serializable
data class UserLevelInfo(
    @JsonNames("current_level")
    val currentLevel: Int,
    @JsonNames("current_min")
    val currentMin: Int,
    @JsonNames("current_exp")
    val currentExp: Int,
    @JsonNames("next_exp")
    val nextExp: Int
)

@Serializable
data class UserOfficial(
    val role: Int,
    val title: String,
    val desc: String,
    val type: Int
)

@Serializable
data class UserOfficialVerify(
    val type: Int,
    val desc: String
)

@Serializable
data class AvatarPendant(
    val pid: Int,
    val name: String,
    val image: String,
    val expire: Long
)

@Serializable
data class VipLabel(
    val path: String,
    val text: String,
    @JsonNames("label_theme")
    val labelTheme: String,
    @JsonNames("text_color")
    val textColor: String
)