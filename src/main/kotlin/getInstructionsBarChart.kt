import org.jetbrains.skija.Paint

fun getInstructionsBarChart(paint : Paint) : MutableList<Array<Any> >{
    var instructions : MutableList <Array <Any> > = mutableListOf()
    var maxx = data1.values[0]
    var sty = 100f
    var stx = 100f
    var endx = 500f
    for(i in 0 until data1.n){
        instructions.add(arrayOf("RectShader", stx, sty, stx + (endx - stx) * data1.values[i] / maxx, 20f, arrayListPaints[i + 16].color.toInt(), arrayListPaints[i].color.toInt()))
        instructions.add(arrayOf("String", data1.keys[i], stx, sty + 30f))
        sty += 40f
    }
    return instructions
}