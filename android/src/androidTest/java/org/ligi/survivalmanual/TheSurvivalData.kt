package org.ligi.survivalmanual

import android.support.test.InstrumentationRegistry
import junit.framework.Assert.fail
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.model.NavigationEntryMap
import org.ligi.survivalmanual.model.SurvivalContent

class TheSurvivalData {

    val survivalContent = SurvivalContent(InstrumentationRegistry.getTargetContext().assets)

    @Test
    fun weCanLoadAllEntriesFromNavigation() {
        NavigationEntryMap.forEach {
            val url = it.entry.url
            val tested = survivalContent.getMarkdown(url)
            if (tested == null) {
                fail("could not load $url")
            }
        }
    }

    @Test
    fun weGetNullForUnknownURL() {
        val tested = survivalContent.getMarkdown("YOLO")
        assertThat(tested).isNull()
    }

}
