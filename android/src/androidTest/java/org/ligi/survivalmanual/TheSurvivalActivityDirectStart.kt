package org.ligi.survivalmanual

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.model.navigationEntryMap
import org.ligi.survivalmanual.model.titleResByURLMap
import org.ligi.survivalmanual.ui.MainActivity

@RunWith(AndroidJUnit4::class)
class TheSurvivalActivityDirectStart {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun thatActivityShouldLaunch() {
    }

    /* TODO remove the need for the sleeps - but without the test was flaky */

    @Test
    fun testWeCanOpenAllTopics() {
        navigationEntryMap.filter { it.entry.isListed }.forEach {
            onView(withId(R.id.main_drawer_layout)).perform(DrawerActions.open())
            SystemClock.sleep(500)
            onView(withId(R.id.main_navigationView)).perform(navigateTo(it.id))
            SystemClock.sleep(500)
            activityTestRule.scenario.onActivity { activity ->
                val subtitle = activity.supportActionBar!!.subtitle
                assertEquals(activity.getString(titleResByURLMap[it.entry.url]!!), subtitle)
            }
        }
    }
}
