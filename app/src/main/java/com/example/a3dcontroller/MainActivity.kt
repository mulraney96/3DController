package com.example.a3dcontroller

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity(){

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DeviceValues.setOrientation(0.0f,0.0f,0.0f)
        DeviceValues.setPosition(0.0f,0.0f,0.0f)
        var url = "http://ec2-52-211-114-128.eu-west-1.compute.amazonaws.com/sensorLog.php?pitch=0" +
                "&roll=0&yaw=0&" +
                "X=0&Y=0&Z=0"
        VolleyRequest.getInstance(this).makeHttpRequest(url)
    }

    fun startCalibration(view: View){
        val intent = Intent(this, OffsetActivity::class.java)
        startActivity(intent)
    }

    fun startRoute(view:View){
        val intent = Intent(this, RouteControllerActivity::class.java)
        startActivity(intent)
    }

    fun startController(view: View){
        val intent = Intent(this, ControllerActivity::class.java)
        startActivity(intent)
    }

}
