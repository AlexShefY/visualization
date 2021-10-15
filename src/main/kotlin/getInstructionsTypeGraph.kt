import org.jetbrains.skija.Paint
import kotlin.math.*

fun getInstructionsTypeGraph(paint : Paint) : MutableList < Array<Any> > {
    var instructions : MutableList <Array <Any> > = mutableListOf()
    var maxx = dataGraph.xyValues.maxOf { it.maxOf { it.first } }
    var maxy = dataGraph.xyValues.maxOf { it.maxOf { it.second } }
    var proportionx = ((maxx + 9) / 10).toInt()
    var proportiony = ((maxy + 9) / 10).toInt()
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
    instructions.add(arrayOf("Rect", 0f, 0f, 1000f, 1000f, greyColor))
    for(i in 0 until dataGraph.n){
        for(j in 0 until dataGraph.xyValues[i].size) {
            if (j != 0) {
                instructions.add(
                    arrayOf(
                        "Line",
                        100f + (dataGraph.xyValues[i][j].first) * 1f / proportionx * 40f,
                        500f - dataGraph.xyValues[i][j].second * 1f / proportiony * 40f,
                        lastx,
                        lasty,
                        arrayListPaints[i].color
                    )
                )
            }
            lastx = (100f + (dataGraph.xyValues[i][j].first) * 1f / proportionx * 40f)
            lasty = 500f - dataGraph.xyValues[i][j].second * 1f / proportiony * 40f
            instructions.add(arrayOf("Point", lastx, lasty, paint.color))
        }
    }
    for(j in 1..10){
        instructions.add(arrayOf("Line", 100f + j * 40f, 500f, 100f + j * 40f, 505f, paint.color))
        instructions.add(arrayOf("Line", 100f, 500f - j * 40, 100f - 5f, 500f - j *40f, paint.color))
        instructions.add(arrayOf("String", "${proportionx * j}", 100f + j * 40f - 3f * "${proportionx * j}".length, 500f + 15f))
        instructions.add(arrayOf("String", "${proportiony * j}", 100f - 10f - 7f * "${proportiony * j}".length, 500f - j * 40f))
    }
    instructions.add(arrayOf("Line", 100f, 500f, 100f, 60f, paint.color))
    instructions.add(arrayOf("Line", 100f, 500f, 540f, 500f, paint.color))
    var sty = 540f
    for(i in 0 until dataGraph.n){
        instructions.add(arrayOf("Rect", 100f, sty, 10f, 10f, arrayListPaints[i].color))
        instructions.add(arrayOf("String", dataGraph.names[i], 120f, sty + 10f))
        sty += 20f
    }
    return instructions
}