package com.example.a3dcontroller

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class DeadReckoning : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    var X: Double = 0.0
    var Y: Double = 0.0
    var Z: Double = 0.0

    var velocity: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0)
    var distance: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0)
    var dT: Double = 0.0
    var timestamp: Long = 0


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
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
        Log.i("Sensor Data", "${event!!.values[0]}, ${event.values[1]}, ${event.values[2]}, ${event.timestamp}")

        if(timestamp != 0L){
            dT = (event.timestamp - timestamp) * (1.0/1000000000.0)

            Log.i("time difference", "${dT}" )

            velocity[0] = getVelocity(velocity[0], event.values[0], dT)
            velocity[1] = getVelocity(velocity[1], event.values[1], dT)
            velocity[2] = getVelocity(velocity[2], event.values[2], dT)

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

            //centreText.text = "X = ${event.values[0]}\n\nY = ${event.values[1]}\n\nZ = ${event.values[2]}"
        }

        timestamp = event.timestamp

    }

    fun getVelocity(vOld: Double, acc: Float, dT: Double):Double {
        var vNew: Double = vOld+(acc*dT)
        return vNew
    }

    fun getDistance(V: Double, dT: Double):Double = V*dT
}

