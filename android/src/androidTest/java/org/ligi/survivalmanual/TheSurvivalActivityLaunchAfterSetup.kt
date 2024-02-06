package org.ligi.survivalmanual

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.model.titleResByURLMap
import org.ligi.survivalmanual.ui.MainActivity

@RunWith(AndroidJUnit4::class)
class TheSurvivalActivityLaunchAfterSetup {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun thatWeCanLaunchWithTools() {
        State.lastVisitedURL = "Tools"

        activityScenarioRule.scenario.recreate()

        verifyCorrectSubtitle("Tools")
    }

    @Test
    fun thatWeCanLaunchWithPower() {
        State.lastVisitedURL = "Power"

        activityScenarioRule.scenario.recreate()

        verifyCorrectSubtitle("Power")
    }


    @Test
    fun thatWeCanRecoverFromBadURL() {
        // happened when deleted "01"
        State.lastVisitedURL = "MAYBEBUG"

        activityScenarioRule.scenario.recreate()

        verifyCorrectSubtitle(State.FALLBACK_URL)
    }

    private fun verifyCorrectSubtitle(s: String) {
        activityScenarioRule.scenario.onActivity {
            assertEquals(it.getString(titleResByURLMap[s]!!), it.supportActionBar!!.subtitle)
        }
    }
}
