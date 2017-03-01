package org.ligi.survivalmanual

import android.content.Intent
import android.net.Uri
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.model.titleResByURLMap
import org.ligi.survivalmanual.ui.MainActivity
import org.ligi.trulesk.TruleskActivityRule

class TheSurvivalActivityLaunchAfterSetup {

    @get:Rule
    val activityTestRule = TruleskActivityRule(MainActivity::class.java, false)

    @Test
    fun thatWeCanLaunchWithTools() {
        State.lastVisitedURL = "Tools"

        activityTestRule.launchActivity()

        verifyCorrectSubtitle("Tools")
    }


    @Test
    fun thatWeCanLaunchWithPower() {
        State.lastVisitedURL = "Power"

        activityTestRule.launchActivity()

        verifyCorrectSubtitle("Power")
    }


    @Test
    fun thatWeCanRecoverFromBadURL() {
        // happened when deleted "01"
        State.lastVisitedURL = "MAYBEBUG"

        activityTestRule.launchActivity()

        verifyCorrectSubtitle(State.FALLBACK_URL)
    }

    @Test
    fun testPowerViaIntentData() {

        activityTestRule.launchActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://survivalmanual.github.io/Power")))

        verifyCorrectSubtitle("Power")
    }

    @Test
    fun testToolsViaIntentData() {

        activityTestRule.launchActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://survivalmanual.github.io/Tools")))

        verifyCorrectSubtitle("Tools")
    }

    private fun verifyCorrectSubtitle(s: String) {
        val subtitle = activityTestRule.activity.supportActionBar!!.subtitle
        assertThat(subtitle).isEqualTo(activityTestRule.activity.getString(titleResByURLMap[s]!!))
    }

}
