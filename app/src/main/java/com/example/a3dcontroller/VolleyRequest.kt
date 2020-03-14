package com.example.a3dcontroller

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import org.json.JSONException
import java.io.File

class VolleyRequest constructor(context: Context) {
    companion object{
        @Volatile
        private var INSTANCE: VolleyRequest? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleyRequest(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>){
        requestQueue.add(req)
    }


    fun makeHttpRequest(url: String) {

        val request = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                if (null != response) {
                    try {
                        var strRes = response.toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }, Response.ErrorListener { error ->  Log.e("ERROR", error.toString())})
        this.addToRequestQueue(request)
    }

}