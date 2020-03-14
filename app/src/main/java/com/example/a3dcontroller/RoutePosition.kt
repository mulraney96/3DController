package com.example.a3dcontroller

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import java.io.File

class RoutePosition(val route: Route) : LineAlgebra, Runnable{
    private var slopeXY: Float = 0.0f
    private var percent: Int = 0
    private var currentNode: Int = 0
    private var deltaX: Float = 0.0f
    private var deltaZ: Float = 0.0f

    private var deltaAngleX: Float = 0.0f
    private var deltaAngleY = 0.0f
    private var deltaAngleZ = 0.0f

    private var currentPosition: Coordinates
    private var baseNode: Coordinates
    private var nextNode: Coordinates
    private lateinit var context: Context
    private var direction: Int = 1



init {
    this.baseNode = route[currentNode]
    this.nextNode = route[currentNode+1]
    this.slopeXY = getSlope(baseNode.xValue, baseNode.yValue, nextNode.xValue, nextNode.yValue)
    this.deltaX = getDelta(baseNode.xValue,nextNode.xValue)
    this.deltaZ = getDelta(baseNode.zValue, nextNode.zValue)
    this.deltaAngleX = getDelta(baseNode.zetaX, nextNode.zetaX)
    Log.i("Ox","$deltaAngleX")
    this.deltaAngleY = getDelta(baseNode.zetaY, nextNode.zetaY)
    Log.i("Oy","$deltaAngleY")
    this.deltaAngleZ = getDelta(baseNode.zetaZ, nextNode.zetaZ)
    Log.i("Oz","$deltaAngleZ")
    this.currentPosition = baseNode
    Log.i("position", "$currentPosition")
}

fun updatePosition(direction: Int){
    this.direction = direction
    run()
}

fun setContext(context: Context){
    this.context = context
}

fun moveUpNode(){
    currentNode++
    updateValues()
    Log.i("MOVEUP", "")
}

fun moveDownNode(){
    currentNode--
    updateValues()
    Log.i("MOVEDOWN", "")
}

fun updateValues(){
    this.baseNode = route[currentNode]
    this.nextNode = route[currentNode+1]
    this.slopeXY = getSlope(baseNode.xValue, baseNode.yValue, nextNode.xValue, nextNode.yValue)
    this.deltaX = getDelta(baseNode.xValue,nextNode.xValue)
    this.deltaZ = getDelta(baseNode.zValue, nextNode.zValue)
    this.deltaAngleX = getDelta(baseNode.zetaX, nextNode.zetaX)
    Log.i("Ox","$deltaAngleX")
    this.deltaAngleY = getDelta(baseNode.zetaY, nextNode.zetaY)
    Log.i("Oy","$deltaAngleY")
    this.deltaAngleZ = getDelta(baseNode.zetaZ, nextNode.zetaZ)
    Log.i("Oz","$deltaAngleZ")
    this.currentPosition = baseNode
    if(percent==100){
        percent = 0
    }
    else{
        percent = 100
    }
}

fun getCurrentPosition(): Coordinates{
    return this.currentPosition
}


fun updateCurrent(direction: Int){
    if(direction<0)
        this.currentNode--
    else
        this.currentNode++
}

fun getCurrent(): Int{
    return this.currentNode
}

fun percentUp(){
    percent++
}

fun percentDown(){
    percent--
}

override fun run() {
    if(direction==1){
        if(this.percent==100){
            if(currentNode+1==route.size()-1){
                Log.i("Location", "Top Node")
                currentPosition = route[currentNode+1]
            }
            else{
                moveUpNode()
            }
        }
        else {
            currentPosition = getPosition(route, currentNode, percent, deltaX, deltaZ, deltaAngleX, deltaAngleY, deltaAngleZ, slopeXY, direction)
            percentUp()
        }
    }
    if(direction==-1){
        if(this.percent==0){
            if(currentNode==0){
                currentPosition = route[0]
                Log.i("Location", "Bottom Node")
            }
            else{
                moveDownNode()
            }
        }
        else {
            currentPosition = getPosition(route, currentNode, percent, deltaX, deltaZ, deltaAngleX, deltaAngleY, deltaAngleZ, slopeXY, direction)
            percentDown()
        }
    }
    Log.i("percent", "$percent")
    Log.i("Base Node", "$baseNode")
    Log.i("Next Node" , "$nextNode")
    var url = "http://ec2-52-211-114-128.eu-west-1.compute.amazonaws.com/routeLog.php?X=" +
            "${currentPosition.xValue}&Y=${currentPosition.yValue}&Z=${currentPosition.zValue}" +
            "&Ox=${currentPosition.zetaX}&Oy=${currentPosition.zetaY}&Oz=${currentPosition.zetaZ}"


    VolleyRequest.getInstance(context).makeHttpRequest(url)

}


}