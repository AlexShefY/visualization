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


var mapActionsType = mapOf(typesOfInput.CLUSTEREDHISTOHRAM to :: prepareClusteredHistohram,
typesOfInput.GRAPH to :: prepareGraph, typesOfInput.STACKEDHISTOHRAM to :: prepareClusteredHistohram,
typesOfInput.PIECHART to :: preparePieChart, typesOfInput.DISTRIBUTIONGRAPH to :: prepareDistributionGraph)

fun main() {
    type = getTypeInput()
    if(type == null){
        println("Error")
        return
    }
    mapActionsType[type]?.let{it()}
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

/*
 * create shader to gradient fill function
 */
fun createShader(stx : Float, endx: Float, color1 : Int, color2 : Int): Shader? {
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
    var canvas1: Canvas = surface.canvas
    val font1 = Font(typeface, 10f)
    val paint1 = Paint()
    val paint = Paint().apply {
        color = 0xff000000.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }

    /*
     * output to the window according to the instructions
     */
    var mapOutActions = mapOf("Rect" to :: rectOut, "RectShader" to :: rectShaderOut,
    "String" to :: stringOut, "Point" to :: pointOut, "Arc" to :: ArcOut, "Line" to :: lineOut)
    fun outWindow(instructions : MutableList<Array<Any>>, canvas : Canvas){
        for(instruction in instructions){
            var paint2 = when(instruction[0].toString()){
                "RectShader" -> paint1
                else -> paint
            }
            mapOutActions[instruction[0].toString()]?.let{it(instruction, canvas, paint2, font1)}
        }
    }
    /*
     * Output to file
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
     * print the histogram
     */
    var mapGetInstruction = mapOf(typesOfInput.CLUSTEREDHISTOHRAM to :: getInstructionsType1,
    typesOfInput.GRAPH to :: getInstructionsTypeGraph, typesOfInput.STACKEDHISTOHRAM to :: getInstructionsStackedHistohram,
    typesOfInput.PIECHART to :: getInstructionsPieChart, typesOfInput.BARCHART to :: getInstructionsBarChart,
    typesOfInput.DISTRIBUTIONGRAPH to :: getInstructionDistributionGraph)

    fun printDiagram(canvas : Canvas, width: Int, height: Int, nanoTime: Long){
        var instructions = mutableListOf<Array<Any>> ()
        mapGetInstruction[type]?.let{instructions = it(paint)}
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