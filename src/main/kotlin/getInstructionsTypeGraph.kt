import org.jetbrains.skija.Paint
import kotlin.math.*


/*
 * in this file we form instructions for displaying the graphic
 */

/*
 * draw lines of graphics
 */
fun paintLines(instructions : MutableList<Instruction>, proportionx : Int, proportiony : Int, paint: Paint){
    for(i in 0 until dataGraph.n){
        for(j in 1 until dataGraph.xyValues[i].size) {
            instructions.add(
                Instruction(
                    Type = "Line",
                    coordinates = floatArrayOf(100f + (dataGraph.xyValues[i][j].first) * 1f / proportionx * 40f,
                        500f - dataGraph.xyValues[i][j].second * 1f / proportiony * 40f,
                        100f + dataGraph.xyValues[i][j - 1].first * 1f / proportionx * 40f,
                        500f - dataGraph.xyValues[i][j - 1].second * 1f / proportiony * 40f),
                    paints = arrayListOf(arrayListPaints[i])
                )
            )
        }
    }
}
/*
 * print marks on graphic
 */
fun printMarks(instructions: MutableList<Instruction>, proportionx : Int, proportiony : Int, paint: Paint)
{
    instructions.add(Instruction(Type = "String", text = fields[0], coordinates = floatArrayOf(100f + 11 * 40f, 500f + 15f)))
    instructions.add(Instruction(Type = "String", text = fields[1], coordinates = floatArrayOf(100f - 10f - 7f * fields[0].length, 500f - 11 * 40f)))
    for(j in 1..10){
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f + j * 40f, 500f, 100f + j * 40f, 505f), paints = arrayListOf(paint)))
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f, 500f - j * 40, 100f - 5f, 500f - j *40f), paints = arrayListOf(paint)))
        instructions.add(Instruction(Type = "String", text = "${proportionx * j}", coordinates = floatArrayOf(100f + j * 40f - 3f * "${proportionx * j}".length, 500f + 15f)))
        instructions.add(Instruction(Type = "String", text = "${proportiony * j}", coordinates = floatArrayOf(100f - 10f - 7f * "${proportiony * j}".length, 500f - j * 40f)))
    }
}

/*
 * draw axes
 */
fun getAxesSimpleGraph(instructions: MutableList<Instruction>, paint: Paint){
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f, 500f, 100f, 60f), paints = arrayListOf(paint)))
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(100f, 500f, 540f, 500f), paints = arrayListOf(paint)))
}

/*
 * draw a description where each category is mapped to a color
 * of the line on the graph
 */
fun printDescription(instructions: MutableList<Instruction>){
    var sty = 540f
    for(i in 0 until dataGraph.n){
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(100f, sty, 10f, 10f), paints = arrayListOf(arrayListPaints[i])))
        instructions.add(Instruction(Type = "String", text = dataGraph.names[i], coordinates = floatArrayOf(120f, sty + 10f)))
        sty += 20f
    }
}

fun getInstructionsTypeGraph(paint : Paint) : MutableList < Instruction > {
    var instructions : MutableList <Instruction > = mutableListOf()
    var maxx = dataGraph.xyValues.maxOf { it.maxOf { it.first } }
    var maxy = dataGraph.xyValues.maxOf { it.maxOf { it.second } }
    var proportionx = normal((maxx + 9).toInt())
    var proportiony = normal((maxy + 9).toInt())
    instructions.add(paintAll())
    printMarks(instructions, proportionx, proportiony, paint)
    getAxesSimpleGraph(instructions, paint)
    printDescription(instructions)
    paintLines(instructions, proportionx, proportiony, paint)
    return instructions
}