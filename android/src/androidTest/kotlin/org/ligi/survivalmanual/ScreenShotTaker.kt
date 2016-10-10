package org.ligi.survivalmanual

import com.jraska.falcon.FalconSpoon

object ScreenShotTaker {

    fun takeScreenShot(activity: MainActivity, help: String) {
        try {
            FalconSpoon.screenshot(activity, help)
        } catch (ignored: Exception) {
            // we could not take the screenshot - no big deal
        }
    }
}
