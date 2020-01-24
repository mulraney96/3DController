package com.example.a3dcontroller

object OffsetValues {
    //singleton class
    private var orientationOffsetValues: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)
    private var positionalOffsetValues: FloatArray = floatArrayOf(0.0f, 0.0f, 0.0f)

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