package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import tigerhacks.android.tigerhacksapp.sponsors.Mentor
import tigerhacks.android.tigerhacksapp.sponsors.Sponsor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Dao
interface SponsorsDAO {
    @Query("SELECT * FROM sponsor")
    fun getSponsors(): LiveData<List<Sponsor>>

    @Query("SELECT * FROM mentor WHERE sponsor=:sponsor")
    fun getMentorsForSponsor(sponsor: String): LiveData<List<Mentor>>

    @Insert
    fun insertAllSponsors(sponsors: List<Sponsor>)

    @Query("DELETE FROM sponsor")
    fun deleteAllSponsors()

    @Transaction
    open suspend fun updateSponsors(sponsors: List<Sponsor>) {
        deleteAllSponsors()
        insertAllSponsors(sponsors)
    }

    @Insert
    fun insertAllMentors(mentors: List<Mentor>)

    @Query("DELETE FROM mentor")
    fun deleteAllMentors()

    @Transaction
    open suspend fun updateMentors(mentors: List<Mentor>) {
        deleteAllMentors()
        insertAllMentors(mentors)
    }
}