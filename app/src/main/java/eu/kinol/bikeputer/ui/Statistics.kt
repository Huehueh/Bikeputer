package eu.kinol.bikeputer.ui

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Stats")
data class Statistics (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val maxVelocity: Float = 0f,
    val avVelocity: Float = 0f,
    val distance: Float = 0f
) {

    fun asMap(): Map<String, Any> {
        return mapOf(
            "Maksymalna prędkość" to maxVelocity,
            "Srednia prędkość" to avVelocity,
            "Dystans" to distance
        )
    }

}