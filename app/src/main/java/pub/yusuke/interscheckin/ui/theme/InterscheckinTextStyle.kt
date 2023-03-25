package pub.yusuke.interscheckin.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object InterscheckinTextStyle {
    val Small = TextStyle(fontSize = 10.sp)
    val Normal = TextStyle(fontSize = 12.sp)
    val Large = TextStyle(fontSize = 14.sp)
    val ExtraLarge = TextStyle(fontSize = 16.sp)
    val SuperExtraLarge = TextStyle(fontSize = 30.sp)

    fun TextStyle.bold() = this.copy(fontWeight = FontWeight.Bold)
}
