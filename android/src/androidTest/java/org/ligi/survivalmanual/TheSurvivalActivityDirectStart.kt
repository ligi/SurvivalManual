package org.ligi.survivalmanual

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.ligi.survivalmanual.model.navigationEntryMap
import org.ligi.survivalmanual.model.titleResByURLMap
import org.ligi.survivalmanual.ui.MainActivity
import org.ligi.trulesk.TruleskActivityRule

class TheSurvivalActivityDirectStart {

    @get:Rule
    val activityTestRule = TruleskActivityRule(MainActivity::class.java)

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
            val activity = activityTestRule.activity
            val subtitle = activity.supportActionBar!!.subtitle
            assertThat(subtitle).isEqualTo(activity.getString(titleResByURLMap[it.entry.url]!!))

            activityTestRule.screenShot("topic_" + subtitle!!.toString().replace(" ", "_").replace("/", "_"))
        }

    }

}
