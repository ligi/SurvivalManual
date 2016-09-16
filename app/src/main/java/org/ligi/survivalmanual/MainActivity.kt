package org.ligi.survivalmanual

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {

    private val webView by lazy { findViewById(R.id.webView) as WebView }
    private val drawerLayout by lazy { findViewById(R.id.drawer_layout) as DrawerLayout }
    private val drawerToggle by lazy { ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (url != null && url.startsWith("survival://")) {
                    processMenuId(NavigationDefinitions.getMenuResFromURL(url))
                    return true
                } else {
                    return false
                }
            }

        })
        val navigationView = findViewById(R.id.navigationView) as NavigationView

        navigationView.setNavigationItemSelectedListener { item -> processMenuId(item.itemId) }

        processMenuId(R.id.menu_intro)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return drawerToggle.onOptionsItemSelected(item)
    }

    private fun processMenuId(menuId: Int): Boolean {
        val urlByMenuId = getURLByMenuId(menuId)
        if (urlByMenuId != null) {
            webView.loadUrl(urlByMenuId)
            drawerLayout.closeDrawers()
            supportActionBar?.setSubtitle(NavigationDefinitions.getTitleResById(menuId))
            return true
        }
        return false
    }

    private fun getURLByMenuId(menuId: Int): String? {
        val urlFragmentByMenuId = NavigationDefinitions.menu2htmlMap[menuId]
        if (urlFragmentByMenuId != null) {
            return "file:///android_asset/html/$urlFragmentByMenuId.htm"
        }
        return null
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }
}
