package com.example.a3dcontroller

import android.util.Log
import java.lang.Math.abs
import kotlin.math.abs

object DeviceValues {
    //singleton class
    private var orientationOffsetValues: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)
    private var positionalOffsetValues: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)
    private var roll: Float = 0.0f
    private var pitch: Float = 0.0f
    private var yaw: Float = 0.0f
    private var X = 0.0f
    private var Y = 0.0f
    private var Z = 0.0f

    fun setOrientation(pitch: Float, roll: Float, yaw: Float){
        this.pitch = pitch
        this.roll = roll
        this.yaw = yaw
    }

    fun getRoll(): Float{
        return this.roll
    }

    fun getPitch(): Float{
        return this.pitch
    }

    fun getYaw(): Float{
        return this.yaw
    }

    fun setPosition(X: Float, Y: Float, Z: Float){
        this.X= X
        this.Y = Y
        this.Z = Z

    }

    fun getX(): Float {
        return this.X
    }

    fun getY(): Float{
        return this.Y
    }

    fun getZ(): Float{
        return this.Z
    }

    fun setOrientationOffsetValues(azimuth: Float, pitch: Float, roll: Float){
        orientationOffsetValues[0] = azimuth
        orientationOffsetValues[1] = pitch
        orientationOffsetValues[2] = roll
    }

    fun setPositionalOffsetValues(X: Float, Y: Float, Z :Float){
        positionalOffsetValues[0] = X
        positionalOffsetValues[1] = Y
        positionalOffsetValues[2] = Z
    }

    fun getAzimuthOffset():Float {
        return orientationOffsetValues[0]
    }

    fun getPitchOffset():Float{
        return orientationOffsetValues[1]
    }

    fun getRollOffset(): Float{
        return orientationOffsetValues[2]
    }

    fun getXOffset(): Float{
        return positionalOffsetValues[0]
    }

    fun getYOffset(): Float{
        return positionalOffsetValues[1]
    }

    fun getZOffset(): Float{
        return positionalOffsetValues[2]
    }


}