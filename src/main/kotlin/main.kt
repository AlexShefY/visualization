import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import org.jetbrains.skija.*
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkiaRenderer
import org.jetbrains.skiko.SkiaWindow
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.io.IOException
import java.nio.channels.ByteChannel
import java.nio.file.Files.*
import java.nio.file.Path
import kotlin.io.path.*
import java.nio.file.StandardOpenOption
import javax.swing.WindowConstants
import java.nio.file.Files.newByteChannel as newByteChannel


var data : ArrayList<classData> = arrayListOf()
var fields : List<String> = listOf()
var n = 0
var m = 0
var fileName = ""
var type : typesOfInput? = null
/*
 * Обработка ввода
 */
fun getTypeInput() : typesOfInput? {
    return when(readLine()!!){
        "clustered histogram" ->  typesOfInput.CLUSTEREDHISTOHRAM
        "stacked histogram" ->  typesOfInput.STACKEDHISTOHRAM
        "graph" ->  typesOfInput.GRAPH
        "bar chart" -> typesOfInput.BARCHART
        "pie chart" -> typesOfInput.PIECHART
        else -> null
    }
}
fun main() {
    type = getTypeInput()
    if (type == typesOfInput.CLUSTEREDHISTOHRAM) {
        n = readLine()!!.toInt()
        m = readLine()!!.toInt()
        fields = readLine()!!.split(' ')
        for (i in 0 until n) {
            var pair = readLine()!!.split(' ')
            var product = pair[0]
            var vec1 = pair.toMutableList()
            vec1.removeFirst()
            var vec = vec1.map { it.toInt() }
            if (vec.size != m) {
                return
            }
            data.add(classData(product, vec.toMutableList()))
        }
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
    Paint().apply(){color = 0xff00FA9A.toInt()}, // MediumSpringGreen
    Paint().apply(){color = 0xffFF4500.toInt()}, // OrangeRed
    Paint().apply(){color = 0xff00FFFF.toInt()}, // Aqua
    Paint().apply(){color = 0xff8B008B.toInt()}, // DarkMagenta
    Paint().apply(){color = 0xff6A5ACD.toInt()} //StateBlue
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
    /*
     * Вывод в окно согласно инструкциям
     */
    fun outWindow(instructions : MutableList<Array<Any>>, canvas : Canvas){
        for(instruction in instructions){
            when(instruction[0].toString()){
                "Rect" -> {
                    rectOut(instruction, canvas)
                }
                "String" -> {
                    stringOut(instruction, canvas, font1, paint)
                }
                "Line" -> {
                    lineOut(instruction, canvas)
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
        val pngData = image.encodeToData(EncodedImageFormat.PNG)
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
    fun printDiagramType1(canvas : Canvas, width: Int, height: Int, nanoTime: Long){
        var instructions = getInstructionsType1(paint)
        outWindow(instructions, canvas)
        outFile(instructions, canvas1)
    }

    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        val contentScale = layer.contentScale
        canvas.scale(contentScale, contentScale)
        val w = (width / contentScale).toInt()
        val h = (height / contentScale).toInt()
        when(type) {
            typesOfInput.CLUSTEREDHISTOHRAM -> printDiagramType1(canvas, width, height, nanoTime)
        }
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