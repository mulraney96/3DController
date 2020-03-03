package com.example.a3dcontroller

import kotlin.math.abs
import kotlin.math.sqrt


 interface LineAlgebra  {
    fun getDistance(a: Coordinates, b: Coordinates):Float{
        val temp = (b.xValue-a.xValue)*(b.xValue-a.xValue)
        val temp2 = (b.yValue-a.yValue)*(b.yValue-a.yValue)
        return sqrt(temp+temp2)
    }

    fun getSlope(a: Coordinates, b: Coordinates):Float{
        val temp = (b.yValue-a.yValue)
        val temp2 = (b.xValue-a.xValue)
        return temp/temp2
    }

    private fun getY(a: Coordinates, nextX: Float, m: Float):Float{
        return (m*(nextX-a.xValue) - a.yValue)
    }

    fun getDeltaX(a: Coordinates, b: Coordinates): Float{
        val x1 = a.xValue
        val x2 = b.xValue

        val temp = x2 - x1
        return abs(temp/100)
    }

    fun getPosition(route: Route, current: Int, percent: Int, deltaX: Float, slope: Float, direction: Int ):Coordinates{
        val L = route[current]
        var nextXposition: Float
        var nextYposition: Float

        if(L==route[route.size()-1] && direction==1){
            return L
        }
        if(L==route[0] && direction == -1 && percent==0){
            return route[0]
        }
        else {
            if (direction == 1) {
                val nextPercent = percent + 1
                nextXposition = L.xValue + (deltaX * nextPercent)
                nextYposition = getY(L, nextXposition, slope)
            } else {
                val nextPercent = percent - 1
                nextXposition = L.xValue + (deltaX * nextPercent)
                nextYposition = getY(L, nextXposition, slope)
            }
        }

        return Coordinates(nextXposition, nextYposition)

    }

}

data class Coordinates( var xValue: Float,var yValue: Float){


}

data class Route(var route: ArrayList<Coordinates>){
    fun size(): Int{
        return route.size
    }

    // overwriting the index operator.
    operator fun get(index: Int):Coordinates{
        return this[index]
    }
}



/*un main(){
    val a = Coordinates(0.0f, 1.0f)
    val b = Coordinates(1.0f, -2.0f)
    val c = Coordinates(2.0f, 3.0f)
    val d = Coordinates(4.0f, -1.0f)

    val list: ArrayList<Coordinates>? = arrayListOf()
    list!!.add(a)
    list.add(b)
    list.add(c)
    list.add(d)

    val route = Route(list)


}*/