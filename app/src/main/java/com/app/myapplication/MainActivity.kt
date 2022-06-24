package com.app.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var httpClient: OkHttpClient

    @Inject
    lateinit var executorServiceDecorator: ExecutorServiceDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LOG.debug("Started Activity")
        val okHttpClient = httpClient.newBuilder()
            .dispatcher(Dispatcher(executorServiceDecorator))
            .addInterceptor(httpClient.interceptors.first())
            .build()

        for (i in 0..10) {
            httpClient.newCall(getPOSTRequestRequest(false)).enqueue(getResponseCallback())
        }
        okHttpClient.newCall(getPOSTRequestRequest(true)).enqueue(getResponseCallback())


        for (i in 0..10) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = httpClient.newCall(getPOSTRequestRequest(false)).execute()
                LOG.debug("onResponse: got {} from Non Priority Call", response)

            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = okHttpClient.newCall(getPOSTRequestRequest(true)).execute()
            LOG.debug("onResponse: got {} from Priority Call", response)

        }
    }

    private fun getResponseCallback() = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            LOG.debug("onResponse: got {} from {}", response, call.request().tag())
        }
    }

    private fun getGETRequestRequest(): Request {
        val urlBuilder = "https://api.github.help".toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("v", "1.0")
        urlBuilder?.addQueryParameter("user", "vogella")
        return Request.Builder().url(urlBuilder?.build().toString()).tag("Normal").build()
    }

    private val _json: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    private fun bowlingJson(): String {
        return ("{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + "Jesse" + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + "Jake" + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}")
    }

    private fun getPOSTRequestRequest(isPriority: Boolean): Request {
        return Request.Builder()
            .url("http://www.roundsapp.com/post")
            .post(bowlingJson().toRequestBody(_json))
            .tag(if (isPriority) "Priority Call" else "Non Priority Call ")
            .build()
    }

    companion object {

        private val LOG = logger<MainActivity>("OKHTTPCalls")
    }
}
