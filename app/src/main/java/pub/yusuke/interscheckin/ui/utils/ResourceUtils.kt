package pub.yusuke.interscheckin.ui.utils

fun Int?.isValidResourceId(): Boolean =
    this != null && this != 0
