package org.ligi.survivalmanual

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.ligi.survivalmanual.model.getTitleResByURL

class TheNavigationDefinitions {

    @Test
    fun getMenuResFromURLWorks() {
        assertThat(getTitleResByURL("02")).isEqualTo(R.string.psychology)
        assertThat(getTitleResByURL("b")).isEqualTo(R.string.edible_medicin_plants)
        assertThat(getTitleResByURL("b#foo")).isEqualTo(R.string.edible_medicin_plants)
    }
}