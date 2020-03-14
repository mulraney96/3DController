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
        return temp/temp2
        Log.d("slope", "${temp/temp2}")
    }

     fun getY(a: Coordinates, nextX: Float, m: Float):Float{
        return ((m*(nextX-a.xValue)) + a.yValue)
    }

    fun getDelta(a: Float, b: Float): Float{
        val temp = b - a
        return abs(temp/100)
    }

    fun getPosition(route: Route, current: Int, percent: Int, deltaX: Float, deltaZ: Float, slopeXY: Float, direction: Int ):Coordinates{
        val L = route[current]
        var nextXposition: Float
        var nextYposition: Float
        var nextZposition: Float

        if(L==route[route.size()-1] && direction==1){
            return L
        }
        if(L==route[0] && direction == -1 && percent==0){
            return route[0]
        }

            if (direction == 1) {
                val nextPercent = percent + 1
                nextXposition = L.xValue + (deltaX * nextPercent)
                nextYposition = getY(L, nextXposition, slopeXY)
                nextZposition = L.zValue +(deltaZ * nextPercent)
            } else {
                val nextPercent = percent - 1
                nextXposition = L.xValue + (deltaX * nextPercent)
                nextYposition = getY(L, nextXposition, slopeXY)
                nextZposition = L.zValue +(deltaZ * nextPercent)


            }

        return Coordinates(nextXposition, nextYposition, nextZposition)

    }

}

data class Coordinates( var xValue: Float,var yValue: Float, var zValue: Float){


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