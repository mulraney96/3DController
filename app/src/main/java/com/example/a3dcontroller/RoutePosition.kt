package com.example.a3dcontroller

class RoutePosition(val route: Route) : LineAlgebra{
    private var slope: Float = 0.0f
    private var percent: Int = 0
    private var currentNode: Int = 0
    private var deltaX: Float = 0.0f
    private lateinit var currentPosition: Coordinates
    private lateinit var baseNode: Coordinates
    private lateinit var nextNode: Coordinates

    init {
        this.baseNode = route[currentNode]
        this.nextNode = route[currentNode+1]
        this.slope = getSlope(baseNode,nextNode)
        this.deltaX = getDeltaX(baseNode,nextNode)
        this.currentPosition = baseNode
    }

    fun updatePosition(direction: Int){
        if(direction==1){
            currentPosition = getPosition(route, currentNode, percent, deltaX, slope, direction)
        }
        if(direction==-1){

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



}