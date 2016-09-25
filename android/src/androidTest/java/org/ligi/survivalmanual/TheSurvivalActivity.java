package org.ligi.survivalmanual;

import android.os.SystemClock;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
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
    public void testThatHelpContainsVersion() {
        thatThatHelpOpens();

        onView(withText(containsString(BuildConfig.VERSION_NAME))).check(matches(isDisplayed()));

        takeScreenShot(activityTestRule.getActivity(), "help");
    }

    /* TODO bring back this test - was flaky  - hanging when opening the drawer
*/
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
