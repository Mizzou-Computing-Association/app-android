package tigerhacks.android.tigerhacksapp.service.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import tigerhacks.android.tigerhacksapp.models.Mentor
import tigerhacks.android.tigerhacksapp.models.Sponsor

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Dao
interface SponsorsDAO {
    @Query("SELECT * FROM sponsor")
    fun getSponsors(): LiveData<List<Sponsor>>

    @Query("SELECT * FROM mentor WHERE sponsor=:sponsor")
    fun getMentorsForSponsor(sponsor: String): LiveData<List<Mentor>>

    @Query("SELECT * FROM mentor")
    fun getAllMentors(): LiveData<List<Mentor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSponsors(sponsors: List<Sponsor>)

    @Query("DELETE FROM sponsor")
    fun deleteAllSponsors()

    @Transaction
    suspend fun updateSponsors(sponsors: List<Sponsor>) {
        deleteAllSponsors()
        insertAllSponsors(sponsors)
        val mentors = arrayListOf<Mentor>()
        for (sponsor in sponsors) {
            for (mentor in sponsor.mentors) {
                mentors.add(mentor.copy(sponsor = sponsor.name))
            }
        }
        updateMentors(mentors)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMentors(mentors: List<Mentor>)

    @Query("DELETE FROM mentor")
    fun deleteAllMentors()

    @Transaction
    suspend fun updateMentors(mentors: List<Mentor>) {
        deleteAllMentors()
        insertAllMentors(mentors)
    }
}