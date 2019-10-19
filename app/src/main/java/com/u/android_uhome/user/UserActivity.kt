package com.u.android_uhome.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import com.u.android_uhome.R
import kotlinx.android.synthetic.main.activity_user.*
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

@RestController
class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        checkStatusBtn.setOnClickListener {
            //            sendGet()
//            Toast.makeText(applicationContext,
//                response, Toast.LENGTH_SHORT).show()
            post()
        }
    }

    private fun sendGet() {
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url =
                URL("http://ec2-13-229-73-124.ap-southeast-1.compute.amazonaws.com:3000/api/device/1")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Accept", "application/json")

            if (conn.responseCode != 200) {
                throw RuntimeException("Failed : HTTP error code : " + conn.responseCode)
            }
            val br = BufferedReader(
                InputStreamReader(
                    conn.inputStream
                )
            )
//             var output: String
            println("Output from Server .... \n")
            while ((br.readLine()) != null) {
                println(br.readLine())
            }

            conn.disconnect()

        } catch (e: MalformedURLException) {

            e.printStackTrace()

        } catch (e: IOException) {

            e.printStackTrace()

        }
    }

    private fun post() {
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val url = URL("http://ec2-13-229-73-124.ap-southeast-1.compute.amazonaws.com:3000/api/device")
            val conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")

            val input = "{\"name\":\"K\"}"

            val os = conn.outputStream
            os.write(input.toByteArray())
            os.flush()

            if (conn.responseCode != HttpURLConnection.HTTP_CREATED) {
                throw RuntimeException("Failed : HTTP error code : " + conn.responseCode)
            }

            val br = BufferedReader(
                InputStreamReader(
                    conn.inputStream
                )
            )

            println("Output from Server .... \n")
            while ((br.readLine()) != null) {
                println(br.readLine())
            }

            conn.disconnect()

        } catch (e: MalformedURLException) {

            e.printStackTrace()

        } catch (e: IOException) {

            e.printStackTrace()

        }
    }
}
