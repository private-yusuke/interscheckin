package pub.yusuke.interscheckin.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme
import pub.yusuke.interscheckin.ui.utils.copy
import pub.yusuke.interscheckin.ui.utils.isValidResourceId

@Composable
fun SettingsScreen(
    viewModel: SettingsContract.ViewModel = hiltViewModel<SettingsViewModel>(),
    navController: NavController,
    onRecreateRequired: () -> Unit
) {
    var foursquareOAuthToken by remember { mutableStateOf("") }
    var foursquareApiKey by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    InterscheckinTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                SettingsTopBar {
                    navController.popBackStack()
                }
            }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(innerPadding.copy(top = 16.dp))
            ) {
                CredentialSettingRow(
                    label = stringResource(R.string.settings_foursquare_oauth_token_label),
                    value = foursquareOAuthToken,
                    onValueChange = { foursquareOAuthToken = it }
                )
                CredentialSettingRow(
                    label = stringResource(R.string.settings_foursquare_api_ken),
                    value = foursquareApiKey,
                    onValueChange = { foursquareApiKey = it }
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveSettings(
                                foursquareOAuthToken = foursquareOAuthToken,
                                foursquareApiKey = foursquareApiKey
                            )
                            // Snackbar ???????????????????????????????????????????????????????????????????????????????????? dismiss ??????
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            onRecreateRequired()
                        }
                    },
                    enabled = foursquareApiKey.isNotBlank() && foursquareOAuthToken.isNotBlank()
                ) {
                    Text(stringResource(R.string.settings_save))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        foursquareOAuthToken = viewModel.foursquareOAuthTokenFlow.first()
        foursquareApiKey = viewModel.foursquareApiKeyFlow.first()
    }

    // ?????????????????????????????????????????? Snackbar ?????????
    if (viewModel.reasonId.isValidResourceId()) {
        val reason = stringResource(viewModel.reasonId!!)
        LaunchedEffect(Unit) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = reason,
                duration = SnackbarDuration.Long
            )
        }
    }
}

@Composable
private fun SettingsTopBar(
    onNavigateToPreviousRoute: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.settings_topbar_title)) },
        navigationIcon = {
            IconButton(onClick = onNavigateToPreviousRoute) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White
    )
}

@Composable
private fun CredentialSettingRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = label)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .heightIn(min = 150.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun CredentialSettingRowPreview() =
    CredentialSettingRow(
        label = "setting1",
        value = "some long credentials being modified here",
        onValueChange = {}
    )

@Preview
@Composable
private fun SettingsScreenPreview() =
    SettingsScreen(
        viewModel = object : SettingsContract.ViewModel {
            override val reasonId: Int? = null
            override val foursquareOAuthTokenFlow: Flow<String> = flowOf("oauth_token")
            override val foursquareApiKeyFlow: Flow<String> = flowOf("api_key")

            override suspend fun saveSettings(
                foursquareOAuthToken: String,
                foursquareApiKey: String
            ) {
            }
        },
        navController = rememberNavController(),
        onRecreateRequired = {}
    )
