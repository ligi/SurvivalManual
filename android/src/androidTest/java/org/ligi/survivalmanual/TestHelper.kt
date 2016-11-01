package org.ligi.survivalmanual

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText

object TestHelper {
 fun invokeMenu(@IdRes menuId: Int, @StringRes menuStringRes: Int) {

        try {
            onView(withId(menuId)).perform(click())
        } catch (nmv: NoMatchingViewException) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withText(menuStringRes)).perform(click())
        }
    }

}
