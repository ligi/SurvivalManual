package org.ligi.survivalmanual

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.v7.app.AppCompatDelegate
import android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
import android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ligi.passandroid.reporting.SpooningFailureHandler
import org.ligi.survivalmanual.ScreenShotTaker.takeScreenShot


class ThePreferenceActivity {

    @get:Rule
    val activityTestRule = ActivityTestRule(PreferenceActivity::class.java)

    @Before
    fun setUp() {
        Espresso.setFailureHandler (SpooningFailureHandler(InstrumentationRegistry.getInstrumentation()))
        activityTestRule.activity.runOnUiThread { activityTestRule.activity.window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD) }
    }

    @Test
    fun thatDayNightAutoIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.auto)).perform(click())

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_AUTO)
        assertThat(State.getNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_AUTO)
        takeScreenShot(activityTestRule.activity, "daynight_auto")
    }


    @Test
    fun thatDayIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.day)).perform(click())

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_NO)
        assertThat(State.getNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_NO)
        takeScreenShot(activityTestRule.activity, "daynight_day")
    }


    @Test
    fun thatNightIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.night)).perform(click())

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
        assertThat(State.getNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES)
        takeScreenShot(activityTestRule.activity, "daynight_night")
    }

    @Test
    fun thatWeCanSwitchEdit() {

        val oldState = State.allowEdit()

        onView(withText(R.string.allow_edit)).perform(click())

        assertThat(oldState).isEqualTo(!State.allowEdit())
    }

    @Test
    fun thatWeCanSwitchSearch() {

        val oldState = State.allowSearch()

        onView(withText(R.string.allow_search)).perform(click())

        assertThat(oldState).isEqualTo(!State.allowSearch())
    }
}
