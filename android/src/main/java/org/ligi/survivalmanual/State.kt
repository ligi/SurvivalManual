package org.ligi.survivalmanual

import android.support.v7.app.AppCompatDelegate
import com.chibatching.kotpref.KotprefModel

object State : KotprefModel() {

    var lastVisitedSite: String by stringPrefVar("01")
    var lastScroll: Int by intPrefVar(0)

    var dayNightMode: Int by intPrefVar(0)

    fun applyDayNightMode() {
        val mode = when (dayNightMode) {
            0 -> AppCompatDelegate.MODE_NIGHT_AUTO
            1 -> AppCompatDelegate.MODE_NIGHT_YES
            2 -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_AUTO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
