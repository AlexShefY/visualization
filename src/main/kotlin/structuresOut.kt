import org.jetbrains.skija.Canvas
import org.jetbrains.skija.Font
import org.jetbrains.skija.Paint
import org.jetbrains.skija.Rect

/*
 * out point
 */
fun pointOut(instruction : Array<Any>, canvas : Canvas, paint : Paint, font : Font){
    canvas.drawPoint(instruction[1].toString().toFloat(), instruction[2].toString().toFloat(), paint)
}

/*
 * out arc
 */
fun ArcOut(instruction : Array<Any>, canvas : Canvas, paint : Paint, font : Font){
    canvas.drawArc(instruction[1].toString().toFloat(), instruction[2].toString().toFloat(), instruction[3].toString().toFloat(), instruction[4].toString().toFloat(),
        instruction[5].toString().toFloat(), instruction[6].toString().toFloat(), true, Paint().apply{color = instruction[7].toString().toInt()})
}

/*
 * out line
 */
fun lineOut(instruction : Array<Any>, canvas : Canvas, paint : Paint, font : Font) {
    var x1 = instruction[1].toString().toFloat()
    var y1 = instruction[2].toString().toFloat()
    var x2 = instruction[3].toString().toFloat()
    var y2 = instruction[4].toString().toFloat()
    var color1 = instruction[5].toString().toInt()
    canvas.drawLine(x1, y1, x2, y2, Paint().apply { color = color1 })
}

/*
 * out string
 */
fun stringOut(instruction : Array<Any>, canvas : Canvas, paint : Paint, font : Font){
    var str = instruction[1].toString()
    var x1 = instruction[2].toString().toFloat()
    var y1 = instruction[3].toString().toFloat()
    canvas.drawString(str, x1, y1, font, paint)
}

/*
 * out rectangle
 */
fun rectOut(instruction : Array<Any>, canvas : Canvas, paint : Paint, font : Font){
    var x1 = instruction[1].toString().toFloat()
    var y1 = instruction[2].toString().toFloat()
    var x2 = instruction[3].toString().toFloat()
    var y2 = instruction[4].toString().toFloat()
    var color1 = instruction[5].toString().toLong()
    canvas.drawRect(Rect.makeXYWH(x1, y1, x2, y2), Paint().apply{color = color1.toInt()})
}

/*
 * out rectangle colored with gradient
 */
fun rectShaderOut(instruction : Array<Any>, canvas : Canvas, paint : Paint, font : Font){
    var x1 = instruction[1].toString().toFloat()
    var y1 = instruction[2].toString().toFloat()
    var x2 = instruction[3].toString().toFloat()
    var y2 = instruction[4].toString().toFloat()
    var color1 = instruction[5].toString().toInt()
    var color2 = instruction[6].toString().toInt()
    var paint2 = paint
    paint2.shader = createShader(x1, x2,
        Paint().apply(){color = color1}.color, // MediumSpringGreen
        Paint().apply(){color = color2}.color)
    canvas.drawRect(Rect.makeXYWH(x1, y1, x2, y2), paint2)
}