package qqbot.eve

import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class ImageService {
    val url1: String = "https://api.66mz8.com/api/rand.tbimg.php?format=png"
    val url2: String = "http://www.dmoe.cc/random.php"

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

    fun removeTemp() {
        val f = File(this.tempImgSrc)
        if (f.exists()) {
            f.delete()
        }
    }
}