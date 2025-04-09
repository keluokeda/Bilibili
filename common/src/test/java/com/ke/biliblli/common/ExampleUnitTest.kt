package com.ke.biliblli.common

import org.junit.Test
import java.util.TreeMap

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {


    }

    @Test
    fun wbiTest() {

        val imgKey = "7cd084941338484aae1ad9425b84077c"
        val subKey = "4932caff0ff746eab6f01bf08b70ac45"


        // 用TreeMap自动排序
        val map = TreeMap<String, Any>()
        map.put("foo", "114")
        map.put("bar", "514")
        map.put("zab", 1919810)
        map.put("wts", 1702204169)

        val result = WbiUtil.enc(map, imgKey, subKey)
        println(result)//8f6f2b5b3d485fe1886cec6a0be8c5d4
    }
}