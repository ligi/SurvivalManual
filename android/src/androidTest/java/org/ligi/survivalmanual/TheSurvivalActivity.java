package org.ligi.survivalmanual;

import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AppCompatDelegate;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.ligi.survivalmanual.ScreenShotTaker.takeScreenShot;


public class TheSurvivalActivity {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void thatActivityShouldLaunch() {

    }

    @Test
    public void thatThatHelpOpens() {
        onView(withId(R.id.menu_help)).perform(click());

        onView(withText(R.string.help_title)).check(matches(isDisplayed()));
    }

    @Test
    public void thatDayNightOpens() {
        onView(withId(R.id.menu_daynight)).perform(click());

        onView(withText(R.string.daynight)).check(matches(isDisplayed()));
        takeScreenShot(activityTestRule.getActivity(), "daynight_dialog");
    }

    @Test
    public void thatDayNightAutoIsSelectable() {

        onView(withId(R.id.menu_daynight)).perform(click());

        onView(withText(R.string.auto)).perform(click());

        onView(withText(android.R.string.ok)).perform(click());

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_AUTO);
        assertThat(State.INSTANCE.getDayNightMode()).isEqualTo(0);
        takeScreenShot(activityTestRule.getActivity(), "daynight_auto");
    }

    @Test
    public void thatNightIsSelectable() {

        onView(withId(R.id.menu_daynight)).perform(click());

        onView(withText(R.string.night)).perform(click());

        onView(withText(android.R.string.ok)).perform(click());

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES);
        assertThat(State.INSTANCE.getDayNightMode()).isEqualTo(1);
        takeScreenShot(activityTestRule.getActivity(), "daynight_night");
    }

    @Test
    public void thatDayIsSelectable() {

        onView(withId(R.id.menu_daynight)).perform(click());

        onView(withText(R.string.day)).perform(click());

        onView(withText(android.R.string.ok)).perform(click());

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_NO);
        assertThat(State.INSTANCE.getDayNightMode()).isEqualTo(2);
        takeScreenShot(activityTestRule.getActivity(), "daynight_day");
    }

    @Test
    public void thatDayNightCancelWorks() {
        thatNightIsSelectable();

        onView(withId(R.id.menu_daynight)).perform(click());

        onView(withText(R.string.day)).perform(click());

        onView(withText(android.R.string.cancel)).perform(click());

        assertThat(AppCompatDelegate.getDefaultNightMode()).isEqualTo(AppCompatDelegate.MODE_NIGHT_YES);
        assertThat(State.INSTANCE.getDayNightMode()).isEqualTo(1);
    }


    @Test
    public void testThatHelpContainsVersion() {
        thatThatHelpOpens();

        onView(withText(containsString(BuildConfig.VERSION_NAME))).check(matches(isDisplayed()));

        takeScreenShot(activityTestRule.getActivity(), "help");
    }

    /* TODO remove the need for the sleeps - but without the test was flaky */

    @Test
    public void testWeCanOpenAllTopics() {
        for (final Integer integer : NavigationDefinitions.INSTANCE.getMenu2htmlMap().keySet()) {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            SystemClock.sleep(500);
            onView(withId(R.id.navigationView)).perform(navigateTo(integer));
            SystemClock.sleep(500);
            final MainActivity activity = activityTestRule.getActivity();
            final CharSequence subtitle = activity.getSupportActionBar().getSubtitle();
            assertThat(subtitle).isEqualTo(activity.getString(NavigationDefinitions.INSTANCE.getTitleResById(integer)));

            takeScreenShot(activity, "topic_" + subtitle.toString().replace(" ", "_").replace("/", "_"));
        }
    }

}
