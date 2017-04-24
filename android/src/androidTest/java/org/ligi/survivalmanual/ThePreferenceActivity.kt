package org.ligi.survivalmanual

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.app.AppCompatDelegate.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.ui.PreferenceActivity
import org.ligi.trulesk.TruleskActivityRule


class ThePreferenceActivity {

    @get:Rule
    val activityTestRule = TruleskActivityRule(PreferenceActivity::class.java)

    @Test
    fun thatDayNightAutoIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.auto)).perform(click())

        assertThat(getDefaultNightMode()).isEqualTo(MODE_NIGHT_AUTO)
        assertThat(State.getNightMode()).isEqualTo(MODE_NIGHT_AUTO)
        activityTestRule.screenShot("daynight_auto")
    }

    @Test
    fun thatDayIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.day)).perform(click())

        assertThat(getDefaultNightMode()).isEqualTo(MODE_NIGHT_NO)
        assertThat(State.getNightMode()).isEqualTo(MODE_NIGHT_NO)
        activityTestRule.screenShot("daynight_day")
    }

    @Test
    fun thatNightIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.night)).perform(click())

        assertThat(getDefaultNightMode()).isEqualTo(MODE_NIGHT_YES)
        assertThat(State.getNightMode()).isEqualTo(MODE_NIGHT_YES)
        activityTestRule.screenShot("daynight_night")
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
