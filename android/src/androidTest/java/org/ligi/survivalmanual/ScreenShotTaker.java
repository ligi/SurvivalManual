package org.ligi.survivalmanual;

import com.squareup.spoon.Spoon;

class ScreenShotTaker {

    static void takeScreenShot(final MainActivity activity, final String help) {
        try {
            Spoon.screenshot(activity, help);
        } catch (Exception ignored) {
            // we could not take the screenshot - no big deal
        }
    }
}
