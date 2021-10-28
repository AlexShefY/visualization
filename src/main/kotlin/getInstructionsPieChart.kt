import org.jetbrains.skija.Paint
import kotlin.math.*


fun paintArcs(instructions: MutableList<Instruction>, sum : Float,  pointx : Float, pointy : Float, startradius : Float, endradius : Float){
    var startAngle = -90f
    var i = 0
    for(v in data1.values){
        var radius = startradius + (endradius - startradius) * v / sum
        instructions.add(Instruction(Type = "Arc", coordinates = floatArrayOf(pointx - radius, pointy - radius, pointx + radius, pointy + radius, startAngle, 360f * v / sum),
            paints = arrayListOf(arrayListPaints[i])))
        instructions.add(Instruction(Type = "Arc", coordinates = floatArrayOf(pointx - startradius, pointy - startradius, pointx + startradius, pointy + startradius, startAngle, 360f * v / sum),
            paints = arrayListOf(Paint().apply { color = whiteColor })))
        instructions.add(Instruction(Type = "Arc", coordinates = floatArrayOf(pointx - startradius, pointy - startradius, pointx + startradius, pointy + startradius, startAngle, 360f * v / sum),
            paints = arrayListOf(arrayListPaints[i + 8])))
        startAngle += 360f * v / sum
        i++
    }
}

var leftx = 100f
var lefty = 100f
var rightx = 600f
var righty = 100f

fun paintOneSector(instructions: MutableList<Instruction>, angleTo : Float, centralX : Double, centralY : Double, percent : Int, i : Int){
    instructions.add(Instruction(Type = "String", text = "$percent%", coordinates = floatArrayOf(centralX.toFloat(), centralY.toFloat())))
    if(angleTo < -90f || angleTo > 90f){
        instructions.add(Instruction(Type = "String", text = data1.keys[i], coordinates = floatArrayOf(leftx, lefty)))
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(leftx - 10f, lefty - 10f, 10f, 10f), paints = arrayListOf(arrayListPaints[i])))
        lefty += 40f
    }
    else{
        instructions.add(Instruction(Type = "String", text = data1.keys[i], coordinates = floatArrayOf(rightx, righty)))
        instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(rightx - 10f, righty - 10f, 10f, 10f), paints = arrayListOf(arrayListPaints[i])))
        righty += 40f
    }
}

fun paintCaptions(instructions: MutableList<Instruction>, sum : Float, pointx: Float, pointy: Float, startradius: Float){
    var i = 0
    var startAngle = -90f
    for(v in data1.values){
        var angleTo = -startAngle - 180f * v / sum
        var centralX = pointx + startradius * cos(angleTo / 180f * PI) / 2
        var centralY = pointy - startradius * sin(angleTo / 180f * PI) / 2
        var percent = (v * 100 / sum).toInt()
        paintOneSector(instructions, angleTo, centralX, centralY, percent, i)
        startAngle += 360f * v / sum
        i++
    }
}

fun paintCenter(instructions: MutableList<Instruction>, sum : Float, pointx : Float, pointy: Float){
    instructions.add(Instruction(Type = "Arc", coordinates = floatArrayOf(pointx - 50f, pointy - 50f, pointx + 50f, pointy + 50f, -90f, 360f),
        paints = arrayListOf(Paint().apply { color = greyColor } )))
    instructions.add(Instruction(Type = "String", text = "All", coordinates = floatArrayOf(pointx - 5f, pointy)))
    instructions.add(Instruction(Type = "String", text = "$sum", coordinates = floatArrayOf(pointx - "$sum".length * 3f, pointy + 10f)))
}

fun getInstructionsPieChart(paint : Paint) : MutableList<Instruction > {
    var instructions : MutableList <Instruction > = mutableListOf()
    var sum = data1.values.sum()
    var startradius = 150f
    var endradius = 300f
    var pointx = 400f
    var pointy = 400f
    instructions.add(paintAll())
    paintArcs(instructions, sum, pointx, pointy, startradius, endradius)
    paintCaptions(instructions, sum, pointx, pointy, startradius)
    paintCenter(instructions, sum, pointx, pointy)
    return instructions
}