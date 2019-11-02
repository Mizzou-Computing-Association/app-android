package tigerhacks.android.tigerhacksapp.service.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import tigerhacks.android.tigerhacksapp.models.Prize
import tigerhacks.android.tigerhacksapp.models.Event
import tigerhacks.android.tigerhacksapp.service.database.daos.PrizesDAO
import tigerhacks.android.tigerhacksapp.service.database.daos.ProfileDAO
import tigerhacks.android.tigerhacksapp.service.database.daos.ScheduleDAO
import tigerhacks.android.tigerhacksapp.service.database.daos.SponsorsDAO
import tigerhacks.android.tigerhacksapp.models.Mentor
import tigerhacks.android.tigerhacksapp.models.Sponsor
import tigerhacks.android.tigerhacksapp.models.Profile

/**
 * @author pauldg7@gmail.com (Paul Gillis)
 */

@Database(entities = [Prize::class, Event::class, Sponsor::class, Mentor::class, Profile::class], version = 6, exportSchema = false)
abstract class TigerHacksDatabase : RoomDatabase() {
    abstract fun prizeDAO(): PrizesDAO
    abstract fun scheduleDAO(): ScheduleDAO
    abstract fun sponsorsDAO(): SponsorsDAO
    abstract fun profileDAO(): ProfileDAO

    companion object {
        private var instance: TigerHacksDatabase? = null

        fun getDatabase(applicationContext: Context): TigerHacksDatabase {
            val immutableInstance = instance
            if (immutableInstance != null) return immutableInstance

            instance = Room.databaseBuilder(
                applicationContext,
                TigerHacksDatabase::class.java,
                "TigerHacksDatabase"
            )
                .fallbackToDestructiveMigration()
                .build()

            return instance!!
        }
    }
}