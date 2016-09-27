package org.ligi.survivalmanual

import android.os.SystemClock
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.NavigationViewActions.navigateTo
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.v7.app.AppCompatDelegate
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.ligi.survivalmanual.ScreenShotTaker.takeScreenShot


class TheSurvivalActivity {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun thatActivityShouldLaunch() {

    }

    @Test
    fun thatThatHelpOpens() {
        onView(withId(R.id.menu_help)).perform(click())

        onView(withText(R.string.help_title)).check(matches(isDisplayed()))
    }

    @Test
    fun thatDayNightOpens() {
        onView(withId(R.id.menu_daynight)).perform(click())

        onView(withText(R.string.daynight)).check(matches(isDisplayed()))
        takeScreenShot(activityTestRule.activity, "daynight_dialog")
    }

    @Test
    fun thatDayNightAutoIsSelectable() {

        onView(withId(R.id.menu_daynight)).perform(click())

        onView(withText(R.string.auto)).perform(click())

        onView(withText(android.R.string.ok)).perform(click())

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_AUTO)
        assertThat(State.dayNightMode).isEqualTo(0)
        takeScreenShot(activityTestRule.activity, "daynight_auto")
    }

    @Test
    fun thatNightIsSelectable() {

        onView(withId(R.id.menu_daynight)).perform(click())

        onView(withText(R.string.night)).perform(click())

        onView(withText(android.R.string.ok)).perform(click())

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
        assertThat(State.dayNightMode).isEqualTo(1)
        takeScreenShot(activityTestRule.activity, "daynight_night")
    }

    @Test
    fun thatDayIsSelectable() {

        onView(withId(R.id.menu_daynight)).perform(click())

        onView(withText(R.string.day)).perform(click())

        onView(withText(android.R.string.ok)).perform(click())

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_NO)
        assertThat(State.dayNightMode).isEqualTo(2)
        takeScreenShot(activityTestRule.activity, "daynight_day")
    }

    @Test
    fun thatDayNightCancelWorks() {
        thatNightIsSelectable()

        onView(withId(R.id.menu_daynight)).perform(click())

        onView(withText(R.string.day)).perform(click())

        onView(withText(android.R.string.cancel)).perform(click())

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
        assertThat(State.dayNightMode).isEqualTo(1)
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
