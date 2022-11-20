package pub.yusuke.foursquareclient.models

import android.location.LocationManager

data class Venue(
    val categories: List<Category>,
    val chains: List<Chain>?,
    val distance: Int?,
    val fsq_id: String,
    val geocodes: Geocodes,
    val link: String?,
    val location: Location,
    val name: String,
    val related_places: RelatedPlaces?,
    val timezone: String?
)

data class Category(
    val icon: Icon,
    val id: String,
    val name: String
)

data class Chain(
    val id: String,
    val name: String
)

data class Geocodes(
    val main: LatAndLong
//    val roof: Roof?
)

data class Icon(
    val prefix: String,
    val suffix: String
)

fun Icon.url() = "${prefix}88$suffix"

data class Location(
    val address: String?,
    val address_extended: String?,
    val census_block: String?,
    val country: String,
    val cross_street: String?,
    val dma: String?,
    val formatted_address: String?,
    val locality: String?,
    val postcode: String?,
    val region: String?
)

data class LatAndLong(
    val latitude: Double,
    val longitude: Double
)

fun LatAndLong.llString() = "${latitude.toBigDecimal().toPlainString()},${longitude.toBigDecimal().toPlainString()}"
fun LatAndLong.toLocation() = android.location.Location(LocationManager.GPS_PROVIDER).apply {
    latitude = this@toLocation.latitude
    longitude = this@toLocation.longitude
}

data class Parent(
    val fsq_id: String,
    val name: String
)

data class RelatedPlaces(
    val parent: Parent?
)

// data class Roof(
//    val latitude: Double,
//    val longitude: Double
// )
