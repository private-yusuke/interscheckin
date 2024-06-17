package pub.yusuke.foursquareclient.models

import com.squareup.moshi.Json

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
)

fun LatAndLong.llString() = "${latitude.toBigDecimal().toPlainString()},${longitude.toBigDecimal().toPlainString()}"

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
