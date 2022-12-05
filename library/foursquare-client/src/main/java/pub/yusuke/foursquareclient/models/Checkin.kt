package pub.yusuke.foursquareclient.models

data class Checkin(
    val id: String,
    val createdAt: Int,
    val type: String,
    val shout: String?,
    val timeZoneOffset: Int,
    val editableUntil: Long?,
    // TODO: これは v3 のエンドポイントが返す Venue とは別物だと考えたほうがよい
//    val venue: Venue,
    val venue: V3Venue,
    val isMayor: Boolean,
    val score: Score
)

data class V3Venue(
    val id: String,
    val name: String
)

data class Score(
    val total: Int
)
