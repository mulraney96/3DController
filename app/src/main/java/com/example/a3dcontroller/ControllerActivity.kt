package com.example.a3dcontroller

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_controller.*
import org.json.JSONException
import java.lang.Math.abs


class ControllerActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var controlOrientation: Boolean = true
    private var oldPitch: Float = 0.0f
    private var oldRoll = 0.0f
    private var oldYaw = 0.0f
    private var oldX = 0.0f
    private var oldY = 0.0f
    private var oldZ = 0.0f

    var timestamp: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //locks in portrait mode
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(sensor.toString() + " changed accuracy to: ",
            Integer.toString(accuracy)
        )
    }


    override fun onSensorChanged(event: SensorEvent) {
        var rotationMatrix: FloatArray =
            floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        var orientation: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)

        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event!!.values)
            SensorManager.getOrientation(rotationMatrix, orientation)

            if(controlOrientation){
                controlText.text = "Now Controlling Orientation"

                if(abs(oldPitch-orientation[1])>0.09 || abs(oldRoll-orientation[2])>0.09 || abs(oldYaw-orientation[0])>0.09 ){
                    Log.i("pitch difference", "${abs(oldPitch-orientation[1])}")
                    DeviceValues.setOrientation(orientation[1], orientation[2], orientation[0])
                    VolleyRequest.makeHttpRequest(this.applicationContext)
                }
            }
            else{
                controlText.text = "Now Controlling Position";
                if(abs(oldX-orientation[2])>0.1 || abs(oldY-orientation[1])>0.1) {
                    DeviceValues.setPosition(orientation[2] * 1.1f, orientation[1] * 1.1f, 0.0f)
                    VolleyRequest.makeHttpRequest(this.applicationContext)
                }
            }
        }
        timestamp = event.timestamp
    }

    fun orientationButtonPressed(view: View) {
        controlOrientation = true
    }

    fun positionButtonPressed(view: View){
        controlOrientation = false
    }

}