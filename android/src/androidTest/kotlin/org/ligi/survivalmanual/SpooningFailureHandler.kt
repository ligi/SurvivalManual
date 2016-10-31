package org.ligi.passandroid.reporting

import android.app.Activity
import android.app.Instrumentation
import android.support.test.espresso.FailureHandler
import android.support.test.espresso.base.DefaultFailureHandler
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.view.View
import com.jraska.falcon.FalconSpoon
import org.hamcrest.Matcher


class SpooningFailureHandler(private val instrumentation: Instrumentation) : FailureHandler {

    private val delegate by lazy { DefaultFailureHandler(instrumentation.targetContext) }


    override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
        try {
            FalconSpoon.screenshot(currentActivity, "error_falcon")
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

        delegate.handle(error, viewMatcher)
    }


    private val currentActivity: Activity
        @Throws(Throwable::class)
        get() {
            instrumentation.waitForIdleSync()
            val activity = arrayOfNulls<Activity>(1)
            instrumentation.runOnMainSync {
                val activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                activity[0] = activities.iterator().next()
            }
            return activity[0]!!
        }

}

