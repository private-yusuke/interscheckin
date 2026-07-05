package pub.yusuke.foursquareclient.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GeotaggingCandidatesResponseTest {
    private val adapter = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(VenueApiService.SearchPlacesNearbyResponse::class.java)

    @Test
    fun parsesCurrentGeotaggingCandidatesResponse() {
        val response = adapter.fromJson(
            """
            {
              "candidates": [{
                "fsq_place_id": "4dd0d870c65bdac7139b6d88",
                "latitude": 36.10657,
                "longitude": 140.10553,
                "categories": [{
                  "fsq_category_id": "52f2ab2ebcbc57f1066b8b4f",
                  "name": "Bus Stop",
                  "icon": {
                    "prefix": "https://example.com/bus_",
                    "suffix": ".png"
                  }
                }],
                "chains": [{
                  "fsq_chain_id": "556f676fbd6a75a99038d8e9",
                  "name": "7-Eleven"
                }],
                "link": "/places/4dd0d870c65bdac7139b6d88",
                "location": {
                  "country": "JP",
                  "formatted_address": "つくば市, 茨城県"
                },
                "name": "天久保三丁目バス停"
              }]
            }
            """.trimIndent(),
        )

        assertNotNull(response)
        val candidate = response!!.candidates.single()
        assertEquals("4dd0d870c65bdac7139b6d88", candidate.fsqPlaceId)
        assertEquals("52f2ab2ebcbc57f1066b8b4f", candidate.categories.single().fsqCategoryId)
        assertEquals("556f676fbd6a75a99038d8e9", candidate.chains?.single()?.fsqChainId)
        assertEquals("つくば市, 茨城県", candidate.location?.formattedAddress)
    }
}
