import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import org.jetbrains.skija.*
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkiaRenderer
import org.jetbrains.skiko.SkiaWindow
import java.awt.Color
import java.awt.Dimension
import java.awt.LinearGradientPaint
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.geom.Point2D
import java.io.IOException
import java.nio.channels.ByteChannel
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import javax.swing.WindowConstants
import kotlin.io.path.*


fun main() {
    type = getTypeInput()
    if(type == null){
        println("Error")
        return
    }
    when(type){
        typesOfInput.CLUSTEREDHISTOHRAM -> prepareClusteredHistohram()
        typesOfInput.GRAPH -> prepareGraph()
        typesOfInput.STACKEDHISTOHRAM -> prepareClusteredHistohram()
        typesOfInput.PIECHART -> preparePieChart()
        typesOfInput.BARCHART -> preparePieChart()
        typesOfInput.DISTRIBUTIONGRAPH -> prepareDistributionGraph()
    }
    fileName = readLine()!!
    createWindow("pf-2021-viz")
}

fun createWindow(title: String) = runBlocking(Dispatchers.Swing) {
    val window = SkiaWindow()
    window.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    window.title = title

    window.layer.renderer = Renderer(window.layer)
    window.layer.addMouseMotionListener(MyMouseMotionAdapter)

    window.preferredSize = Dimension(800, 600)
    window.minimumSize = Dimension(100,100)
    window.pack()
    window.layer.awaitRedraw()
    window.isVisible = true
}

var arrayListPaints = arrayListOf(
    Paint().apply(){color = 0xffDC143C.toInt()}, // Crimson
    Paint().apply(){color = 0xff2E8B57.toInt()}, // green
    Paint().apply(){color = 0xff2F4F4F.toInt()}, // green 1
    Paint().apply(){color = 0xffFF4500.toInt()}, // OrangeRed
    Paint().apply(){color = 0xff00FFFF.toInt()}, // Aqua
    Paint().apply(){color = 0xff00FA9A.toInt()}, // MediumSpringGreen
    Paint().apply(){color = 0xff8B008B.toInt()}, // DarkMagenta
    Paint().apply(){color = 0xff6A5ACD.toInt()}, //StateBlue
    Paint().apply(){color = 0xAfDC143C.toInt()}, // Crimson
    Paint().apply(){color = 0xAf2E8B57.toInt()}, // green
    Paint().apply(){color = 0xAf2F4F4F.toInt()}, // green 1
    Paint().apply(){color = 0xAfFF4500.toInt()}, // OrangeRed
    Paint().apply(){color = 0xAf00FFFF.toInt()}, // Aqua
    Paint().apply(){color = 0xAf00FA9A.toInt()}, // MediumSpringGreen
    Paint().apply(){color = 0xAf8B008B.toInt()}, // DarkMagenta
    Paint().apply(){color = 0xAf6A5ACD.toInt()},//StateBlue
    Paint().apply(){color = 0x4fDC143C.toInt()}, // Crimson
    Paint().apply(){color = 0x4f2E8B57.toInt()}, // green
    Paint().apply(){color = 0x4f2F4F4F.toInt()}, // green 1
    Paint().apply(){color = 0x4fFF4500.toInt()}, // OrangeRed
    Paint().apply(){color = 0x4f00FFFF.toInt()}, // Aqua
    Paint().apply(){color = 0x4f00FA9A.toInt()}, // MediumSpringGreen
    Paint().apply(){color = 0x4f8B008B.toInt()}, // DarkMagenta
    Paint().apply(){color = 0x4f6A5ACD.toInt()} //StateBlue
)
/*
 * Подбираем наиболее красивое деление для гистограммы
 */
fun normal(a : Int) : Int{
    var p = 1
    while(p * 10 <= a){
        p *= 10
    }
    var t = 1
    while((t + 1) * p <= a){
        t++
    }
    var p1 = p
    p *= t
    if(p1 != 1){
        p1 /= 10
        while((p + p1) <= a){
            p += p1
        }
    }
    return p
}
fun max(a : Float, b : Float) : Float{
    if(a > b){
        return a
    }
    return b
}

/*
 * create shader to gradient fill function
 */
fun createShader(stx : Float, endx : Float, color1 : Int, color2 : Int): Shader? {
    val start = Point(stx, 0f)
    val end = Point(endx, 0f)
    val colors = intArrayOf(
        Paint().apply(){color = color1}.color, // MediumSpringGreen
        Paint().apply(){color = color2}.color)
    return Shader.makeLinearGradient(start, end, colors)
}
class Renderer(val layer: SkiaLayer): SkiaRenderer {
    val typeface = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf")
    var surface: Surface = Surface.makeRasterN32Premul(1000, 1000)
    var canvas1: Canvas = surface.getCanvas()
    val font = Font(typeface, 20f)
    val font1 = Font(typeface, 10f)
    val paint = Paint().apply {
        color = 0xff000000.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }

    val paint1 = Paint()
    /*
     * Вывод в окно согласно инструкциям
     */
    fun outWindow(instructions : MutableList<Array<Any>>, canvas : Canvas){
        for(instruction in instructions){
            when(instruction[0].toString()){
                "Rect" -> {
                    rectOut(instruction, canvas)
                }
                "RectShader" ->{
                    rectShaderOut(instruction, canvas, paint1)
                }
                "String" -> {
                    stringOut(instruction, canvas, font1, paint)
                }
                "Line" -> {
                    lineOut(instruction, canvas)
                }
                "Point" -> {
                    pointOut(instruction, canvas, paint)
                }
                "Arc" -> {
                    ArcOut(instruction, canvas, paint)
                }
            }
        }
    }
    /*
     * Вывод в файл
     */
    fun outFile(instrucions: MutableList<Array<Any>>, canvas: Canvas){
        outWindow(instrucions, canvas)
        val image = surface.makeImageSnapshot()
        val pngData = image.encodeToData(EncodedImageFormat.JPEG)
        val pngBytes = pngData!!.toByteBuffer()
        try {
            val path: Path = Path(fileName)
            val channel: ByteChannel = newByteChannel(
                path,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
            )
            channel.write(pngBytes)
            channel.close()
        } catch (e: IOException) {
            println(e)
        }
    }
    /*
     * Отдельная функция для первого типа
    */
    fun printDiagram(canvas : Canvas, width: Int, height: Int, nanoTime: Long){
        var instructions = when(type) {
            typesOfInput.CLUSTEREDHISTOHRAM -> getInstructionsType1(paint)
            typesOfInput.GRAPH -> getInstructionsTypeGraph(paint)
            typesOfInput.STACKEDHISTOHRAM -> getInstructionsStackedHistohram(paint)
            typesOfInput.PIECHART -> getInstructionsPieChart(paint)
            typesOfInput.BARCHART -> getInstructionsBarChart(paint)
            typesOfInput.DISTRIBUTIONGRAPH -> getInstructionDistributionGraph()
            else -> mutableListOf()
        }
        outWindow(instructions, canvas)
        outFile(instructions, canvas1)
    }

    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        val contentScale = layer.contentScale
        canvas.scale(contentScale, contentScale)
        val w = (width / contentScale).toInt()
        val h = (height / contentScale).toInt()
        printDiagram(canvas, width, height, nanoTime)
        layer.needRedraw()
    }
}

object State {
    var mouseX = 0f
    var mouseY = 0f
}

object MyMouseMotionAdapter : MouseMotionAdapter() {
    override fun mouseMoved(event: MouseEvent) {
        State.mouseX = event.x.toFloat()
        State.mouseY = event.y.toFloat()
    }
}