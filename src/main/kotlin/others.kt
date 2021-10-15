import kotlin.math.sqrt

/*
 * select the most appropriate division for the histogram
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
    if(p != a){
        p += p1 / 2
    }
    return p
}

fun max(a : Float, b : Float) : Float{
    if(a > b){
        return a
    }
    return b
}

fun dist(x1 : Float, y1 : Float, x2 : Float, y2 : Float) : Float{
    return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
}