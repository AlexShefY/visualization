import org.jetbrains.skija.Color
import org.jetbrains.skija.Paint
import java.awt.Color.RED

/*
 * list of colors
 */
var arrayListPaintsInts = arrayOf(0xffDC143C.toInt(), 0xff2E8B57.toInt(),
    0xff2F4F4F.toInt(), 0xffFF4500.toInt(), 0xff00FFFF.toInt(),
    0xff00FA9A.toInt(), 0xff8B008B.toInt(), 0xff6A5ACD.toInt(),
    0xAfDC143C.toInt(), 0xAf2E8B57.toInt(), 0xAf2F4F4F.toInt(),
    0xAfFF4500.toInt(), 0xAf00FFFF.toInt(), 0xAf00FA9A.toInt(),
    0xAf8B008B.toInt(), 0xAf6A5ACD.toInt(), 0x4fDC143C,
    0x4f2E8B57, 0x4f2F4F4F, 0x4fFF4500,
    0x4f00FFFF, 0x4f00FA9A, 0x4f8B008B, 0x4f6A5ACD
)

var arrayListPaints = arrayListPaintsInts.map{Paint().apply { color = it }}
