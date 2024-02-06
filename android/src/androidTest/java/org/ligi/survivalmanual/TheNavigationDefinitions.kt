package org.ligi.survivalmanual

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.model.getTitleResByURL
@RunWith(AndroidJUnit4::class)
class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertEquals(R.string.psychology, getTitleResByURL("Psychology"))
        assertEquals(R.string.basic_medicine, getTitleResByURL("Medicine"))
        assertEquals(R.string.basic_medicine, getTitleResByURL("Medicine#foo"))
    }
}