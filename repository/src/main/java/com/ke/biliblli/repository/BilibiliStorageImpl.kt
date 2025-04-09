package com.ke.biliblli.repository

import android.content.Context
import androidx.core.content.edit
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.entity.WbiParams
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BilibiliStorageImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : BilibiliStorage {
    private val sharedPreferences = context.getSharedPreferences("bilibili", Context.MODE_PRIVATE)

    override var wbiParams: WbiParams?
        get() {
            val image = sharedPreferences.getString("wbi_image", null) ?: return null

            val sub = sharedPreferences.getString("wbi_sub", null) ?: return null

            val time = sharedPreferences.getLong("wbi_time", 0)

            return WbiParams(image, sub, time)
        }
        set(value) {
            sharedPreferences.edit(commit = true) {
                putString("wbi_image", value?.image)
                putString("wbi_sub", value?.sub)
                putLong("wbi_time", value?.time ?: 0)
            }
        }
}