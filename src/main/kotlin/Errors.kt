/*
 * Class for processing errors
 */
enum class Error{
    WRONGTYPE, INTINPUT, MATCHSIZEFIELDS, TWOFIELDS, EMPTYINPUT, FLOATINPUT
}

fun Errors(typeEr : Error){
    when(typeEr){
        Error.WRONGTYPE -> println("you entered the wrong command type\n")
        Error.INTINPUT -> println("there must be an integer input\n")
        Error.MATCHSIZEFIELDS -> println("the number of fields does not match the declared\n")
        Error.TWOFIELDS -> println("there should be two fields\n")
        Error.EMPTYINPUT -> println("empty input\n")
        Error.FLOATINPUT -> println("there must be a float input\n")
    }
}