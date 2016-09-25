package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertThat(NavigationDefinitions.getMenuResFromURL("01")).isEqualTo(R.id.menu_intro)
        assertThat(NavigationDefinitions.getMenuResFromURL("a")).isEqualTo(R.id.menu_appendix_kits)
        assertThat(NavigationDefinitions.getMenuResFromURL("a#foo")).isEqualTo(R.id.menu_appendix_kits)
    }
}