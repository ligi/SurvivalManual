package org.ligi.survivalmanual

import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.NavigationViewActions.navigateTo
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
import android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ligi.passandroid.reporting.SpooningFailureHandler
import org.ligi.survivalmanual.ScreenShotTaker.takeScreenShot


class TheSurvivalActivity {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Espresso.setFailureHandler (SpooningFailureHandler(InstrumentationRegistry.getInstrumentation()))
        activityTestRule.activity.runOnUiThread { activityTestRule.activity.window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD) }
    }

    @Test
    fun thatActivityShouldLaunch() {

    }

    @Test
    fun thatThatHelpOpens() {
        TestHelper.invokeMenu(R.id.menu_help,R.string.introduction)

        onView(withText(R.string.help_title)).check(matches(isDisplayed()))
    }


    @Test
    fun testThatHelpContainsVersion() {
        thatThatHelpOpens()

        onView(withText(containsString(BuildConfig.VERSION_NAME))).check(matches(isDisplayed()))

        takeScreenShot(activityTestRule.activity, "help")
    }

    /* TODO remove the need for the sleeps - but without the test was flaky */

    @Test
    fun testWeCanOpenAllTopics() {
        for (integer in NavigationDefinitions.menu2htmlMap.keys) {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
            SystemClock.sleep(500)
            onView(withId(R.id.navigationView)).perform(navigateTo(integer))
            SystemClock.sleep(500)
            val activity = activityTestRule.activity
            val subtitle = activity.supportActionBar!!.subtitle
            assertThat(subtitle).isEqualTo(activity.getString(NavigationDefinitions.getTitleResById(integer)))

            takeScreenShot(activity, "topic_" + subtitle!!.toString().replace(" ", "_").replace("/", "_"))
        }
    }

}
