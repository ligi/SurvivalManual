package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.model.getTitleResByURL

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertThat(getTitleResByURL("Psychology")).isEqualTo(R.string.psychology)
        assertThat(getTitleResByURL("Clouds")).isEqualTo(R.string.clouds)
        assertThat(getTitleResByURL("Clouds#foo")).isEqualTo(R.string.clouds)
    }
}