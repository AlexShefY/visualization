import org.jetbrains.skija.Canvas
import org.jetbrains.skija.Font
import org.jetbrains.skija.Paint
import org.jetbrains.skija.Rect

/*
 * out point
 */
fun pointOut(instruction : Instruction, canvas : Canvas){
    canvas.drawPoint(instruction.coordinates[0], instruction.coordinates[1], instruction.paints[0])
}

/*
 * out arc
 */
fun ArcOut(instruction : Instruction, canvas : Canvas){
    canvas.drawArc(instruction.coordinates[0], instruction.coordinates[1], instruction.coordinates[2], instruction.coordinates[3],
        instruction.coordinates[4], instruction.coordinates[5], true, instruction.paints[0])
}

/*
 * out line
 */
fun lineOut(instruction : Instruction, canvas : Canvas) {
    var x1 = instruction.coordinates[0]
    var y1 = instruction.coordinates[1]
    var x2 = instruction.coordinates[2]
    var y2 = instruction.coordinates[3]
    var paint = instruction.paints[0]
    canvas.drawLine(x1, y1, x2, y2, paint)
}

/*
 * out string
 */
fun stringOut(instruction : Instruction, canvas : Canvas){
    var str = instruction.text
    var x1 = instruction.coordinates[0]
    var y1 = instruction.coordinates[1]
    var font = instruction.font
    var paint = instruction.paints[0]
    canvas.drawString(str, x1, y1, font, paint)
}

/*
 * out rectangle
 */
fun rectOut(instruction : Instruction, canvas : Canvas){
    var x1 = instruction.coordinates[0]
    var y1 = instruction.coordinates[1]
    var x2 = instruction.coordinates[2]
    var y2 = instruction.coordinates[3]
    var paint = instruction.paints[0]
    canvas.drawRect(Rect.makeXYWH(x1, y1, x2, y2), paint)
}

/*
 * out rectangle colored with gradient
 */
fun rectShaderOut(instruction : Instruction, canvas : Canvas){
    var x1 = instruction.coordinates[0]
    var y1 = instruction.coordinates[1]
    var x2 = instruction.coordinates[2]
    var y2 = instruction.coordinates[3]
    var paint1 = instruction.paints[0]
    var paint2 = instruction.paints[1]
    var paint3 = instruction.paints[2]
    paint3.shader = createShader(x1, x2,
        paint2, // MediumSpringGreen
        paint1)
    canvas.drawRect(Rect.makeXYWH(x1, y1, x2, y2), paint3)
}