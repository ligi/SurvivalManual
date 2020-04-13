package org.ligi.survivalmanual

import android.Manifest
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.linkedin.android.testbutler.TestButler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.ui.PreferenceActivity
import org.ligi.trulesk.TruleskActivityRule

class ThePreferenceActivity {

    @get:Rule
    val activityTestRule = TruleskActivityRule(PreferenceActivity::class.java) {
        TestButler.grantPermission(ApplicationProvider.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    @Test
    fun thatDayNightAutoIsSelectable() {

        onView(withText(R.string.preference_daynight_title)).perform(click())

        onView(withText(R.string.system)).perform(click())

        assertThat(getDefaultNightMode()).isEqualTo(MODE_NIGHT_FOLLOW_SYSTEM)
        assertThat(State.getNightMode()).isEqualTo(MODE_NIGHT_FOLLOW_SYSTEM)
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
    fun thatWeCanSwitchSearch() {

        val oldState = State.allowSearch()

        onView(withText(R.string.allow_search)).perform(click())

        assertThat(oldState).isEqualTo(!State.allowSearch())
    }
}
