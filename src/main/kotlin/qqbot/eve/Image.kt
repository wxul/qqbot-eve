package qqbot.eve

import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.result.Result
import com.google.gson.*
import com.google.gson.reflect.TypeToken

data class HttpImgModel(val code: String = "", val acgurl: String = "", val width: String = "", val height: String = "")

class ImageService {
    val url1: String = "https://api.66mz8.com/api/rand.tbimg.php?format=png"
    val url2: String = "https://random.52ecy.cn/randbg.php"

    val tempImgSrc: String = "./temp/img1.png"

    fun downloadImage(url: String) {
        var conn: HttpURLConnection? = null
        println("download:${url}")
        try {
            conn = URL(url).openConnection() as HttpURLConnection
            // 建立链接
            conn.connect()
            // 打开输入流
            conn.inputStream.use { input ->
                BufferedOutputStream(FileOutputStream(this.tempImgSrc)).use { output ->
                    input.copyTo(output) // 将文件复制到本地
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            conn?.disconnect()
        }
    }

    fun getImageApiString(url: String): String? {
        val (request, response, result) = url
                .httpGet()
                .response()

        val (data, error) = result
        if (error == null) {
            return String(response.data)
        } else {
            return null
        }
    }

    fun getImageApi(): HttpImgModel? {
        val res = this.getImageApiString(this.url2)
        println(res)
        if (res == null) {
            return null
        } else {
            val result = object : TypeToken<HttpImgModel>() {}.type
            var img: HttpImgModel = Gson().fromJson(res, result)
            return img
        }
    }

    fun removeTemp() {
        val f = File(this.tempImgSrc)
        if (f.exists()) {
            f.delete()
        }
    }
}