package pub.yusuke.interscheckin.ui.utils

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterscheckinTopBar(
    titleText: String,
    onClickNavigationIcon: () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector = Icons.Filled.ArrowBack,
    navigationIconDescription: String = "backIcon",
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(titleText) },
        navigationIcon = {
            IconButton(onClick = onClickNavigationIcon) {
                Icon(navigationIcon, navigationIconDescription)
            }
        },
        modifier = modifier,
        actions = actions,
    )
}
