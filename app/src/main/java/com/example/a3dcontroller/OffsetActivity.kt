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
import kotlinx.android.synthetic.main.activity_offset.*


class OffsetActivity : AppCompatActivity(), SensorEventListener {

    private var azimuthValues = arrayListOf<Float>()
    private var pitchValues = arrayListOf<Float>()
    private var rollValues = arrayListOf<Float>()

    private var xAccelerationValues = arrayListOf<Float>()
    private var yAccelerationValues = arrayListOf<Float>()
    private var zAccelerationValues = arrayListOf<Float>()

    var orientationOffset: Boolean = false
    var accelerationOffset: Boolean = false

    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offset)

        azimuthValues.clear()
        pitchValues.clear()
        rollValues.clear()

        xAccelerationValues.clear()
        yAccelerationValues.clear()
        zAccelerationValues.clear()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
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

    override fun onSensorChanged(event: SensorEvent) {
        var rotationMatrix: FloatArray =
            floatArrayOf(0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f)
        var orientation: FloatArray = floatArrayOf(0.0f,0.0f,0.0f)

        if(accelerationOffset == true && orientationOffset == true){
            sensorManager.unregisterListener(this)
            sensorManager.unregisterListener(this)
            orientation_text.text = "    Offset Calculated"
        }

        if(event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event!!.values)
            SensorManager.getOrientation(rotationMatrix, orientation)

            if (rollValues!!.size <= 50) {
                azimuthValues!!.add(orientation[0])
                pitchValues!!.add(orientation[1])
                rollValues!!.add(orientation[2])
                Log.i("Orientation readings" , "${event.values[0]},${event.values[1]},${event.values[2]} ")
                orientation_text.text = "    Calibrating Offset"
            } else {
                orientationOffset = true
                DeviceValues.setOrientationOffsetValues(
                    calculateOffset(azimuthValues), calculateOffset(pitchValues),
                    calculateOffset(rollValues)
                )
                Log.i(
                    "Orientation offset",
                    "${DeviceValues.getAzimuthOffset()}, ${DeviceValues.getPitchOffset()}," +
                            " ${DeviceValues.getRollOffset()}"
                )
            }
        }
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            if(xAccelerationValues!!.size <= 50){
                xAccelerationValues.add(event.values[0])
                yAccelerationValues.add(event.values[1])
                zAccelerationValues.add(event.values[2])
                Log.i("accelerometer readings" , "${event.values[0]},${event.values[1]},${event.values[2]} ")
            }
            else{
                accelerationOffset = true
                DeviceValues.setPositionalOffsetValues(calculateOffset(xAccelerationValues),calculateOffset(yAccelerationValues),
                    calculateOffset(zAccelerationValues))
                Log.i("Acceleration offset", "${DeviceValues.getXOffset()}, ${DeviceValues.getYOffset()}, ${DeviceValues.getZOffset()}")
            }
        }

    }

    private fun calculateOffset(list: ArrayList<Float>): Float{
        var temp = 0.0f
        for(i in 0..list.lastIndex){
            temp += list[i]
        }
        return temp/list.size
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
         //To change body of created functions use File | Settings | File Templates.
    }
}


