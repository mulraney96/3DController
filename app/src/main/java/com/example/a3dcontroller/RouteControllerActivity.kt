package com.example.a3dcontroller

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_route_controller.*

class RouteControllerActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private lateinit var routePosition: RoutePosition


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_controller)
        val a = Coordinates(-1.0f,0.0f)
        val b = Coordinates(1.0f, 0.0f)
        val list = arrayListOf(a,b)
        val route = Route(list)
        routePosition = RoutePosition(route)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        currentPos_text.text = "Current Position = (${routePosition.getCurrentPosition().xValue}, ${routePosition.getCurrentPosition().yValue}"

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
    }

    override fun onSensorChanged(event: SensorEvent?) {
        var rotationMatrix: FloatArray =
            floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        var orientation: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)

        if (event!!.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientation)

            if(orientation[1]<0){
                routePosition.updatePosition(-1)
                currentPos_text.text = "Current Position = (${routePosition.getCurrentPosition().xValue}, ${routePosition.getCurrentPosition().yValue}"

            }
            else {
                routePosition.updatePosition(1)
                currentPos_text.text = "Current Position = (${routePosition.getCurrentPosition().xValue}, ${routePosition.getCurrentPosition().yValue}"

            }
        }
    }
}
