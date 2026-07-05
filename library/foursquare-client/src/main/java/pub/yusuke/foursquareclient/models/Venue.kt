package pub.yusuke.foursquareclient.models

import com.squareup.moshi.Json
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.math.sqrt

data class Venue(
    val categories: List<Category>,
    val chains: List<Chain>?,
    val distance: Long?,
    @Json(name = "fsq_id")
    val fsqId: String,
    val geocodes: Geocodes,
    val link: String?,
    val location: Location,
    val name: String,
    @Json(name = "related_places")
    val relatedPlaces: RelatedPlaces?,
    val timezone: String?,
)

data class Category(
    val icon: Icon,
    val id: String,
    val name: String,
)

data class Chain(
    val id: String,
    val name: String,
)

data class Geocodes(
    val main: LatAndLong?,
//    val roof: Roof?
)

data class Icon(
    val prefix: String,
    val suffix: String,
)

fun Icon.url() = "${prefix}88$suffix"

data class Location(
    val address: String?,
    @Json(name = "address_extended")
    val addressExtended: String?,
    @Json(name = "census_block")
    val censusBlock: String?,
    val country: String,
    @Json(name = "cross_street")
    val crossStreet: String?,
    val dma: String?,
    @Json(name = "formatted_address")
    val formattedAddress: String?,
    val locality: String?,
    val postcode: String?,
    val region: String?,
)

data class LatAndLong(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        const val EARTH_RADIUS_METERS = 6_371_000.0
    }
}

fun LatAndLong.llString() = "${latitude.toBigDecimal().toPlainString()},${longitude.toBigDecimal().toPlainString()}"
fun LatAndLong.distanceFrom(other: LatAndLong): Long {
    val latitudeDelta = Math.toRadians(latitude - other.latitude)
    val longitudeDelta = Math.toRadians(longitude - other.longitude)
    val startLatitude = Math.toRadians(other.latitude)
    val endLatitude = Math.toRadians(latitude)
    val haversine =
        sin(latitudeDelta / 2) * sin(latitudeDelta / 2) +
            cos(startLatitude) * cos(endLatitude) *
            sin(longitudeDelta / 2) * sin(longitudeDelta / 2)
    return (LatAndLong.EARTH_RADIUS_METERS * 2 * asin(sqrt(haversine))).roundToLong()
}

data class Parent(
    @Json(name = "fsq_id")
    val fsqId: String,
    val name: String,
)

data class RelatedPlaces(
    val parent: Parent?,
)

// data class Roof(
//    val latitude: Double,
//    val longitude: Double
// )
