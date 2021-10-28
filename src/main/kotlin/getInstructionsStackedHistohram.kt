import kotlin.math.*
import org.jetbrains.skija.Paint

var greyColor = 0xffd3dbe4.toInt()
var whiteColor = 0xffffffff.toInt()

fun addOneColumnStacked(instructions: MutableList<Instruction>, i : Int, proportion : Int, stx : Float, sty : Float){
    var j = 0
    var curList = (data[i].values zip Array(data[i].values.size) { j++ }).toMutableList()
    curList.sortBy { it.first; it.second }
    var sty1 = sty
    for (cur in curList) {
        var dy1 = cur.first * 1f / proportion * 40f
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(stx, sty1 - dy1, 30f, dy1), paints = arrayListOf(arrayListPaints[cur.second])))
        sty1 -= dy1
    }
}

fun addColumnsStacked(instructions: MutableList<Instruction>, x0 : Float, y0 : Float, dy : Float, proportion : Int){
    var stx = x0
    for(i in data.indices) {
        stx += 30f
        instructions.add(Instruction(Type = "String", text = data[i].key, coordinates = floatArrayOf(stx, y0 + dy + 10f)))
        addOneColumnStacked(instructions, i, proportion, stx, y0 + dy)
        stx += 30f
    }
}

fun printHelpTagLines(instructions: MutableList<Instruction>, x0 : Float, y0 : Float, dx : Float, dy : Float, paint : Paint){
    for(j in 1..10){
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0 + dy - j * 40f, x0 + dx, y0 + dy - j * 40f), paints = arrayListOf(paint)))
    }
}

fun getInstructionsStackedHistohram(paint : Paint) : MutableList<Instruction>{
    var instructions : MutableList<Instruction> = mutableListOf()
    var maxsum = data.maxOf { it.values.sum() }
    var proportion = normal(maxsum.toInt())
    var y0 = fields.size * 20f + 220
    var x0 = 140f
    var dy = 420f
    var dx = 60f * data.size + 30f
    instructions.add(paintAll())
    printDescription(instructions, 30f, 30f, 20f)
    lineInstructions(instructions, paint, x0, y0 + dy, x0, y0, x0 + dx, y0 + dy)
    printTags(instructions, x0, y0 + dy, 40f, proportion, paint)
    printHelpTagLines(instructions, x0, y0, dx, dy, paint)
    addColumnsStacked(instructions, x0, y0, dy, proportion)
    return instructions
}
