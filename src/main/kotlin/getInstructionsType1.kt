import org.jetbrains.skija.Paint
import kotlin.math.*
/*
 * Формирование инструкций вывода по входным данным
 */
fun lineInstructions(instructions : MutableList<Instruction>, paint : Paint, x0 : Float, y0 : Float, x1 : Float, y1 : Float, x2 : Float, y2 : Float){
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0, x1, y1), paints = arrayListOf(paint)))
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0, x2, y2), paints = arrayListOf(paint)))
}

fun printTags(instructions: MutableList<Instruction>, x0 : Float, y0 : Float, dy : Float, proportion: Int, paint: Paint){
    for(j in 1..10){
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0 - dy * j, x0 - 5f, y0 - dy * j), paints = arrayListOf(paint)))
        instructions.add(Instruction(Type = "String", text = "${proportion * j}", coordinates = floatArrayOf(x0 - dy - 5f * "${proportion * j}".length, y0 - dy * j)))
    }
}

fun getInstructionsType1(paint : Paint) : MutableList<Instruction>{
    var instructions : MutableList <Instruction> = mutableListOf()
    instructions.add(paintAll())
    var propotion = data.maxOf { it.values.maxOf {it} }.toInt() / 10
    var allsize = data.sumOf { max(it.values.size * 20f + 40f, 6.5f * it.key.length).toDouble() }.toFloat()
    if(propotion == 0){
        propotion++
    }
    propotion = normal(propotion)
    printDescription(instructions, 30f, 30f, 20f)
    var y0 = fields.size * 20f + 140
    var x0 = 140f
    lineInstructions(instructions, paint, x0, y0 + 300f, x0, y0 - 20f, x0 + allsize + 20f, y0 + 300f)
    printTags(instructions, x0, y0 + 300f, 20f, propotion, paint)
    for(i in 0 until n){
        var j = 0
        instructions.add(Instruction(Type = "String", text = data[i].key, coordinates = floatArrayOf(x0, 320f + y0)))
        for(value in data[i].values){
            var dy = value.toFloat() / propotion
            dy *= 20f
            instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, 300f + y0, x0 + 20f, 300f + y0), paints = arrayListOf(arrayListPaints[j])))
            instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(x0, 300f + y0 - dy, 20f, dy), paints = arrayListOf(arrayListPaints[j])))
            x0 += 20f
            j += 1
        }
        x0 += max(40f, data[i].key.length * 6.5f - data[i].values.size * 20f)
    }
    return instructions
}