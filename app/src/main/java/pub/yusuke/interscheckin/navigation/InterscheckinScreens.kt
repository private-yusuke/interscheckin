package pub.yusuke.interscheckin.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class InterscheckinScreens(
    val name: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    val route: String = name.withArguments(navArguments)

    // / Screen for creating checkin
    object Main : InterscheckinScreens("main")

    object Settings : InterscheckinScreens(
        name = "settings",
        navArguments = listOf(
            navArgument("reasonId") {
                type = NavType.ReferenceType
                defaultValue = 0
            }
        )
    ) {
        fun createRoute(reasonId: Int? = null) =
            reasonId?.let {
                route.replace("{reasonId}", it.toString())
            } ?: name
    }
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
