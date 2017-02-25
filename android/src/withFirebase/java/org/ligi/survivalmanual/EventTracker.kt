package org.ligi.survivalmanual

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object EventTracker {

    lateinit var context: Context

    val analytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    fun init(context: Context) {
        this.context = context
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
}