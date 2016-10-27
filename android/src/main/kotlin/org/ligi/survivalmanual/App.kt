package org.ligi.survivalmanual

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        EventTracker.init(this)
        State.applyDayNightMode()
    }
}
