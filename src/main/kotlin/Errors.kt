/*
 * Class for processing errors
 */
enum class Error{
    WRONGTYPE, INTINPUT, MATCHSIZEFIELDS, TWOFIELDS, EMPTYINPUT, FLOATINPUT
}

fun Errors(typeEr : Error, line : Int){
    when(typeEr){
        Error.WRONGTYPE -> println("you entered the wrong command type in ${line + 1} line\n")
        Error.INTINPUT -> println("there must be an integer input in ${line + 1} line\n")
        Error.MATCHSIZEFIELDS -> println("the number of fields does not match the declared in ${line + 1} line\n")
        Error.TWOFIELDS -> println("there should be two fields in ${line + 1} line\n")
        Error.EMPTYINPUT -> println("empty input in ${line + 1} line\n")
        Error.FLOATINPUT -> println("there must be a float input in ${line + 1} line\n")
    }
}