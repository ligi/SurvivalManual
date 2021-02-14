package org.ligi.survivalmanual

import android.app.Application
import org.ligi.survivalmanual.model.State
import org.ligi.tracedroid.TraceDroid
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        TraceDroid.init(this)
        State.applyDayNightMode()
    }
}
