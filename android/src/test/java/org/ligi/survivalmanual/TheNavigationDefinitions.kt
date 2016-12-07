package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.model.NavigationDefinitions

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertThat(NavigationDefinitions.getTitleResByURL("02")).isEqualTo(R.string.psychology)
        assertThat(NavigationDefinitions.getTitleResByURL("a")).isEqualTo(R.string.kits)
        assertThat(NavigationDefinitions.getTitleResByURL("a#foo")).isEqualTo(R.string.kits)
    }
}