import java.awt.Color
import org.jetbrains.skija.*

import kotlin.math.*

fun getInstructionDistributionGraph(paint : Paint) : MutableList<Instruction>{
    var instructions : MutableList <Instruction> = mutableListOf()
    var maxx = dataDistributionGraph.xValues.maxOf { abs(it) }
    var maxy = dataDistributionGraph.yValues.maxOf { abs(it) }
    var delx = normal(maxx.toInt())
    var dely = normal(maxx.toInt())
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
    }
    var maxd = 0.0
    for(i in 0 until numberPix){
        for(j in 0 until numberPix){
            var xcur = (i - 300f) * 1f / 30 * delx + 0.05f
            var ycur = (300f - j) * 1f / 30 * dely + 0.05f
            for(x in 0 until n){
                var m1 = dataDistributionGraph.xValues[x]
                var m2 = dataDistributionGraph.yValues[x]
                dist[i][j] = dist[i][j] + 1f / (sqrt(2 * PI) * g1[x]) * E.pow(-0.5f * (dist(m1, m2, xcur, ycur).pow(4) / (g1[x] * g1[x])))
                maxd = max(maxd, dist[i][j])
            }
        }
    }
    var colorBlack = 0xff000000.toLong()
    for(i in 0 until numberPix){
        for(j in 0 until numberPix){
            var xgraph = i * 1f + 10f
            var ygraph = j * 1f + 10f
            dist[i][j] /= maxd
            var colorCurRed = (dist[i][j] * 0x8f + (1.0 -  dist[i][j]) * 0xff).toLong()
            var colorCurGreen = (dist[i][j] * 0x00 + (1.0 -  dist[i][j]) * 0xff).toLong()
            var colorCurBlue = (dist[i][j] * 0x00 + (1.0 -  dist[i][j]) * 0xff).toLong()
            var colorCur = 0xff000000 + colorCurRed * 0x10000 + colorCurGreen * 0x100 + colorCurBlue
            instructions.add(Instruction(Type = "Rect", coordinates = floatArrayOf(xgraph, ygraph,1f, 1f), paints = arrayListOf(Paint().apply { color = colorCur.toInt() })))
        }
    }
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(310f, 10f, 310f, 610f), paints = arrayListOf(Paint().apply { color = colorBlack.toInt() } )))
    instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(10f, 310f, 610f, 310f), paints = arrayListOf(Paint().apply { color = colorBlack.toInt() } )))
    for(i in -10..10) {
        if(i != 0) {
            instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(310f, 310f - i * 30f, 313f, 310f - i * 30f), paints = arrayListOf(Paint().apply{ color = colorBlack.toInt() } )))
            instructions.add(Instruction(Type = "Line", coordinates = floatArrayOf(310f + i * 30f, 310f, 310f + i * 30f, 313f), paints = arrayListOf(Paint().apply { color = colorBlack.toInt() } )))
            instructions.add(Instruction(Type = "String", text = "${i * delx}", coordinates = floatArrayOf(320f, 310f - i * 30f)))
            instructions.add(Instruction(Type = "String", text = "${i * dely}", coordinates = floatArrayOf(310f + i * 30f, 320f)))
        }
    }
    return instructions
}