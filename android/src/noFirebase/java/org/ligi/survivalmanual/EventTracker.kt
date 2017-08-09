package org.ligi.survivalmanual

import android.content.Context

object EventTracker {
    fun init(context: Context) {}
    fun trackContent(id: String) {}
    fun trackGeneric(event: String, value: String) {}
    fun trackGeneric(event: String) {}
    fun trackError(event: String) {}
}