package org.ligi.survivalmanual;

import com.jraska.falcon.FalconSpoon;

class ScreenShotTaker {

    static void takeScreenShot(final MainActivity activity, final String help) {
        try {
            FalconSpoon.screenshot(activity, help);
        } catch (Exception ignored) {
            // we could not take the screenshot - no big deal
        }
    }
}
