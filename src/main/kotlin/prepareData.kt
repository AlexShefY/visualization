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
 * Input processing
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

fun prepareClusteredHistohram() : Boolean{
    var nInput = readLine()!!.toIntOrNull()
    var mInput = readLine()!!.toIntOrNull()
    if(nInput == null || mInput == null){
        Errors(Error.INTINPUT)
        return false
    }
    n = nInput
    m = mInput
    fields = readLine()!!.split(' ')
    if(fields.size != m){
        Errors(Error.MATCHSIZEFIELDS)
        return false
    }
    for(i in 0 until n){
        var pair = readLine()!!.split(' ')
        if(pair.isEmpty()){
            Errors(Error.EMPTYINPUT)
            return false
        }
        var product = pair[0]
        var vec1 = pair.toMutableList()
        vec1.removeFirst()
        var vec = vec1.map {
            if(it.toIntOrNull() == null){
                Errors(Error.INTINPUT)
                return false
            }
            it.toInt()
        }
        if (vec.size != m) {
            Errors(Error.MATCHSIZEFIELDS)
            return false
        }
        data.add(classData(product, vec.toMutableList()))
    }
    return true
}

fun prepareGraph() : Boolean {
    fields = readLine()!!.split(' ')
    if(fields.size != 2){
        Errors(Error.TWOFIELDS)
        return false
    }
    var nInput = readLine()!!.toIntOrNull()
    if(nInput == null){
        Errors(Error.INTINPUT)
        return false
    }
    n = nInput
    dataGraph.n = nInput
    for(i in 0 until n){
        var name = readLine()!!
        if(name.isEmpty()){
            Errors(Error.EMPTYINPUT)
            continue
        }
        dataGraph.names.add(name)
        var mInput = readLine()!!.toIntOrNull()
        if(mInput == null){
            Errors(Error.INTINPUT)
            return false
        }
        m = mInput
        var list = mutableListOf<Pair<Float, Float>>()
        for (i in 0 until m) {
            var pair = readLine()!!.split(' ')
            if(pair.size != 2){
                Errors(Error.MATCHSIZEFIELDS)
                return false
            }
            var x = pair[0].toFloatOrNull()
            var y = pair[1].toFloatOrNull()
            if(x == null || y === null){
                Errors(Error.FLOATINPUT)
                return false
            }
            list.add(Pair(x, y))
        }
        list.sortWith( compareBy({it.first}, {it.second}))
        dataGraph.xyValues.add(list)
    }
    return true
}

fun preparePieChart() : Boolean{
    var nInput = readLine()!!.toIntOrNull()
    if(nInput == null){
        Errors(Error.INTINPUT)
        return false
    }
    n = nInput
    data1.n = n
    var list : MutableList <Pair<Int, String> > = mutableListOf()
    for(i in 0 until n){
        var str = readLine()!!.split(' ')
        if(str.size != 2){
            Errors(Error.TWOFIELDS)
            return false
        }
        var name = str[0]
        var value = str[1].toIntOrNull() // exception !!!!!!!!!
        if(value == null){
            Errors(Error.INTINPUT)
            return false
        }
        list.add(Pair(value, name))
    }
    list.sortByDescending { it.first }
    for(v in list){
        data1.values.add(v.first)
        data1.keys.add(v.second)
    }
    return true
}

fun prepareDistributionGraph() : Boolean{
    var nInput = readLine()!!.toIntOrNull()
    if(nInput == null){
        Errors(Error.INTINPUT)
        return false
    }
    n = nInput
    dataDistributionGraph.n = n
    for(i in 0 until n){
        var str = readLine()!!.split(' ')
        if(str.size != 2){
            Errors(Error.TWOFIELDS)
            return false
        }
        var x = str[0].toFloatOrNull()
        var y = str[1].toFloatOrNull() // exception !!!!!!!!!
        if(x == null || y == null){
            Errors(Error.FLOATINPUT)
            return false
        }
        dataDistributionGraph.xValues.add(x)
        dataDistributionGraph.yValues.add(y)
    }
    return true
}