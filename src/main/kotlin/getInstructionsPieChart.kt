import org.jetbrains.skija.Paint
import kotlin.math.*


fun getInstructionsPieChart(paint : Paint) : MutableList<Instruction > {
    var instructions : MutableList <Instruction > = mutableListOf()
    var sum = data1.values.sum()
    var startAngle = -90f
    var startradius = 150f
    var endradius = 300f
    var pointx = 400f
    var pointy = 400f
    var i = 0
    instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(0f, 0f, 1000f, 1000f), paints = arrayListOf(Paint().apply{ color = greyColor})))
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
    instructions.add(Instruction(Type = "Arc", coordinates = floatArrayOf(pointx - 50f, pointy - 50f, pointx + 50f, pointy + 50f, -90f, 360f),
        paints = arrayListOf(Paint().apply { color = greyColor } )))
    instructions.add(Instruction(Type = "String", text = "All", coordinates = floatArrayOf(pointx - 5f, pointy)))
    instructions.add(Instruction(Type = "String", text = "$sum", coordinates = floatArrayOf(pointx - "$sum".length * 3f, pointy + 10f)))
    startAngle = -90f
    var leftx = 100f
    var lefty = 100f
    var rightx = 600f
    var righty = 100f
    i = 0
    for(v in data1.values){
        var angleTo = -startAngle - 180f * v / sum
        var centralX = pointx + startradius * cos(angleTo / 180f * PI) / 2
        var centralY = pointy - startradius * sin(angleTo / 180f * PI) / 2
        var percent = (v * 100 / sum).toInt()
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
        startAngle += 360f * v / sum
        i++
    }
    return instructions
}