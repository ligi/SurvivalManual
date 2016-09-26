package org.ligi.survivalmanual

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import okio.Okio
import org.ligi.snackengage.SnackEngage
import org.ligi.snackengage.snacks.DefaultRateSnack

class MainActivity : AppCompatActivity() {

    val recycler by lazy { findViewById(R.id.contentRecycler) as RecyclerView }

    private val drawerLayout by lazy { findViewById(R.id.drawer_layout) as DrawerLayout }
    private val drawerToggle by lazy { ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val navigationView = findViewById(R.id.navigationView) as NavigationView

        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            processMenuId(item.itemId)
            true
        }

        recycler.layoutManager = LinearLayoutManager(this)

        processMenuId(R.id.menu_intro)

        SnackEngage.from(this).withSnack(DefaultRateSnack()).build().engageWhenAppropriate()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_help) {
            val textView = TextView(this)
            val helpText = getString(R.string.help_text).replace("\$VERSION", BuildConfig.VERSION_NAME)
            textView.text = Html.fromHtml(helpText)
            textView.movementMethod = LinkMovementMethod.getInstance()
            val padding = resources.getDimensionPixelSize(R.dimen.help_padding)
            textView.setPadding(padding, padding, padding, padding)
            AlertDialog.Builder(this)
                    .setTitle(R.string.help_title)
                    .setView(textView)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            return true
        }
        return drawerToggle.onOptionsItemSelected(item)
    }

    private fun processMenuId(menuId: Int) {
        val urlByMenuId = getURLByMenuId(menuId)
        val imageWidth = Math.min(recycler.width, recycler.height)
        recycler.adapter = MarkdownRecyclerAdapter(Okio.buffer(Okio.source(assets.open(urlByMenuId))), imageWidth, {
            val menuId = NavigationDefinitions.getMenuResFromURL(it)
            if (menuId != null) {
                processMenuId(menuId)
            }

        })
        supportActionBar?.setSubtitle(NavigationDefinitions.getTitleResById(menuId))
    }


    private fun getURLByMenuId(menuId: Int): String {
        val urlFragmentByMenuId = NavigationDefinitions.menu2htmlMap[menuId]
        return "md/$urlFragmentByMenuId.md"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }
}
