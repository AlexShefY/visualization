import org.jetbrains.skija.Paint
import kotlin.math.*

fun getInstructionsPieChart(paint : Paint) : MutableList<Array<Any> > {
    var instructions : MutableList <Array <Any> > = mutableListOf()
    var sum = data1.values.sum()
    var startAngle = -90f
    var startradius = 150f
    var endradius = 300f
    var pointx = 400f
    var pointy = 400f
    var i = 0
    for(v in data1.values){
        var radius = startradius + (endradius - startradius) * v / sum
        instructions.add(arrayOf("Arc", pointx - radius, pointy - radius, pointx + radius, pointy + radius, startAngle, 360f * v / sum, arrayListPaints[i].color))
        instructions.add(arrayOf("Arc", pointx - startradius, pointy - startradius, pointx + startradius, pointy + startradius, startAngle, 360f * v / sum, 0xffffffff.toInt()))
        instructions.add(arrayOf("Arc", pointx - startradius, pointy - startradius, pointx + startradius, pointy + startradius, startAngle, 360f * v / sum, arrayListPaints[i + 8].color))
        startAngle += 360f * v / sum
        i++
    }
    instructions.add(arrayOf("Arc", pointx - 50f, pointy - 50f, pointx + 50f, pointy + 50f, -90f, 360f, greyColor))
    instructions.add(arrayOf("String", "All", pointx - 5f, pointy))
    instructions.add(arrayOf("String", "$sum", pointx - "$sum".length * 3f, pointy + 10f))
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
        var percent = v * 100 / sum
        instructions.add(arrayOf("String", "$percent%", centralX, centralY))
        if(angleTo < -90f || angleTo > 90f){
            instructions.add(arrayOf("String", data1.keys[i], leftx, lefty))
            instructions.add(arrayOf("Rect", leftx - 10f, lefty - 10f, 10f, 10f, arrayListPaints[i].color))
            lefty += 40f
        }
        else{
            instructions.add(arrayOf("String", data1.keys[i], rightx, righty))
            instructions.add(arrayOf("Rect", rightx - 10f, righty - 10f, 10f, 10f, arrayListPaints[i].color))
            righty += 40f
        }
        startAngle += 360f * v / sum
        i++
    }
    return instructions
}