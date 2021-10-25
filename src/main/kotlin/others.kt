import kotlin.math.sqrt

/*
 * select the most appropriate division for the histogram
 */
fun normal(a : Int) : Int{
    var a1 = a / 10
    if(a1 == 0){
        a1 = 1
    }
    var p = 1
    while(p * 10 <= a1){
        p *= 10
    }
    var t = 1
    while((t + 1) * p <= a1){
        t++
    }
    var p1 = p
    p *= t
    if(p != a1){
        p += p1 / 2
    }
    return p
}

fun dist(x1 : Float, y1 : Float, x2 : Float, y2 : Float) : Float{
    return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
}