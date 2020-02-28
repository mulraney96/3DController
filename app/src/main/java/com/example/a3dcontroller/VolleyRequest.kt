package com.example.a3dcontroller

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

object VolleyRequest {


    fun makeHttpRequest(context: Context) {
        var url = "http://ec2-34-246-187-96.eu-west-1.compute.amazonaws.com/sensorLog.php?pitch=${DeviceValues.getPitch()}" +
                "&roll=${DeviceValues.getRoll()}&yaw=${DeviceValues.getYaw()}&" +
                "X=${DeviceValues.getX()}&Y=${DeviceValues.getY()}&Z=${DeviceValues.getZ()}"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                if (null != response) {
                    try {
                        var strRes = response.toString()
                        Log.i("Response", "$strRes")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }, Response.ErrorListener { error ->  Log.e("ERROR", error.toString())})
        queue.add(request)
    }

}