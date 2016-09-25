package org.ligi.survivalmanual;

import android.support.test.espresso.web.model.Atoms;
import android.support.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static org.hamcrest.Matchers.containsString;
import static org.ligi.survivalmanual.ScreenShotTaker.takeScreenShot;


public class TheSurvivalActivity {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private void waitForInitialWebView() {
        onWebView(withId(R.id.webView)).forceJavascriptEnabled();
        onWebView(withId(R.id.webView)).check(webMatches(Atoms.getCurrentUrl(), containsString("01.htm")));
    }

    @Test
    public void thatActivityShouldLaunch() {
        waitForInitialWebView();
    }

    @Test
    public void thatThatHelpOpens() {
        waitForInitialWebView();
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

    @Test
    public void testWeCanOpenAllTopics() {
        waitForInitialWebView();
        for (final Integer integer : NavigationDefinitions.INSTANCE.getMenu2htmlMap().keySet()) {
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withId(R.id.navigationView)).perform(navigateTo(integer));
            onWebView(withId(R.id.webView)).check(webMatches(Atoms.getCurrentUrl(), containsString("htm")));

            final MainActivity activity = activityTestRule.getActivity();
            final CharSequence subtitle = activity.getSupportActionBar().getSubtitle();
            assertThat(subtitle).isEqualTo(activity.getString(NavigationDefinitions.INSTANCE.getTitleResById(integer)));

            takeScreenShot(activity, "topic_" + subtitle.toString().replace(" ", "_").replace("/", "_"));
        }
    }
    */
}
