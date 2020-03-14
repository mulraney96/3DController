package com.example.a3dcontroller

import android.util.Log
import kotlin.math.abs
import kotlin.math.sqrt


 interface LineAlgebra  {
    fun getDistance(a: Coordinates, b: Coordinates):Float{
        val temp = (b.xValue-a.xValue)*(b.xValue-a.xValue)
        val temp2 = (b.yValue-a.yValue)*(b.yValue-a.yValue)
        return sqrt(temp+temp2)
    }

    fun getSlope(x1: Float, y1: Float, x2: Float, y2: Float):Float{
        val temp = (y2-y1)
        val temp2 = (x2-x1)
        if(temp2==0f)
            return 0f
        else
            return temp/temp2
    }

     fun getY(a: Coordinates, nextX: Float, m: Float):Float{
        val temp = (m*(nextX-a.xValue)) + a.yValue
         if(temp==0f)
             return 0.0f
         else
             return temp

    }

    fun getDelta(a: Float, b: Float): Float{
        val temp = b - a
        return temp/100
    }

    fun getPosition(route: Route, current: Int, percent: Int, deltaX: Float, deltaZ: Float, deltaAngleX: Float, deltaAngleY: Float, deltaAngleZ: Float, slopeXY: Float, direction: Int ):Coordinates{
        val L = route[current]
        var nextXposition: Float
        var nextYposition: Float
        var nextZposition: Float
        var nextZetaX: Float
        var nextZetaY: Float
        var nextZetaZ: Float

        if(L==route[route.size()-1] && direction==1 && percent==100){
            return L
            Log.i("Location", "returned L")
        }
        if(L==route[0] && direction == -1 && percent==0){
            return route[0]
            Log.i("Location", "Returned bottom node")
        }

        else if (direction != -1) {
                val nextPercent = percent + 1
                Log.i("Location", "Moving Up")
                nextXposition = L.xValue + (deltaX * nextPercent)
                nextYposition = getY(L, nextXposition, slopeXY)
                nextZposition = L.zValue + (deltaZ * nextPercent)
                nextZetaX = L.zetaX + (deltaAngleX*nextPercent)
                nextZetaY = L.zetaY + (deltaAngleY*nextPercent)
            Log.i("Y Angle", "$nextZetaY")
                nextZetaZ = L.zetaZ + (deltaAngleZ*nextPercent)
            Log.i("Z Angle", "$nextZetaZ")

            } else {
                val nextPercent = percent - 1
                nextXposition = L.xValue + (deltaX * nextPercent)
                nextYposition = getY(L, nextXposition, slopeXY)
                nextZposition = L.zValue +(deltaZ * nextPercent)
                nextZetaX = L.zetaX + (deltaAngleX*nextPercent)
                nextZetaY = L.zetaY + (deltaAngleY*nextPercent)
                nextZetaZ = L.zetaZ + (deltaAngleZ*nextPercent)

            }

        return Coordinates(nextXposition, nextYposition, nextZposition, nextZetaX, nextZetaY, nextZetaZ)

    }

}

data class Coordinates( var xValue: Float,var yValue: Float, var zValue: Float,var zetaX: Float, var zetaY: Float,var zetaZ: Float){


}

data class Route(var route: ArrayList<Coordinates>){
    fun size(): Int{
        return route.size
    }

    // overwriting the index operator.
    operator fun get(index: Int):Coordinates{
        return route.get(index)
    }
}



fun main() {
  /* val a = Coordinates(-1.0f, 2.0f)
    val b = Coordinates(1.0f, -2.0f)

    val list: ArrayList<Coordinates>? = arrayListOf()
    list!!.add(a)
    list.add(b)

    val route = Route(list)
    var currentPosition = route[0]
    var temp = RoutePosition(route)
    var m = temp.getSlope(a, b)
    println(m)
    var deltaX = temp.getDeltaX(a, b)
    println(deltaX)
    var percent = 1
    var nextX = currentPosition.xValue + (deltaX * percent)
    var nextY = temp.getY(currentPosition, nextX, m)
    println("X = $nextX, Y = $nextY")

*/


}