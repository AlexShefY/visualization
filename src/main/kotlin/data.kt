import org.jetbrains.skija.Canvas
import org.jetbrains.skija.Font
import org.jetbrains.skija.Paint

class classData(var key : String = "", var values : MutableList<Float> = mutableListOf()){

}

class classGraph(var n : Int = 0, var names : MutableList<String> = mutableListOf(), var xyValues : MutableList<MutableList<Pair<Float, Float> > > = mutableListOf()){

}

class classData1(var n : Int = 0, var keys : MutableList<String> = mutableListOf(), var values : MutableList<Float> = mutableListOf()){

}

class classDistributionGraph(var n : Int = 0, var xValues: MutableList<Float> = mutableListOf(), var yValues : MutableList<Float> = mutableListOf()){

}

class Instruction(var Type : String = "", var text : String = "", var coordinates : FloatArray = floatArrayOf(), var paints : ArrayList<Paint> = arrayListOf(), var font: Font = Font()){
}