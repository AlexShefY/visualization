import org.jetbrains.skija.Paint
import kotlin.math.*
/*
 * Формирование инструкций вывода по входным данным
 */

fun addOneKeyColumnsClustered(instructions: MutableList<Instruction>, i : Int, proportion : Int, x0 : Float, y0 : Float){
    instructions.add(Instruction(Type = "String", text = data[i].key, coordinates = floatArrayOf(x0, 320f + y0)))
    var j = 0
    for(value in data[i].values){
        var dy = value / proportion
        dy *= 20f
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0 + 20f * j, 300f + y0, x0 + 20f * (j + 1), 300f + y0), paints = arrayListOf(arrayListPaints[j])))
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(x0 + 20f * j, 300f + y0 - dy, 20f, dy), paints = arrayListOf(arrayListPaints[j])))
        j += 1
    }
}

fun addColumnsClustered(instructions : MutableList<Instruction>, x0 : Float, y0 : Float, proportion : Int){
    var xcur = x0
    for(i in 0 until n){
        addOneKeyColumnsClustered(instructions, i, proportion, xcur, y0)
        xcur += max(40f, data[i].key.length * 6.5f - data[i].values.size * 20f) + 20f * data[i].values.size
    }
}

fun getInstructionsGraph(paint : Paint) : MutableList<Instruction>{
    var instructions : MutableList <Instruction> = mutableListOf()
    var proportion = normal(data.maxOf { oneData -> oneData.values.maxOf {it} }.toInt())
    var allsize = data.sumOf { max(it.values.size * 20f + 40f, 6.5f * it.key.length).toDouble() }.toFloat()
    var y0 = fields.size * 20f + 140
    var x0 = 140f
    instructions.add(paintAll())
    printDescription(instructions, 30f, 30f, 20f)
    lineInstructions(instructions, paint, x0, y0 + 300f, x0, y0 - 20f, x0 + allsize + 20f, y0 + 300f)
    printTags(instructions, x0, y0 + 300f, 20f, proportion, paint)
    addColumnsClustered(instructions, x0, y0, proportion)
    return instructions
}