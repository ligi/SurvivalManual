package org.ligi.survivalmanual

import android.app.Application
import org.ligi.survivalmanual.model.State

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        State.applyDayNightMode()
    }
}
