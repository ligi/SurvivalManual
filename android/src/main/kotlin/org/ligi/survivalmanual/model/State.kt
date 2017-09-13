package org.ligi.survivalmanual.model

import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import com.chibatching.kotpref.KotprefModel
import org.ligi.survivalmanual.R.string.*

object State : KotprefModel() {

    val FALLBACK_URL = NavigationEntryMap.first().entry.url

    var lastVisitedURL by stringPref(FALLBACK_URL)
    var searchTerm by nullableStringPref(null)
    var lastScrollPos by intPref(0)
    var isInitialOpening by booleanPref(true)

    private val sharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    fun markVisited() = sharedPreferences.getBoolean(preference_mark_visited.string(), false)
    fun allowEdit() = sharedPreferences.getBoolean(preference_key_edittoggle.string(), false)
    fun allowSearch() = sharedPreferences.getBoolean(preference_key_search.string(), false)
    fun allowSelect() = sharedPreferences.getBoolean(preference_key_select_text.string(), false)
    fun getFontSize() = 8f + 4f * Integer.parseInt(getFontSizeString())

    private fun getFontSizeString() = sharedPreferences.getString(preference_key_fontsize.string(), "2")

    fun nightModeString(): String? = sharedPreferences.getString(preference_key_nightmode.string(), "auto")

    fun getNightMode() = when (nightModeString()) {
        "day" -> AppCompatDelegate.MODE_NIGHT_NO
        "night" -> AppCompatDelegate.MODE_NIGHT_YES
        "darknight" -> AppCompatDelegate.MODE_NIGHT_YES
        "auto" -> AppCompatDelegate.MODE_NIGHT_AUTO
        else -> AppCompatDelegate.MODE_NIGHT_AUTO
    }

    fun Int.string() = context.getString(this)!!

    fun applyDayNightMode() {
        AppCompatDelegate.setDefaultNightMode(getNightMode())
    }
}
