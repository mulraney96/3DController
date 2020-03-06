package com.example.a3dcontroller

import android.content.Context
import android.util.Log

class RoutePosition(val route: Route) : LineAlgebra, Runnable{
    private var slope: Float = 0.0f
    private var percent: Int = 0
    private var currentNode: Int = 0
    private var deltaX: Float = 0.0f
    private var currentPosition: Coordinates
    private var baseNode: Coordinates
    private var nextNode: Coordinates
    private lateinit var context: Context

    init {
        this.baseNode = route[currentNode]
        this.nextNode = route[currentNode+1]
        this.slope = getSlope(baseNode,nextNode)
        this.deltaX = getDeltaX(baseNode,nextNode)
        this.currentPosition = baseNode
    }

    fun updatePosition(direction: Int){
        if(direction==1){
            if(this.percent==100){
                if(currentNode+1==route.size()-1){
                    currentPosition = route[currentNode+1]
                }
                else{
                    moveUpNode()
                    Log.d("Move Node", "UP")
                    Log.d("percent", "$percent")
                }
            }
            else {
                currentPosition = getPosition(route, currentNode, percent, deltaX, slope, direction)
                percentUp()
            }
        }
        if(direction==-1){
            if(this.percent==0){
                if(currentNode==0){
                    currentPosition = route[0]
                }
                else{
                   moveDownNode()
                    Log.d("Move Node", "DOWN")
                    Log.d("percent", "$percent")
                }
            }
            else {
                currentPosition = getPosition(route, currentNode, percent, deltaX, slope, direction)
                percentDown()
            }
        }
        var url = "http://ec2-52-211-114-128.eu-west-1.compute.amazonaws.com/routeLog.php?X=" +
                "${currentPosition.xValue}&Y=${currentPosition.yValue}&Z=-5"
        VolleyRequest.makeHttpRequest(context, url)
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
        this.nextNode = route[currentNode+1]
        this.slope = getSlope(baseNode,nextNode)
        this.deltaX = getDeltaX(baseNode,nextNode)
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

    fun updateSlope() {
        if (route[currentNode + 1] != null) {
            this.slope = getSlope(route[currentNode], route[currentNode + 1])
        }
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}