package org.ligi.survivalmanual

import android.app.Application
import org.ligi.survivalmanual.model.State
import org.ligi.tracedroid.TraceDroid

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TraceDroid.init(this)
        State.applyDayNightMode()
    }
}
