package org.ligi.survivalmanual.ui

import android.content.Context
import android.support.design.widget.NavigationView
import android.util.AttributeSet
import android.view.Menu
import org.ligi.survivalmanual.model.NavigationDefinitions

class MyNavigationView(context: Context, attrs: AttributeSet) : NavigationView(context, attrs) {

    init {

        val listedItems = NavigationDefinitions.content.filter { it.entry.isListed }

        listedItems.filter { !it.entry.isAppendix }.forEach {
            menu.add(0, it.id, Menu.NONE, it.entry.titleRes).apply {
                it.entry.iconRes?.let { setIcon(it) }
            }
        }

        val submenu = menu.addSubMenu("Appendix")

        listedItems.filter { it.entry.isAppendix }.forEach {
            submenu.add(0, it.id, Menu.NONE, it.entry.titleRes)
        }
    }
}
