package pub.yusuke.interscheckin.navigation.entity

data class Friend(
    val id: String,
    val firstName: String,
    val lastName: String?,
    val bio: String?,
    val photo: Photo?,
    val homeCity: String?,
) {
    data class Photo(
        val prefix: String,
        val suffix: String,
    )

    val displayName: String
        get() = if (lastName != null) "$firstName $lastName" else firstName
}

fun Friend.Photo.url(size: Int = 88) = "${prefix}${size}x${size}$suffix"
