package org.ligi.survivalmanual

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
import org.ligi.survivalmanual.model.State

object EventTracker {

    lateinit var context: Context

    val analytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    fun init(context: Context) {
        this.context = context

        analytics.setUserProperty("store", BuildConfig.STORE)
        analytics.setUserProperty("nightmode", State.nightModeString())
        analytics.setUserProperty("select", State.allowSelect().toString())
        analytics.setUserProperty("search", State.allowSearch().toString())
        analytics.setUserProperty("fontsize", State.getFontSize().toString())
        analytics.setUserProperty("edit", State.allowEdit().toString())
    }

    fun trackContent(type: String) {
        val params = Bundle()

        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type)

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params)
    }

    fun trackGeneric(event: String, value: String) {
        val params = Bundle()

        params.putString(FirebaseAnalytics.Param.VALUE, value)

        analytics.logEvent(event, params)
    }

    fun trackGeneric(event: String) {
        analytics.logEvent(event, Bundle())
    }

    fun trackError(event: String) {
        FirebaseCrash.log(event)
    }

}