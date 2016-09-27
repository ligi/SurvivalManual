package org.ligi.survivalmanual

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
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
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById(R.id.navigationView) as NavigationView

        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            processMenuId(item.itemId)
            true
        }

        recycler.layoutManager = LinearLayoutManager(this)

        class RememberPositionOnScroll : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                State.lastScroll = (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                super.onScrolled(recyclerView, dx, dy)
            }
        }

        recycler.addOnScrollListener(RememberPositionOnScroll())

        SnackEngage.from(this).withSnack(DefaultRateSnack()).build().engageWhenAppropriate()

        recycler.post {
            processMenuId(NavigationDefinitions.getMenuResFromURL(State.lastVisitedSite)!!)
            recycler.scrollToPosition(State.lastScroll)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_help -> {
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

            R.id.menu_daynight -> {
                var newNightMode: Int? = null

                AlertDialog.Builder(this)
                        .setTitle(R.string.daynight)

                        .setSingleChoiceItems(R.array.daynight_options, State.dayNightMode, { dialogInterface: DialogInterface, i: Int ->
                            newNightMode = i
                        })
                        .setPositiveButton(android.R.string.ok, { dialogInterface: DialogInterface, i: Int ->
                            if (newNightMode != null) {
                                State.dayNightMode = newNightMode!!
                                State.applyDayNightMode()
                                if (Build.VERSION.SDK_INT >= 11) {
                                    recreate()
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()


                return true
            }

            R.id.menu_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, null))
                return true
            }

            R.id.menu_rate -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
                startActivity(intent)
                return true
            }

        }

        return drawerToggle.onOptionsItemSelected(item)
    }

    private fun processMenuId(menuId: Int) {
        val urlByMenuId = getURLByMenuId(menuId)
        State.lastVisitedSite = NavigationDefinitions.menu2htmlMap[menuId]!!
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
