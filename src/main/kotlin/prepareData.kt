
var data : MutableList<classData> = mutableListOf()
var dataGraph : classGraph = classGraph()
var fields : List<String> = listOf()
var n = 0
var m = 0
var fileName = ""
var type : typesOfInput? = null
var data1 : classData1 = classData1()
var dataDistributionGraph = classDistributionGraph()

/*
 * Input processing
 */
fun getTypeInput() : typesOfInput? {
    var s = lines[st++]
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

/*
 * get from input key and desired number of values
 */
fun getKeyValues(m : Int) : Pair<String, MutableList<Float>>?{
    var line = lines[st++].split(' ').toMutableList()
    if(line.isEmpty()){
        Errors(Error.EMPTYINPUT, st - 1)
        return null
    }
    var key = line[0]
    line.removeFirst()
    var values = line.map{
        if(it.toFloatOrNull() == null){
            Errors(Error.FLOATINPUT, st - 1)
            return null
        }
        it.toFloat()
    }.toMutableList()
    if(values.size != m){
        Errors(Error.MATCHSIZEFIELDS, st - 1)
        return null
    }
    return Pair(key, values)
}

/*
 * get from input pair floats
 */
fun getPairFloat() : Pair<Float, Float >?{
    var keyValues = getKeyValues(1) ?: return null
    var x = keyValues.first.toFloatOrNull()
    var y = keyValues.second[0]
    if(x == null){
        Errors(Error.FLOATINPUT, st - 1)
        return null
    }
    return Pair(x, y)
}

/*
 * get point on graphic from input
 */
fun getPoints() : Pair<Int, MutableList<Pair<Float, Float>>>?{
    var n = lines[st++].toIntOrNull()
    if(n == null){
        Errors(Error.INTINPUT, st)
        return null
    }
    var list = mutableListOf<Pair<Float, Float>>()
    for (i in 0 until n) {
        var pair = getPairFloat() ?: return null
        list.add(pair)
    }
    list.sortWith( compareBy({it.first}, {it.second}))
    return Pair(n, list)
}

/*
 * prepare data for clustered histogram or stacked histogram
 */

fun prepareClusteredHistogram() : Boolean{
    var nInput = lines[st++].toIntOrNull()
    var mInput = lines[st++].toIntOrNull()
    if(nInput == null || mInput == null){
        Errors(Error.INTINPUT, st - 1)
        return false
    }
    n = nInput
    m = mInput
    fields = lines[st++].split(' ')
    if(fields.size != m){
        Errors(Error.MATCHSIZEFIELDS, st - 1)
        return false
    }
    for(i in 0 until n){
        var element = getKeyValues(m) ?: return false
        data.add(classData(element.first, element.second))
    }
    return true
}

/*
 * prepare data for graph
 */

fun prepareGraph() : Boolean {
    fields = lines[st++].split(' ')
    if(fields.size != 2){
        Errors(Error.TWOFIELDS, st - 1)
        return false
    }
    var nInput = lines[st++].toIntOrNull()
    if(nInput == null){
        Errors(Error.INTINPUT, st - 1)
        return false
    }
    n = nInput
    dataGraph.n = nInput
    for(i in 0 until n){
        var name = lines[st++]
        if(name.isEmpty()){
            Errors(Error.EMPTYINPUT, st - 1)
            continue
        }
        dataGraph.names.add(name)
        var mList = getPoints() ?: return false
        dataGraph.xyValues.add(mList.second)
    }
    return true
}

/*
 * prepare data for pie chart or bar chart
 */
fun preparePieChart() : Boolean{
    var nInput = lines[st++].toIntOrNull()
    if(nInput == null){
        Errors(Error.INTINPUT, st - 1)
        return false
    }
    n = nInput
    data1.n = n
    var list : MutableList <Pair<Float, String> > = mutableListOf()
    for(i in 0 until n){
        val nameValue = getKeyValues(1) ?: return false
        list.add(Pair(nameValue.second[0], nameValue.first))
    }
    list.sortByDescending { it.first }
    for(v in list){
        data1.values.add(v.first)
        data1.keys.add(v.second)
    }
    return true
}

/*
 * prepare data for distribution graph
 */
fun prepareDistributionGraph() : Boolean{
    var nValues = getPoints() ?: return false
    n = nValues.first
    dataDistributionGraph.n = n
    nValues.second.forEach {
        dataDistributionGraph.xValues.add(it.first)
        dataDistributionGraph.yValues.add(it.second)
    }
    return true
}