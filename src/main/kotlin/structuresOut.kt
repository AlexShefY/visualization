import org.jetbrains.skija.Canvas
import org.jetbrains.skija.Font
import org.jetbrains.skija.Paint
import org.jetbrains.skija.Rect



fun pointOut(instruction: Array<Any>, canvas: Canvas, paint : Paint){
    canvas.drawPoint(instruction[1].toString().toFloat(), instruction[2].toString().toFloat(), paint)
}

fun ArcOut(instruction : Array<Any>, canvas : Canvas, paint: Paint){
    canvas.drawArc(instruction[1].toString().toFloat(), instruction[2].toString().toFloat(), instruction[3].toString().toFloat(), instruction[4].toString().toFloat(),
        instruction[5].toString().toFloat(), instruction[6].toString().toFloat(), true, Paint().apply{color = instruction[7].toString().toInt()})
}
/*
 * Выводим линию по инструкции
 */
fun lineOut(instruction: Array<Any>, canvas: Canvas) {
    var x1 = instruction[1].toString().toFloatOrNull()
    var y1 = instruction[2].toString().toFloatOrNull()
    var x2 = instruction[3].toString().toFloatOrNull()
    var y2 = instruction[4].toString().toFloatOrNull()
    var color1 = instruction[5].toString().toIntOrNull()
    if (x1 == null || y1 == null || x2 == null || y2 == null || color1 == null) {
        println("Error")
        return
    }
    canvas.drawLine(x1, y1, x2, y2, Paint().apply { color = color1 })
}

/*
 * Выводим строку по инструкции
 */
fun stringOut(instruction : Array<Any>, canvas : Canvas, font1: Font, paint: Paint){
    var str = instruction[1].toString()
    var x1 = instruction[2].toString().toFloatOrNull()
    var y1 = instruction[3].toString().toFloatOrNull()
    if(x1 == null || y1 == null){
        println("Error")
        return
    }
    canvas.drawString(str, x1, y1, font1, paint)
}

/*
 * Вывод прямоугольника по инструкции
 */
fun rectOut(instruction : Array<Any>, canvas : Canvas){
    var x1 = instruction[1].toString().toFloatOrNull()
    var y1 = instruction[2].toString().toFloatOrNull()
    var x2 = instruction[3].toString().toFloatOrNull()
    var y2 = instruction[4].toString().toFloatOrNull()
    var color1 = instruction[5].toString().toLongOrNull()
    if(x1 == null || y1 == null || x2 == null || y2 == null || color1 == null){
        println("Error")
        return
    }
    canvas.drawRect(Rect.makeXYWH(x1, y1, x2, y2), Paint().apply{color = color1.toInt()})
}

fun rectShaderOut(instruction : Array<Any>, canvas : Canvas, paint1 : Paint){
    var x1 = instruction[1].toString().toFloatOrNull()
    var y1 = instruction[2].toString().toFloatOrNull()
    var x2 = instruction[3].toString().toFloatOrNull()
    var y2 = instruction[4].toString().toFloatOrNull()
    var color1 = instruction[5].toString().toIntOrNull()
    var color2 = instruction[6].toString().toIntOrNull()
    if(x1 == null || y1 == null || x2 == null || y2 == null || color1 == null || color2 == null){
        println("Error")
        return
    }
    var paint2 = paint1
    paint2.shader = createShader(x1, x2,
        Paint().apply(){color = color1}.color, // MediumSpringGreen
        Paint().apply(){color = color2}.color)
    canvas.drawRect(Rect.makeXYWH(x1, y1, x2, y2), paint2)
}