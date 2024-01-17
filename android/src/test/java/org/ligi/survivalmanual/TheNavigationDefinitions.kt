package org.ligi.survivalmanual

import org.junit.Assert.*
import org.junit.Test
import org.ligi.survivalmanual.model.getTitleResByURL

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertEquals(R.string.psychology, getTitleResByURL("Psychology"))
        assertEquals(R.string.basic_medicine, getTitleResByURL("Medicine"))
        assertEquals(R.string.basic_medicine, getTitleResByURL("Medicine#foo"))
    }
}