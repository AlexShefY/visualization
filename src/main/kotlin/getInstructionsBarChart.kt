import org.jetbrains.skija.Paint

/*
 * in this file we form instructions for displaying the bar chart
 */
fun getInstructionsBarChart(paint : Paint) : MutableList<Instruction>{
    var instructions : MutableList <Instruction> = mutableListOf()
    var maxx = data1.values[0]
    var sty = 100f
    var stx = 100f
    var endx = 500f
    instructions.add(paintAll())
    for(i in 0 until data1.n){
        instructions.add(Instruction(Type ="RectShader", coordinates = floatArrayOf(stx, sty, stx + (endx - stx) * data1.values[i] / maxx, 20f),
            paints = arrayListOf(arrayListPaints[i + 16], arrayListPaints[i])))
        instructions.add(Instruction(Type = "String", text = data1.keys[i], coordinates = floatArrayOf(stx, sty + 30f)))
        sty += 40f
    }
    return instructions
}