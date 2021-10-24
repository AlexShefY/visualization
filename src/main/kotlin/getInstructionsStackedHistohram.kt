import kotlin.math.*
import org.jetbrains.skija.Paint

var greyColor = 0xffd3dbe4.toInt()
var whiteColor = 0xffffffff.toInt()

fun paintAll() : Instruction{
    return Instruction(Type = "Rect", coordinates = floatArrayOf(0f, 0f, 1000f, 1000f), paints = arrayListOf(Paint().apply { color = greyColor } ))
}

fun printDescription(instructions: MutableList<Instruction>, stx : Float, sty : Float, dy : Float) {
    for(j in fields.indices){
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(stx, sty + dy * j,  10f, 10f), paints = arrayListOf(arrayListPaints[j])))
        instructions.add(Instruction(Type = "String", text = fields[j], coordinates = floatArrayOf(stx + 10f, sty + 10f + dy* j)))
    }
}

fun getInstructionsStackedHistohram(paint : Paint) : MutableList<Instruction>{
    var instructions : MutableList<Instruction> = mutableListOf()
    instructions.add(paintAll())
    var maxsum = data.maxOf { it.values.sum() }
    printDescription(instructions, 30f, 30f, 20f)
    var proportion = (maxsum / 10).toInt()
    if(proportion == 0){
        proportion = 1
    }
    proportion = normal(proportion)
    var y0 = fields.size * 20f + 220
    var x0 = 140f
    var dy = 420f
    var dx = 60f * data.size + 30f
    lineInstructions(instructions, paint, x0, y0 + dy, x0, y0, x0 + dx, y0 + dy)
    var stx = x0
    printTags(instructions, x0, y0 + dy, 40f, proportion, paint)
    for(j in 1..10){
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0 + dy - j * 40f, x0 + dx, y0 + dy - j * 40f), paints = arrayListOf(paint)))
    }
    for(i in data.indices) {
        stx += 30f
        var j = 0
        var curList = (data[i].values zip Array(data[i].values.size) { j++ }).toMutableList()
        instructions.add(Instruction(Type = "String", text = data[i].key, coordinates = floatArrayOf(stx, y0 + dy + 10f)))
        curList.sortBy { it.first; it.second }
        var sty = y0 + dy
        for (cur in curList) {
            var dy1 = cur.first * 1f / proportion * 40f
            instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(stx, sty - dy1, 30f, dy1), paints = arrayListOf(arrayListPaints[cur.second])))
            sty -= dy1
        }
        stx += 30f
    }
    return instructions
}
