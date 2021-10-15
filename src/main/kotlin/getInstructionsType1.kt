import org.jetbrains.skija.Paint
import kotlin.math.*
/*
 * Формирование инструкций вывода по входным данным
 */
fun getInstructionsType1(paint : Paint) : MutableList<Array<Any> >{
    var instructions : MutableList <Array <Any> > = mutableListOf()
    instructions.add(arrayOf("Rect", 0f, 0f, 1000f, 1000f, greyColor))
    var propotion = data.maxOf { it.values.maxOf {it} }.toInt() / 10
    var allsize = data.sumOf { max(it.values.size * 20f + 40f, 6.5f * it.key.length).toDouble() }
    if(propotion == 0){
        propotion++
    }
    propotion = normal(propotion)
    for(j in fields.indices){
        instructions.add(arrayOf("Rect", 30f, 30f + 20 * j * 1f,  10f, 10f, arrayListPaints[j].color.toInt()))
        instructions.add(arrayOf("String", fields[j], 40f, 40f + 20 * j * 1f))
    }
    var y0 = fields.size * 20f + 140
    var x0 = 140f
    instructions.add(arrayOf("Rect", 0f, 0f, 1000f, 1000f, greyColor))
    instructions.add(arrayOf("Line", x0 , y0 - 20f, x0, y0 + 300f, paint.color.toInt()))
    instructions.add(arrayOf("Line", x0, y0 + 300f, x0 + allsize + 20f, y0 + 300f, paint.color.toInt()))
    for(j in 1..10){
        instructions.add(arrayOf("Line", x0, y0 + 300f - 20f * j, x0 - 5f, y0 + 300f - 20f * j, paint.color.toInt()))
        instructions.add(arrayOf("String", "${propotion * j}", x0 - 20f - 5f * "${propotion * j}".length, y0 + 300f -20f * j))
    }
    for(i in 0 until n){
        var j = 0
        instructions.add(arrayOf("String", data[i].key, x0, 320f + y0))
        for(value in data[i].values){
            var dy = value.toFloat() / propotion
            dy *= 20f
            instructions.add(arrayOf("Line", x0, 300f + y0, x0 + 20f, 300f + y0, arrayListPaints[j].color))
            instructions.add(arrayOf("Rect", x0, 300f + y0 - dy, 20f, dy, arrayListPaints[j].color))
            x0 += 20f
            j += 1
        }
        x0 += max(40f, data[i].key.length * 6.5f - data[i].values.size * 20f)
    }
    return instructions
}