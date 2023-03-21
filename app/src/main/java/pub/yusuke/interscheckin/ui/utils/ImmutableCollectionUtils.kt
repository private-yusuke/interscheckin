package pub.yusuke.interscheckin.ui.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun <T> emptyImmutableList(): ImmutableList<T> = emptyList<T>().toImmutableList()
