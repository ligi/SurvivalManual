package org.ligi.survivalmanual

import android.content.Intent
import android.net.Uri
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.ligi.survivalmanual.model.NavigationDefinitions
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.ui.MainActivity
import org.ligi.trulesk.TruleskActivityRule

class TheSurvivalActivityLaunchAfterSetup {

    @get:Rule
    val activityTestRule = TruleskActivityRule(MainActivity::class.java, false)

    @Test
    fun thatWeCanLaunchWith04() {
        State.lastVisitedURL = "04"

        activityTestRule.launchActivity()

        verifyCorrectSubtitle("04")
    }


    @Test
    fun thatWeCanLaunchWith05() {
        State.lastVisitedURL = "05"

        activityTestRule.launchActivity()

        verifyCorrectSubtitle("05")
    }


    @Test
    fun thatWeCanRecoverFromBadURL() {
        // happened when deleted "01"
        State.lastVisitedURL = "MAYBEBUG"

        activityTestRule.launchActivity()

        verifyCorrectSubtitle(State.FALLBACK_URL)
    }

    @Test
    fun thatWeCanRecoverFromBadURL05() {

        activityTestRule.launchActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://survivalmanual.github.io/05")))

        verifyCorrectSubtitle("05")
    }

    @Test
    fun thatWeCanRecoverFromBadURL06() {

        activityTestRule.launchActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://survivalmanual.github.io/06")))

        verifyCorrectSubtitle("06")
    }

    private fun verifyCorrectSubtitle(s: String) {
        val subtitle = activityTestRule.activity.supportActionBar!!.subtitle
        assertThat(subtitle).isEqualTo(activityTestRule.activity.getString(NavigationDefinitions.titleResByURLMap[s]!!))
    }

}
