import org.jetbrains.skija.Paint

fun paintAll() : Instruction{
    return Instruction(Type = "Rect", coordinates = floatArrayOf(0f, 0f, 1000f, 1000f), paints = arrayListOf(Paint().apply { color = greyColor } ))
}

fun printDescription(instructions: MutableList<Instruction>, stx : Float, sty : Float, dy : Float) {
    for(j in fields.indices){
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(stx, sty + dy * j,  10f, 10f), paints = arrayListOf(arrayListPaints[j])))
        instructions.add(Instruction(Type = "String", text = fields[j], coordinates = floatArrayOf(stx + 10f, sty + 10f + dy* j)))
    }
}

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