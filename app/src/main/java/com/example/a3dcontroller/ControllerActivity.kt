package com.example.a3dcontroller

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_controller.*


class ControllerActivity : AppCompatActivity(), SensorEventListener {

        private lateinit var sensorManager: SensorManager

    var X: Double = 0.0
    var Y: Double = 0.0
    var Z: Double = 0.0

    var velocity: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0)
    var distance: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0)
    var dT: Double = 0.0
    var timestamp: Long = 0

        var url: String = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //locks in portrait mode
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_controller)

            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val vectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            val linearAccSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        }

        override fun onResume() {
            super.onResume()
            sensorManager.registerListener (
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL
            )
            sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL)
        }

        override fun onPause() {
            super.onPause()
            sensorManager.unregisterListener(this)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }


        override fun onSensorChanged(event: SensorEvent) {
            var rotationMatrix: FloatArray =
                floatArrayOf(0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f)
            var orientation: FloatArray = floatArrayOf(0.0f,0.0f,0.0f)

            if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event!!.values)
                SensorManager.getOrientation(rotationMatrix, orientation)

                var azimuth = (orientation[0] - OffsetValues.getAzimuthOffset()) * -1
                var pitch = (orientation[1] - OffsetValues.getPitchOffset()) * -1
                var roll = (orientation[2] - OffsetValues.getRollOffset())
                url =
                    "https://www.eeng.dcu.ie/~sadleirr/sensorlog/sensorlog.php?pitch=${pitch}&roll=${roll}&yaw=${azimuth}"
                makeHttpRequest(url)

            }

            if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                if(timestamp != 0L){
                    dT = (event.timestamp - timestamp) * (1.0/1000000000.0)

                    Log.i("time difference", "${dT}" )

                    val accX = event.values[0] - OffsetValues.getXOffset()
                    val accY = event.values[1] - OffsetValues.getYOffset()
                    val accZ = event.values[2] - OffsetValues.getZOffset()

                    velocity[0] = getVelocity(velocity[0], accX, dT)
                    velocity[1] = getVelocity(velocity[1], accY, dT)
                    velocity[2] = getVelocity(velocity[2], accZ, dT)

                    Log.i("Velocity calculated", "${velocity[0]}, ${velocity[1]}, ${velocity[2]}")

                    distance[0] = getDistance(velocity[0], dT)
                    distance[1] = getDistance(velocity[1], dT)
                    distance[2] = getDistance(velocity[2], dT)

                    Log.i("distance calculated", "${distance[0]}, ${distance[1]}, ${distance[2]}")

                    Log.i("coor before", "${X}, ${Y}, ${Z}")
                    X+= distance[0]
                    Y+= distance[1]
                    Z+= distance[2]

                    Log.i("coor after", "${X}, ${Y}, ${Z}")

                    coordinate_text.text = "X = ${X}\n\nY = ${Y}\n\nZ = ${Z}"
                }

                timestamp = event.timestamp

            }


        }

        private fun makeHttpRequest(url : String){
            val queue = Volley.newRequestQueue(this)
            val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    if (null != response) {
                        try {

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }, Response.ErrorListener { })
            queue.add(request)
        }

    fun getVelocity(vOld: Double, acc: Float, dT: Double):Double {
        var vNew: Double = vOld+(acc*dT)
        return vNew
    }

    fun getDistance(V: Double, dT: Double):Double = V*dT

}
