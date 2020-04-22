package org.ligi.survivalmanual.model

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.chibatching.kotpref.KotprefModel
import org.ligi.survivalmanual.R.string.*

private const val DEFAULT_FONT_SIZE_STRING = "2"

object State : KotprefModel() {

    val FALLBACK_URL = navigationEntryMap.first().entry.url

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

    private fun getFontSizeString() =
            sharedPreferences.getString(preference_key_fontsize.string(), DEFAULT_FONT_SIZE_STRING)
                    ?: DEFAULT_FONT_SIZE_STRING

    fun nightModeString(): String? = sharedPreferences.getString(preference_key_nightmode.string(), "system")

    fun getNightMode() = when (nightModeString()) {
        "day" -> AppCompatDelegate.MODE_NIGHT_NO
        "night" -> AppCompatDelegate.MODE_NIGHT_YES
        "darknight" -> AppCompatDelegate.MODE_NIGHT_YES
        "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    fun Int.string() = context.getString(this)

    fun applyDayNightMode() {
        AppCompatDelegate.setDefaultNightMode(getNightMode())
    }
}
