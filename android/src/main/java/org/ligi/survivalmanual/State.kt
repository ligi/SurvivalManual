package org.ligi.survivalmanual

import com.chibatching.kotpref.KotprefModel

object State : KotprefModel() {

    var lastVisitedSite : String by stringPrefVar("01")
    var lastScroll : Int by intPrefVar(0)
}
