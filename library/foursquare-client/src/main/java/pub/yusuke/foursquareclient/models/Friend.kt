package pub.yusuke.foursquareclient.models

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
}

fun Friend.Photo.url(size: Int = 88) = "${prefix}${size}x${size}$suffix"
