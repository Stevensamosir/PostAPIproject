// MainActivity.kt
package com.example.postapiexample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.betrbetainternshiptestproject.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    private val apiUrl = "https://jsonplaceholder.typicode.com/posts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun postData(view: View) {
        launchPostDataTask()
    }

    private fun launchPostDataTask() {
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                performPostRequest()
            }
            Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
        }
    }

    private fun performPostRequest(): String {
        return try {
            // Buat URL
            val url = URL(apiUrl)
            // Buat koneksi
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            // Buat data JSON
            val postData = JSONObject().apply {
                put("title", "Judul Contoh")
                put("body", "Isi Contoh")
                put("userId", 1)
            }

            // Konversi JSON menjadi byte
            val outputInBytes = postData.toString().toByteArray(StandardCharsets.UTF_8)

            // Tulis data ke output stream koneksi
            connection.outputStream.use { it.write(outputInBytes) }

            // Dapatkan respons
            val responseCode = connection.responseCode

            "Kode Respons: $responseCode"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }
}
