import org.jetbrains.skija.Paint
import kotlin.math.*

fun getInstructionsTypeGraph(paint : Paint) : MutableList < Instruction > {
    var instructions : MutableList <Instruction > = mutableListOf()
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
    instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(0f, 0f, 1000f, 1000f), paints = arrayListOf(Paint().apply { color = greyColor } )))
    for(i in 0 until dataGraph.n){
        for(j in 0 until dataGraph.xyValues[i].size) {
            if (j != 0) {
                instructions.add(
                    Instruction(
                        Type = "Line",
                        coordinates = floatArrayOf(100f + (dataGraph.xyValues[i][j].first) * 1f / proportionx * 40f,
                        500f - dataGraph.xyValues[i][j].second * 1f / proportiony * 40f,
                        lastx,
                        lasty),
                        paints = arrayListOf(arrayListPaints[i])
                    )
                )
            }
            lastx = (100f + (dataGraph.xyValues[i][j].first) * 1f / proportionx * 40f)
            lasty = 500f - dataGraph.xyValues[i][j].second * 1f / proportiony * 40f
            instructions.add(Instruction(Type = "Point", coordinates = floatArrayOf(lastx, lasty), paints = arrayListOf(paint)))
        }
    }
    for(j in 1..10){
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f + j * 40f, 500f, 100f + j * 40f, 505f), paints = arrayListOf(paint)))
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f, 500f - j * 40, 100f - 5f, 500f - j *40f), paints = arrayListOf(paint)))
        instructions.add(Instruction(Type = "String", text = "${proportionx * j}", coordinates = floatArrayOf(100f + j * 40f - 3f * "${proportionx * j}".length, 500f + 15f)))
        instructions.add(Instruction(Type = "String", text = "${proportiony * j}", coordinates = floatArrayOf(100f - 10f - 7f * "${proportiony * j}".length, 500f - j * 40f)))
    }
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f, 500f, 100f, 60f), paints = arrayListOf(paint)))
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f, 500f, 540f, 500f), paints = arrayListOf(paint)))
    var sty = 540f
    for(i in 0 until dataGraph.n){
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(100f, sty, 10f, 10f), paints = arrayListOf(arrayListPaints[i])))
        instructions.add(Instruction(Type = "String", text = dataGraph.names[i], coordinates = floatArrayOf(120f, sty + 10f)))
        sty += 20f
    }
    return instructions
}