package org.ligi.survivalmanual

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.ui.PreferenceActivity
import org.ligi.trulesk.TruleskActivityRule

class ThePreferenceActivity {

    @get:Rule
    val activityTestRule = TruleskActivityRule(PreferenceActivity::class.java)

    @Test
    fun thatDayFollowSystemIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.system)).perform(click())

        assertEquals(MODE_NIGHT_FOLLOW_SYSTEM, getDefaultNightMode())
        assertEquals(MODE_NIGHT_FOLLOW_SYSTEM, State.getNightMode())
        activityTestRule.screenShot("daynight_auto")
    }

    @Test
    fun thatDayIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.day)).perform(click())

        assertEquals(MODE_NIGHT_NO, getDefaultNightMode())
        assertEquals(MODE_NIGHT_NO, State.getNightMode())
        activityTestRule.screenShot("daynight_day")
    }

    @Test
    fun thatNightIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.night)).perform(click())

        assertEquals(MODE_NIGHT_YES, getDefaultNightMode())
        assertEquals(MODE_NIGHT_YES, State.getNightMode())
        activityTestRule.screenShot("daynight_night")
    }


    @Test
    fun thatWeCanSwitchSearch() {

        val oldState = State.allowSearch()

        onView(withText(R.string.allow_search)).perform(click())

        assertEquals(!State.allowSearch(), oldState)
    }
}
