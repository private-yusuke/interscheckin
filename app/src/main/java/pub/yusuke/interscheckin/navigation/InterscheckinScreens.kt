package pub.yusuke.interscheckin.navigation

import androidx.navigation.NamedNavArgument

sealed class InterscheckinScreens(
    val name: String,
    val navArguments: List<NamedNavArgument> = emptyList(),
) {
    val route: String = name.withArguments(navArguments)

    object Splash : InterscheckinScreens("splash")
    object LocationAccessAcquirement : InterscheckinScreens("locationAccessAcquirement")

    // / Screen for creating checkin
    object Main : InterscheckinScreens("main")
    object Settings : InterscheckinScreens("settings")
    object CredentialSettings : InterscheckinScreens("credentialSettings")
    object LocationSettings : InterscheckinScreens("locationSettings")
    object ResetSettings : InterscheckinScreens("resetSettings")
    object Histories : InterscheckinScreens("histories")
    object FriendSelection : InterscheckinScreens("friendSelection")
}

private fun String.withArguments(navArguments: List<NamedNavArgument>): String {
    val requiredArguments = navArguments.filter { it.argument.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(prefix = "/", separator = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter { it.argument.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(prefix = "?", separator = "&") { "${it.name}={${it.name}}" }
        .orEmpty()
    return this + requiredArguments + optionalArguments
}
