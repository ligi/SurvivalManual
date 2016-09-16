package org.ligi.survivalmanual

import org.junit.Assert.assertEquals
import org.junit.Test

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertEquals(NavigationDefinitions.getMenuResFromURL("01.htm"), R.id.menu_intro)
        assertEquals(NavigationDefinitions.getMenuResFromURL("a.htm"), R.id.menu_appendix_kits)
        assertEquals(NavigationDefinitions.getMenuResFromURL("a.htm#foo"), R.id.menu_appendix_kits)
    }
}