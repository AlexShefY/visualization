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
import java.io.File.createTempFile
import java.io.IOException
import java.nio.channels.ByteChannel
import java.nio.ByteBuffer
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
     * Формирование инструкций вывода по входным данным
     */
    fun getInstructionsType1() : MutableList<Array<Any> >{
        var instructions : MutableList <Array <Any> > = mutableListOf()
        var propotion = 0
        var allsize = 0f
        for(i in 0 until n){
            allsize += max(data[i].values.size * 20f + 20f, 6.5f * data[i].key.length)
            for(value in data[i].values){
                if(value > propotion){
                    propotion = value
                }
            }
        }
        propotion = propotion / 10
        if(propotion == 0){
            propotion++
        }
        propotion = normal(propotion)
        for(j in 0 until fields.size){
            instructions.add(arrayOf("Rect", 30f, 30f + 20 * j * 1f,  10f, 10f, arrayListPaints[j].color.toInt()))
            instructions.add(arrayOf("String", fields[j], 40f, 40f + 20 * j * 1f))
        }
        var y0 = fields.size * 20f + 140
        var x0 = 140f
        instructions.add(arrayOf("Rect", x0 - 80f, y0 - 80f, allsize + 120f, 420f, 0xffd3dbe4.toInt()))
        instructions.add(arrayOf("Line", x0 , y0 - 20f, x0, y0 + 300f, paint.color.toInt()))
        instructions.add(arrayOf("Line", x0, y0 + 300f, x0 + allsize + 20f, y0 + 300f, paint.color.toInt()))
        for(j in 1..10){
            instructions.add(arrayOf("Line", x0, y0 + 300f - 20f * j, x0 - 5f, y0 + 300f - 20f * j, paint.color.toInt()))
            instructions.add(arrayOf("String", "${propotion * j}", x0 - 20f - 5f * "${propotion * j}".length, y0 + 300f -20f * j))
        }
        for(i in 0 until n){
            var j = 0
            instructions.add(arrayOf("String", data[i].key, x0, 320f + y0))
            for(value in data[i].values){
                var dy = value.toFloat() / propotion
                dy *= 20f
                instructions.add(arrayOf("Line", x0, 300f + y0, x0 + 20f, 300f + y0, arrayListPaints[j].color))
                instructions.add(arrayOf("Rect", x0, 300f + y0 - dy, 20f, dy, arrayListPaints[j].color))
                x0 += 20f
                j += 1
            }
            x0 += max(20f, data[i].key.length * 6.5f - data[i].values.size * 20f)
        }
        return instructions
    }
    /*
     * Вывод в окно согласно инструкциям
     */
    fun outWindow(instructions : MutableList<Array<Any>>, canvas : Canvas){
        for(instruction in instructions){
            when(instruction[0].toString()){
                "Rect" -> {
                    var x1 = instruction[1].toString().toFloatOrNull()
                    var y1 = instruction[2].toString().toFloatOrNull()
                    var x2 = instruction[3].toString().toFloatOrNull()
                    var y2 = instruction[4].toString().toFloatOrNull()
                    var color1 = instruction[5].toString().toIntOrNull()
                    if(x1 == null || y1 == null || x2 == null || y2 == null || color1 == null){
                        println("Error")
                        return
                    }
                    canvas.drawRect(Rect.makeXYWH(x1, y1, x2, y2), Paint().apply{color = color1})
                }
                "String" -> {
                    var str = instruction[1].toString()
                    var x1 = instruction[2].toString().toFloatOrNull()
                    var y1 = instruction[3].toString().toFloatOrNull()
                    if(x1 == null || y1 == null){
                       println("Error")
                        return
                    }
                    canvas.drawString(str, x1, y1, font1, paint)
                }
                "Line" -> {
                    var x1 = instruction[1].toString().toFloatOrNull()
                    var y1 = instruction[2].toString().toFloatOrNull()
                    var x2 = instruction[3].toString().toFloatOrNull()
                    var y2 = instruction[4].toString().toFloatOrNull()
                    var color1 = instruction[5].toString().toIntOrNull()
                    if(x1 == null || y1 == null || x2 == null || y2 == null || color1 == null){
                        println("Error")
                        return
                    }
                    canvas.drawLine(x1, y1, x2, y2, Paint().apply{color = color1})
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
        var instructions = getInstructionsType1()
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