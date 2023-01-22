package pub.yusuke.interscheckin.repositories.visitedvenues

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.SkipQueryVerification

@Dao
interface VisitedVenueDao {
    @Query(
        """
        select
            *
        from
            ${VisitedVenue.TABLE_NAME}
        order by
            distance(
                setsrid(location, ${VisitedVenue.SRID}),
                makepoint(:latitude, :longitude, ${VisitedVenue.SRID})
            ) asc
        limit :limit
    """
    )
    @SkipQueryVerification
    suspend fun findByLatLong(
        latitude: Double,
        longitude: Double,
        limit: Long = 10
    ): List<VisitedVenue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitedVenues(visitedVenues: List<VisitedVenue>)

    @Query("delete from ${VisitedVenue.TABLE_NAME}")
    suspend fun deleteAll()
}
