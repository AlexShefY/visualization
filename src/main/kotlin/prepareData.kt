var data : ArrayList<classData> = arrayListOf()
var dataGraph : classGraph = classGraph()
var fields : List<String> = listOf()
var n = 0
var m = 0
var fileName = ""
var type : typesOfInput? = null

/*
 * Обработка ввода
 */
fun getTypeInput() : typesOfInput? {
    var s =readLine()!!.toString()
    return when(s){
        "clustered histogram" ->  typesOfInput.CLUSTEREDHISTOHRAM
        "stacked histogram" ->  typesOfInput.STACKEDHISTOHRAM
        "graph" ->  typesOfInput.GRAPH
        "bar chart" -> typesOfInput.BARCHART
        "pie chart" -> typesOfInput.PIECHART
        else -> null
    }
}

fun prepareClusteredHistohram(){
    n = readLine()!!.toInt()
    m = readLine()!!.toInt()
    fields = readLine()!!.split(' ')
    for (i in 0 until n) {
        var pair = readLine()!!.split(' ')
        var product = pair[0]
        var vec1 = pair.toMutableList()
        vec1.removeFirst()
        var vec = vec1.map { it.toInt() } // exception !!!!!!!!!!!!!!!!!!
        if (vec.size != m) {
            return
        }
        data.add(classData(product, vec.toMutableList()))
    }
}

fun prepareGraph() {
    fields = readLine()!!.split(' ')
    n = readLine()!!.toInt()
    dataGraph.n = n
    var list = mutableListOf<Pair<Float, Float>>()
    for (i in 0 until n) {
        var pair = readLine()!!.split(' ')
        var x = pair[0].toFloat() // FLOAT ?????
        var y = pair[1].toFloat() // exception !!!!!!!!!!!!!!!!!!!!!!!!
        list.add(Pair(x, y))
    }
    list.sortWith( compareBy({it.first}, {it.second}))
    for(point in list){
        dataGraph.xValues.add(point.first)
        dataGraph.yValues.add(point.second)
    }
}
