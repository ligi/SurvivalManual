package org.ligi.survivalmanual.model

import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import com.chibatching.kotpref.KotprefModel
import org.ligi.survivalmanual.R

object State : KotprefModel() {

    val FALLBACK_URL="02"

    var lastVisitedURL by stringPref(FALLBACK_URL)
    var searchTerm by nullableStringPref(null)
    var lastScrollPos by intPref(0)
    var isInitialOpening by booleanPref(true)

    internal val sharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    fun allowEdit() = sharedPreferences.getBoolean(context.getString(R.string.preference_key_edittoggle), false)
    fun allowSearch() = sharedPreferences.getBoolean(context.getString(R.string.preference_key_search), false)
    fun allowSelect() = sharedPreferences.getBoolean(context.getString(R.string.preference_key_select_text), false)

    fun getFontSize() =
            8f + 4f*Integer.parseInt(sharedPreferences.getString(context.getString(R.string.preference_key_fontsize), "2"))

    fun getNightMode(): Int {
        val key = sharedPreferences.getString(context.getString(R.string.preference_key_nightmode), "auto")
        return when (key) {
            "day" -> AppCompatDelegate.MODE_NIGHT_NO
            "night" -> AppCompatDelegate.MODE_NIGHT_YES
            "auto" -> AppCompatDelegate.MODE_NIGHT_AUTO
            else -> AppCompatDelegate.MODE_NIGHT_AUTO
        }
    }

    fun applyDayNightMode() {
        AppCompatDelegate.setDefaultNightMode(getNightMode())
    }
}
