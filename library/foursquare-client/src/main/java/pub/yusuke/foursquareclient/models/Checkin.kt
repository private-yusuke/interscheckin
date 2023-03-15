package pub.yusuke.foursquareclient.models

data class Checkin(
    val id: String,
    val createdAt: Long,
    val type: String,
    val shout: String?,
    val timeZoneOffset: Long,
    val editableUntil: Long?,
    val venue: V2Venue,
    val isMayor: Boolean,
    val score: Score?,
) {
    data class V2Venue(
        val id: String,
        val name: String,
        val location: Location,
        val categories: List<Category>,
    ) {
        data class Location(
            val state: String?,
            val city: String?,
            val lat: Double,
            val lng: Double,
        )

        data class Category(
            val id: String,
            val name: String,
            val pluralName: String,
            val shortName: String,
            val icon: Icon,
            val primary: Boolean,
        ) {
            data class Icon(
                val prefix: String,
                val suffix: String,
            )
        }
    }

    data class Score(
        val total: Long,
    )
}

fun Checkin.V2Venue.Category.Icon.url() = "${prefix}88$suffix"
