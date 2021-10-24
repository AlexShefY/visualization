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
import java.io.File
import java.io.IOException
import java.nio.channels.ByteChannel
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import javax.swing.WindowConstants
import kotlin.io.path.*


var mapActionsType = mapOf(typesOfInput.CLUSTEREDHISTOHRAM to :: prepareClusteredHistogram,
typesOfInput.GRAPH to :: prepareGraph, typesOfInput.STACKEDHISTOHRAM to :: prepareClusteredHistogram,
typesOfInput.PIECHART to :: preparePieChart, typesOfInput.DISTRIBUTIONGRAPH to :: prepareDistributionGraph,
typesOfInput.BARCHART to :: preparePieChart)
var st = 0
var lines : MutableList<String> = mutableListOf()
fun main(args : Array<String>) {
    lines = File(args[0]).readText().split('\n').map{it.removeSuffix("\r")}.toMutableList()
    type = getTypeInput()
    if(type == null){
        Errors(Error.WRONGTYPE, st - 1)
        return
    }
    if(mapActionsType[type]?.let{it()} == false) {
        return
    }
    fileName = lines[st++]
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
fun createShader(stx : Float, endx: Float, paint1 : Paint, paint2: Paint): Shader? {
    val start = Point(stx, 0f)
    val end = Point(endx, 0f)
    val colors = intArrayOf(paint1.color, paint2.color)
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
    fun outWindow(instructions : MutableList<Instruction>, canvas : Canvas){
        for(instruction in instructions){
            var paint2 = when(instruction.Type){
                "RectShader" -> paint1
                else -> paint
            }
            instruction.paints.add(paint2)
            instruction.font = font1
            mapOutActions[instruction.Type]?.let{it(instruction, canvas)}
        }
    }
    /*
     * Output to file
     */
    fun outFile(instrucions: MutableList<Instruction>, canvas: Canvas){
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
        var instructions = mutableListOf<Instruction> ()
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