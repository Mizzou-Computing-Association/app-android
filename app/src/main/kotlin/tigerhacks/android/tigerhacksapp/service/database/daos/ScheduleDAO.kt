package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tigerhacks.android.tigerhacksapp.models.Event

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Dao
interface ScheduleDAO {

    @Query("SELECT * FROM event WHERE day=1")
    fun getFridayEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE day=2")
    fun getSaturdayEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE day=3")
    fun getSundayEvents(): LiveData<List<Event>>

    @Query("DELETE FROM event")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(events: List<Event>)

    @Transaction
    suspend fun updateEvents(events: List<Event>) {
        deleteAll()
        insertAll(events)
    }
}