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
import javax.swing.WindowConstants
var data : ArrayList<classData> = arrayListOf()
var n = 0
fun main() {
    n = readLine()!!.toInt()
    for(i in 0 until n) {
        var pair = readLine()!!.split(' ')
        var product = pair[0]
        var vec1 = pair.toMutableList()
        vec1.removeFirst()
        var vec = vec1.map {it.toInt()}
        data.add(classData(product, vec.toMutableList()))
    }
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

var arrayListPaints = arrayListOf(Paint().apply{color = 0xffff0000.toInt()},
    Paint().apply(){color = 0xffffff00.toInt()},
    Paint().apply(){color = 0xff0000ff.toInt()},
    Paint().apply(){color = 0xff00ff00.toInt()}
)

class Renderer(val layer: SkiaLayer): SkiaRenderer {
    val typeface = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf")
    val font = Font(typeface, 40f)
    val paint = Paint().apply {
        color = 0xff9BC730L.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }



    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        val contentScale = layer.contentScale
        canvas.scale(contentScale, contentScale)
        val w = (width / contentScale).toInt()
        val h = (height / contentScale).toInt()
        var propotion = 0
        var allsize = 0f
        for(i in 0 until n){
            allsize += data[i].values.size * 20f
            for(value in data[i].values){
                if(value > propotion){
                    propotion = value
                }
            }
        }
        allsize += (n - 1) * 20f
        propotion /= 10
        var y0 = 40f
        var x0 = 40f
        canvas.drawRect(Rect.makeXYWH(x0, y0, allsize, 260f), Paint().apply { color = 0xffd0d0d0.toInt()})
        for(i in 0 until n){
            var j = 0
            for(value in data[i].values){
                var dy = value.toFloat() / propotion
                dy *= 20f
                canvas.drawLine(x0, 300f, x0 + 20f, 300f, arrayListPaints[j])
                canvas.drawRect(Rect.makeXYWH(x0, 300f - dy, 20f, dy), arrayListPaints[j])
                x0 += 20f
                j += 1
            }
            x0 += 20f
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