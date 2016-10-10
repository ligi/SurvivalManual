package org.ligi.survivalmanual

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        State.applyDayNightMode()
    }

}
