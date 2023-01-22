package pub.yusuke.interscheckin.repositories.visitedvenues

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anbora.labs.spatia.geometry.Point

@Entity(tableName = VisitedVenue.TABLE_NAME)
data class VisitedVenue(
    @PrimaryKey
    val id: String,
    val name: String,
    val categoriesString: String? = null,
    val location: Point,
    @ColumnInfo(name = "icon_name")
    val iconName: String?,
    @ColumnInfo(name = "icon_url")
    val iconUrl: String?
) {
    companion object {
        const val TABLE_NAME = "visited_venues"
        const val SRID = 4326
    }
}
