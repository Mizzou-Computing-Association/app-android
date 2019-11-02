package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tigerhacks.android.tigerhacksapp.models.Profile

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Dao
interface ProfileDAO {
    @Query("SELECT * FROM profile")
    fun getProfile(): LiveData<Profile>

    @Query("DELETE FROM profile")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(profile: Profile)

    @Transaction
    suspend fun updateProfile(profile: Profile) {
        deleteAll()
        insertAll(profile)
    }
}