package org.ligi.survivalmanual

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.ui.PreferenceActivity

@RunWith(AndroidJUnit4::class)
class ThePreferenceActivity {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(PreferenceActivity::class.java)

    @Test
    fun thatDayFollowSystemIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.system)).perform(click())

        assertEquals(MODE_NIGHT_FOLLOW_SYSTEM, getDefaultNightMode())
        assertEquals(MODE_NIGHT_FOLLOW_SYSTEM, State.getNightMode())
    }

    @Test
    fun thatDayIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.day)).perform(click())

        assertEquals(MODE_NIGHT_NO, getDefaultNightMode())
        assertEquals(MODE_NIGHT_NO, State.getNightMode())
    }

    @Test
    fun thatNightIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.night)).perform(click())

        assertEquals(MODE_NIGHT_YES, getDefaultNightMode())
        assertEquals(MODE_NIGHT_YES, State.getNightMode())
    }
}
