package com.example.a3dcontroller

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_route_controller.*

class RouteControllerActivity : AppCompatActivity(), SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private lateinit var routePosition: RoutePosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_controller)
        val a = Coordinates(-4.0f,-3.0f, 0.0f)
        val b = Coordinates(-2.0f, 2.0f, 0.0f)
        val c = Coordinates(0.0f, 5.0f, 0.0f)
        val d = Coordinates(1.0f, 3.5f, 0.0f)
        val e = Coordinates(2.0f, -0.5f, 0.0f)


        val list = arrayListOf(a,b,c,d,e)
        val route = Route(list)
        routePosition = RoutePosition(route)
        routePosition.setContext(this)
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
    }

    override fun onSensorChanged(event: SensorEvent?) {
        var rotationMatrix: FloatArray =
            floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        var orientation: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)

        if (event!!.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientation)

            if(orientation[1]<0){
                currentPos_text.text = "Current Position:\nX = ${routePosition.getCurrentPosition().xValue}" +
                        "\nY=${routePosition.getCurrentPosition().yValue}" +
                        "\nZ=${routePosition.getCurrentPosition().zValue}"
                routePosition.updatePosition(-1)
            }
            else {
                currentPos_text.text = "Current Position:\nX = ${routePosition.getCurrentPosition().xValue}" +
                        "\nY=${routePosition.getCurrentPosition().yValue}" +
                        "\nZ=${routePosition.getCurrentPosition().zValue}"
                routePosition.updatePosition(1)
            }
        }
    }
}
