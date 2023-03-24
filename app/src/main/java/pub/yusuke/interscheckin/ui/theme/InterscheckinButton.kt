package pub.yusuke.interscheckin.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pub.yusuke.interscheckin.ui.theme.InterscheckinTextStyle.bold

@Composable
fun InterscheckinPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
    ) {
        Text(
            text = text,
            style = InterscheckinTextStyle.Normal.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun InterscheckinSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
    ) {
        Text(
            text = text,
            style = InterscheckinTextStyle.Normal.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
