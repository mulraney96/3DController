package com.example.a3dcontroller

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DeviceValues.setOrientation(0.0f,0.0f,0.0f)
        DeviceValues.setPosition(0.0f,0.0f,0.0f)
        VolleyRequest.makeHttpRequest(this)
    }

    fun startCalibration(view: View){
        val intent = Intent(this, OffsetActivity::class.java)
        startActivity(intent)
    }

    fun startController(view: View){
        val intent = Intent(this, ControllerActivity::class.java)
        startActivity(intent)
    }

}
