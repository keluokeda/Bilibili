package com.ke.biliblli.common.entity

import java.util.Calendar

data class WbiParams(
    val image: String,
    val sub: String,
    val time: Long
) {

    fun canUse(): Boolean {
        val date = Calendar.getInstance()
        date.timeInMillis = time
        val now = Calendar.getInstance()
        return date.get(Calendar.DATE) == now.get(Calendar.DATE)
    }
}
