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
    this.currentPosition = baseNode
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
}

fun moveDownNode(){
    currentNode--
    updateValues()
}

fun updateValues(){
    this.baseNode = route[currentNode]
    Log.i("Base Node", "$baseNode")
    this.nextNode = route[currentNode+1]
    this.slopeXY = getSlope(baseNode.xValue, baseNode.yValue, nextNode.xValue, nextNode.yValue)
    Log.i("Slope", "$slopeXY")
    this.deltaX = getDelta(baseNode.xValue,nextNode.xValue)
    Log.i("Delta X", "$deltaX")
    this.deltaZ = getDelta(baseNode.zValue, nextNode.zValue)
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
                currentPosition = route[currentNode+1]
                Log.i("End Node", "Here")
            }
            else{
                moveUpNode()
                Log.d("Move Node", "UP")
                Log.d("percent", "$percent")
                Log.d("Current Position", "$currentPosition")
            }
        }
        else {
            currentPosition = getPosition(route, currentNode, percent, deltaX, deltaZ, slopeXY, direction)
            percentUp()
            Log.i("current percent", "$percent")
        }
    }
    if(direction==-1){
        if(this.percent==0){
            if(currentNode==0){
                currentPosition = route[0]
                Log.i("start Node", "here")
            }
            else{
                moveDownNode()
                Log.d("Move Node", "DOWN")
                Log.d("percent", "$percent")
            }
        }
        else {
            currentPosition = getPosition(route, currentNode, percent, deltaX, deltaZ, slopeXY, direction)
            percentDown()
        }
    }
    var url = "http://ec2-52-211-114-128.eu-west-1.compute.amazonaws.com/routeLog.php?X=" +
            "${currentPosition.xValue}&Y=${currentPosition.yValue}&Z=${currentPosition.zValue}"

    VolleyRequest.getInstance(context).makeHttpRequest(url)

}


}