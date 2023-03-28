package pub.yusuke.interscheckin.ui.utils

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun InterscheckinTopBar(
    titleText: String,
    onClickNavigationIcon: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = Icons.Filled.ArrowBack,
    navigationIconDescription: String = "backIcon",
) {
    TopAppBar(
        title = { Text(titleText) },
        navigationIcon = {
            IconButton(onClick = onClickNavigationIcon) {
                Icon(navigationIcon, navigationIconDescription)
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        modifier = modifier,
    )
}
