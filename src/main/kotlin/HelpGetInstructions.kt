import org.jetbrains.skija.Paint

/*
 * paint all painting area in grey color
 */
fun paintAll() : Instruction{
    return Instruction(Type = "Rect", coordinates = floatArrayOf(0f, 0f, 1000f, 1000f), paints = arrayListOf(Paint().apply { color = greyColor } ))
}

/*
 * draw a description for clustered and stacked histogram
 * where each category is mapped to a color
 * of the line on the graph
 */
fun printDescription(instructions: MutableList<Instruction>, stx : Float, sty : Float, dy : Float) {
    for(j in fields.indices){
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(stx, sty + dy * j,  10f, 10f), paints = arrayListOf(arrayListPaints[j])))
        instructions.add(Instruction(Type = "String", text = fields[j], coordinates = floatArrayOf(stx + 10f, sty + 10f + dy* j)))
    }
}

/*
 * paint axes for graphic and stacked/clustered histogram
 */
fun lineInstructions(instructions : MutableList<Instruction>, paint : Paint, x0 : Float, y0 : Float, x1 : Float, y1 : Float, x2 : Float, y2 : Float){
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0, x1, y1), paints = arrayListOf(paint)))
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0, x2, y2), paints = arrayListOf(paint)))
}

/*
 * paint marks on histogram
 */
fun printMarksSimpleVersion(instructions: MutableList<Instruction>, x0 : Float, y0 : Float, dy : Float, proportion: Int, paint: Paint){
    for(j in 1..10){
        instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(x0, y0 - dy * j, x0 - 5f, y0 - dy * j), paints = arrayListOf(paint)))
        instructions.add(Instruction(Type = "String", text = "${proportion * j}", coordinates = floatArrayOf(x0 - dy - 5f * "${proportion * j}".length, y0 - dy * j)))
    }
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