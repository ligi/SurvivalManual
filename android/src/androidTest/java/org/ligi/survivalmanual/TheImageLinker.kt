package org.ligi.survivalmanual

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.ligi.survivalmanual.functions.linkImagesInMarkDown
@RunWith(AndroidJUnit4::class)
class TheImageLinker {

    @Test
    fun testLinkingWorks() {
        assertEquals("foo [![Figure 4-7 Butterfly Closure](fig04-07.png)](fig04-07.png)", linkImagesInMarkDown("foo ![Figure 4-7 Butterfly Closure](fig04-07.png)"))
    }

    @Test
    fun testLinkingWorksForMoreThanOneImage() {
        assertEquals("foo [![Figure 4-7 Butterfly Closure](fig04-07.png)](fig04-07.png) bar [![Figure 5-8 Butterfly Closure](fig05-08.png)](fig05-08.png) yo", linkImagesInMarkDown("foo ![Figure 4-7 Butterfly Closure](fig04-07.png) bar ![Figure 5-8 Butterfly Closure](fig05-08.png) yo"))
    }
}