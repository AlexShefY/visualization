var data : ArrayList<classData> = arrayListOf()
var dataGraph : classGraph = classGraph()
var fields : List<String> = listOf()
var n = 0
var m = 0
var fileName = ""
var type : typesOfInput? = null
var data1 : classData1 = classData1()
var dataDistributionGraph = classDistributionGraph()

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
        "distribution graph" -> typesOfInput.DISTRIBUTIONGRAPH
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
    for(i in 0 until n) {
        m = readLine()!!.toInt() // exception !!!!!!!!!!!!!!!!!!!
        var name = readLine()!!
        dataGraph.names.add(name)
        var list = mutableListOf<Pair<Float, Float>>()
        for (i in 0 until m) {
            var pair = readLine()!!.split(' ')
            var x = pair[0].toFloat() // FLOAT ?????
            var y = pair[1].toFloat() // exception !!!!!!!!!!!!!!!!!!!!!!!!
            list.add(Pair(x, y))
        }
        list.sortWith( compareBy({it.first}, {it.second}))
        dataGraph.xyValues.add(list)
    }
}

fun preparePieChart(){
    n = readLine()!!.toInt()
    data1.n = n
    var list : MutableList <Pair<Int, String> > = mutableListOf()
    for(i in 0 until n){
        var str = readLine()!!.split(' ')
        if(str.size != 2){
            return
        }
        var name = str[0]
        var value = str[1].toIntOrNull() // exception !!!!!!!!!
        if(value == null){
            return
        }
        list.add(Pair(value, name))
    }
    list.sortByDescending { it.first }
    for(v in list){
        data1.values.add(v.first)
        data1.keys.add(v.second)
    }
}

fun prepareDistributionGraph(){
    n = readLine()!!.toInt()
    dataDistributionGraph.n = n
    for(i in 0 until n){
        var str = readLine()!!.split(' ')
        if(str.size != 2){
            return
        }
        var x = str[0].toFloatOrNull()
        var y = str[1].toFloatOrNull() // exception !!!!!!!!!
        if(x == null || y == null){
            return
        }
        dataDistributionGraph.xValues.add(x)
        dataDistributionGraph.yValues.add(y)
    }
}