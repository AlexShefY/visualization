import java.awt.Color
import kotlin.math.*

fun normalizeX(x : Int) : Int{
    var x1 : Int = 1
    while(x1 * 10 <= x){
        x1 *= 10
    }
    var x2 = x1
    while(x2 <= x){
        x2 += x1
    }
    return x2
}
fun dist(x1 : Float, y1 : Float, x2 : Float, y2 : Float) : Float{
    return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
}
fun getInstructionDistributionGraph() : MutableList<Array<Any> >{
    var instructions : MutableList <Array <Any> > = mutableListOf()
    var maxx = 0f
    var maxy = 0f
    for(i in 0 until dataDistributionGraph.n){
        maxx = max(maxx, abs(dataDistributionGraph.xValues[i]))
        maxy = max(maxy, abs(dataDistributionGraph.yValues[i]))
    }
    var delx = normalizeX((maxx / 10).toInt())
    var dely = normalizeX((maxy / 10).toInt())
    var numberPix = 600
    var dist : Array<Array<Double> > = Array(numberPix) {Array(numberPix) {0.0} }
    n = dataDistributionGraph.n
    var g1 : Array<Double> = Array(n) {0.0}
    for(x in 0 until n){
        var m1 = dataDistributionGraph.xValues[x]
        var m2 = dataDistributionGraph.yValues[x]
        for(y in 0 until n){
            var d = dist(dataDistributionGraph.xValues[y], dataDistributionGraph.yValues[y], m1, m2)
            g1[x] = g1[x] + d * d
        }
        g1[x] = sqrt(g1[x] / (n))
        println(g1[x])
    }
    var maxd = 0.0
    for(i in 0 until numberPix){
        for(j in 0 until numberPix){
            var xcur = (i - 300f) / 30 * delx + 0.05f
            var ycur = (300f - j) / 30 * dely + 0.05f
            for(x in 0 until n){
                var m1 = dataDistributionGraph.xValues[x]
                var m2 = dataDistributionGraph.yValues[x]
                dist[i][j] = dist[i][j] + 1f / (sqrt(2 * PI) * g1[x]) * E.pow(-0.5f * (dist(m1, m2, xcur, ycur).pow(4) / (g1[x] * g1[x])))
                maxd = max(maxd, dist[i][j])
            }
        }
    }
    var colorBlack = 0xff000000.toLong()
    var colorWhite = 0xffffffff.toLong()
    for(i in 0 until numberPix){
        for(j in 0 until numberPix){
            var xgraph = i * 1f + 10f
            var ygraph = j * 1f + 10f
            dist[i][j] /= maxd
            var colorCurRed = (dist[i][j] * 0x8f + (1.0 -  dist[i][j]) * 0xff).toLong()
            var colorCurGreen = (dist[i][j] * 0x00 + (1.0 -  dist[i][j]) * 0xff).toLong()
            var colorCurBlue = (dist[i][j] * 0x00 + (1.0 -  dist[i][j]) * 0xff).toLong()
            var colorCur = 0xff000000 + colorCurRed * 0x10000 + colorCurGreen * 0x100 + colorCurBlue
            instructions.add(arrayOf("Rect", xgraph, ygraph, 1f,  1f, colorCur))
        }
    }
    instructions.add(arrayOf("Line", 310f, 10f, 310f, 610f, colorBlack.toInt()))
    instructions.add(arrayOf("Line", 10f, 310f, 610f, 310f, colorBlack.toInt()))
    return instructions
}