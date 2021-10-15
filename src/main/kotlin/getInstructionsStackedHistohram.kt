import kotlin.math.*
import org.jetbrains.skija.Paint

var greyColor = 0xffd3dbe4.toInt()

fun getInstructionsStackedHistohram(paint : Paint) : MutableList<Array<Any> >{
    var instructions : MutableList<Array<Any>> = mutableListOf()
    instructions.add(arrayOf("Rect", 0f, 0f, 1000f, 1000f, greyColor))
    var maxsum = data.maxOf { it.values.sum() }
    for(j in fields.indices){
        instructions.add(arrayOf("Rect", 30f, 30f + 20 * j * 1f,  10f, 10f, arrayListPaints[j].color.toInt()))
        instructions.add(arrayOf("String", fields[j], 40f, 40f + 20 * j * 1f))
    }
    var proportion = maxsum / 10
    if(proportion == 0){
        proportion = 1
    }
    proportion = normal(proportion)
    var y0 = fields.size * 20f + 220
    var x0 = 140f
    var dy = 420f
    var dx = 60f * data.size + 30f
    instructions.add(arrayOf("Line", x0, y0, x0, y0 + dy, paint.color))
    instructions.add(arrayOf("Line", x0, y0 + dy, x0 + dx, y0 + dy, paint.color))
    var stx = x0

    for(j in 1..10){
        instructions.add(arrayOf("Line", x0, y0 + dy - j * 40f, x0 - 5f, y0 + dy - j * 40f, paint.color))
        instructions.add(arrayOf("String", "${proportion * j}", x0 - 40f, y0 + dy - j * 40f))
        instructions.add(arrayOf("Line", x0, y0 + dy - j * 40f, x0 + dx, y0 + dy - j * 40f, paint.color))
    }
    for(i in data.indices) {
        stx += 30f
        var j = 0
        var curList = (data[i].values zip Array(data[i].values.size, {j++})).toMutableList()
        instructions.add(arrayOf("String", data[i].key, stx, y0 + dy + 10f))
        curList.sortBy { it.first; it.second }
        var sty = y0 + dy
        for (cur in curList) {
            var dy1 = cur.first * 1f / proportion * 40f
            instructions.add(arrayOf("Rect", stx, sty - dy1, 30f, dy1, arrayListPaints[cur.second].color))
            sty -= dy1
        }
        stx += 30f
    }
    return instructions
}
