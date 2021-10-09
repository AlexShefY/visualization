import org.jetbrains.skija.Paint

fun getInstructionsTypeGraph(paint : Paint) : MutableList < Array<Any> > {
    var instructions : MutableList <Array <Any> > = mutableListOf()
    var maxx = 0f
    var maxy = 0f
    var stx = 100f
    var sty = 500f
    for(i in 0 until dataGraph.n){
        maxx = max(maxx, dataGraph.xValues[i])
        maxy = max(maxy, dataGraph.yValues[i])
    }
    var proportionx = (maxx / 10).toInt()
    var proportiony = (maxy / 10).toInt()
    if(proportionx == 0){
        proportionx = 1
    }
    proportionx = normal(proportionx)
    if(proportiony == 0){
        proportiony = 1
    }
    proportiony = normal(proportiony)
    var lastx = 0f
    var lasty = 0f
    instructions.add(arrayOf("Rect", 100f, 60f, 440f, 440f, 0xffd3dbe4.toInt()))
    for(i in 0 until dataGraph.n){
        if(i != 0){
            instructions.add(arrayOf("Line", 100f + (dataGraph.xValues[i]) * 1f / proportionx * 40f, 500f - dataGraph.yValues[i] * 1f / proportiony * 40f, lastx, lasty, paint.color))
        }
        lastx = (100f + (dataGraph.xValues[i]) * 1f / proportionx * 40f)
        lasty = 500f - dataGraph.yValues[i] * 1f / proportiony * 40f
        instructions.add(arrayOf("Point", lastx, lasty, paint.color))
    }
    for(j in 1..10){
        instructions.add(arrayOf("Line", 100f + j * 40f, 500f, 100f + j * 40f, 505f, paint.color))
        instructions.add(arrayOf("Line", 100f, 500f - j * 40, 100f - 5f, 500f - j *40f, paint.color))
        instructions.add(arrayOf("String", "${proportionx * j}", 100f + j * 40f - 3f * "${proportionx * j}".length, 500f + 15f))
        instructions.add(arrayOf("String", "${proportiony * j}", 100f - 10f - 7f * "${proportiony * j}".length, 500f - j * 40f))
    }
    instructions.add(arrayOf("Line", 100f, 500f, 100f, 60f, paint.color))
    instructions.add(arrayOf("Line", 100f, 500f, 540f, 500f, paint.color))
    return instructions
}