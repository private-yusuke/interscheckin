package pub.yusuke.interscheckin

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.anbora.labs.spatia.geometry.GeometryConverters
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenue
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenueDao

@Database(
    entities = [
        VisitedVenue::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(GeometryConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun visitedVenueDao(): VisitedVenueDao
}
