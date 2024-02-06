package org.ligi.survivalmanual.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import org.ligi.survivalmanual.model.navigationEntryMap

class MyNavigationView(context: Context, attrs: AttributeSet) : NavigationView(context, attrs) {

    init {
        refresh()
    }

    fun refresh() {
        menu.clear()

        val listedItems = navigationEntryMap.filter { it.entry.isListed }

        listedItems.filter { !it.entry.isAppendix }.forEach { nav ->
            menu.add(0, nav.id, Menu.NONE, context.getString(nav.entry.titleRes)).apply {
                nav.entry.iconRes?.let { setIcon(it) }
            }
        }

        val submenu = menu.addSubMenu("Appendix")

        listedItems.filter { it.entry.isAppendix }.forEach {
            submenu.add(0, it.id, Menu.NONE, context.getString(it.entry.titleRes))
        }
    }
}
