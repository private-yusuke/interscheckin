package pub.yusuke.foursquareclient.models

data class Checkin(
    val id: String,
    val createdAt: Int,
    val type: String,
    val shout: String?,
    val timeZoneOffset: Int,
    val editableUntil: Long?,
    val venue: V2Venue,
    val isMayor: Boolean,
    val score: Score
)

data class V2Venue(
    val id: String,
    val name: String
)

data class Score(
    val total: Int
)
