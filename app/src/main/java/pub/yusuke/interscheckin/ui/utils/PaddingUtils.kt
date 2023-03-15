package pub.yusuke.interscheckin.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

fun PaddingValues.copy(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    top: Dp = this.calculateTopPadding(),
    bottom: Dp = this.calculateBottomPadding(),
    start: Dp = this.calculateStartPadding(layoutDirection),
    end: Dp = this.calculateEndPadding(layoutDirection),
): PaddingValues =
    PaddingValues(
        top = top,
        bottom = bottom,
        start = start,
        end = end,
    )
