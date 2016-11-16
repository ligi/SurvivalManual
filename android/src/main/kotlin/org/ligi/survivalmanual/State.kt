package org.ligi.survivalmanual

import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import com.chibatching.kotpref.KotprefModel

object State : KotprefModel() {

    var lastVisitedSite: String by stringPrefVar("01")
    var lastScroll: Int by intPrefVar(0)
    internal val sharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    fun allowEdit() = sharedPreferences.getBoolean(context.getString(R.string.preference_key_edittoggle), false)

    fun getNightMode(): Int {
        val key = sharedPreferences.getString(context.getString(R.string.preference_key_nightmode), "auto")
        return when (key) {
            "day" -> return AppCompatDelegate.MODE_NIGHT_NO
            "night" -> return AppCompatDelegate.MODE_NIGHT_YES
            "auto" -> return AppCompatDelegate.MODE_NIGHT_AUTO
            else -> AppCompatDelegate.MODE_NIGHT_AUTO
        }
    }

    fun applyDayNightMode() {
        val mode = when (getNightMode()) {
            0 -> AppCompatDelegate.MODE_NIGHT_AUTO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            2 -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_AUTO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
