@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 *  {
 *       "cid": 516976173,
 *       "page": 1,
 *       "from": "vupload",
 *       "part": "01【开荒】新手教程+基础道具（如果想看主线你任务，从P16开始，将由完整的主线任务体验）",
 *       "duration": 1669,
 *       "vid": "",
 *       "weblink": "",
 *       "dimension": { "width": 3840, "height": 2160, "rotate": 0 },
 *       "first_frame": "http://i2.hdslb.com/bfs/storyff/n220226a227s72tlccizzo371uo5qzm9_firsti.jpg",
 *       "ctime": 0
 *     }
 */
@Serializable
data class PageResponse(
    val cid: Long,
    val page: Int,
    val part: String,
    val duration: Long,
    @JsonNames("first_frame")
    val firstFrame: String? = null
)
