package tigerhacks.android.tigerhacksapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

enum class ThemeMode {
    LIGHT,
    DARK,
    AUTO_BATTERY,
    FOLLOW_SYSTEM,
    DEFAULT;

    fun getPosition(): Int {
        return when(this) {
            LIGHT -> 0
            DARK -> 1
            else -> 2
        }
    }
}

class TigerApplication : Application() {

    companion object {
        private const val THEME_KEY = "theme"

        private var sharedPreferences: SharedPreferences? = null

        fun getThemeMode(): ThemeMode {
            val themeOridinal = sharedPreferences?.getInt(THEME_KEY, ThemeMode.DEFAULT.ordinal) ?: ThemeMode.DEFAULT.ordinal
            return ThemeMode.values()[themeOridinal]
        }

        fun setThemeMode(themeMode: ThemeMode) {
            val mode = when (themeMode) {
                ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                ThemeMode.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeMode.AUTO_BATTERY -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                ThemeMode.DEFAULT -> if (Build.VERSION.SDK_INT >= 29) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
            }
            sharedPreferences?.edit()?.putInt(THEME_KEY, themeMode.ordinal)?.apply()
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }



    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences("TigerPreferences", Context.MODE_PRIVATE)
        val theme = getThemeMode()
        setThemeMode(theme)
    }
}