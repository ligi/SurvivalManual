package org.ligi.survivalmanual

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.github.rjeschke.txtmark.Processor
import org.ligi.compat.HtmlCompat
import org.ligi.compat.WebViewCompat
import org.ligi.snackengage.SnackEngage
import org.ligi.snackengage.snacks.DefaultRateSnack
import org.ligi.survivalmanual.ImageLogic.isImage

class MainActivity : AppCompatActivity() {

    val recycler by lazy { findViewById(R.id.contentRecycler) as RecyclerView }

    private val drawerLayout by lazy { findViewById(R.id.drawer_layout) as DrawerLayout }
    private val drawerToggle by lazy { ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) }

    var currentUrl: String? = null

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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_help -> {
            EventTracker.trackGeneric("help")
            val textView = TextView(this)
            val helpText = getString(R.string.help_text).replace("\$VERSION", BuildConfig.VERSION_NAME)
            textView.text = HtmlCompat.fromHtml(helpText)
            textView.movementMethod = LinkMovementMethod.getInstance()
            val padding = resources.getDimensionPixelSize(R.dimen.help_padding)
            textView.setPadding(padding, padding, padding, padding)
            AlertDialog.Builder(this)
                    .setTitle(R.string.help_title)
                    .setView(textView)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            true
        }

        R.id.menu_daynight -> {
            EventTracker.trackGeneric("daynight_open")
            var newNightMode: Int? = null

            AlertDialog.Builder(this)
                    .setTitle(R.string.daynight)

                    .setSingleChoiceItems(R.array.daynight_options, State.dayNightMode, { dialogInterface: DialogInterface, i: Int ->
                        newNightMode = i
                    })
                    .setPositiveButton(android.R.string.ok, { dialogInterface: DialogInterface, i: Int ->
                        newNightMode?.let {
                            EventTracker.trackGeneric("select_night_mode", it.toString())
                            State.dayNightMode = it
                            State.applyDayNightMode()
                            if (Build.VERSION.SDK_INT >= 11) {
                                recreate()
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            true
        }

        R.id.menu_share -> {
            EventTracker.trackGeneric("share")
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, null))
            true
        }

        R.id.menu_rate -> {
            EventTracker.trackGeneric("rate")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
            startActivity(intent)

            true
        }

        R.id.menu_print -> {
            EventTracker.trackGeneric("print", currentUrl!!)
            val newWebView = WebView(this@MainActivity)
            newWebView.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) = false
                override fun onPageFinished(view: WebView, url: String) = createWebPrintJob(view)
            })

            val htmlDocument = Processor.process(assets.open(currentUrl).reader().readText())
            newWebView.loadDataWithBaseURL("file:///android_asset/md/", htmlDocument, "text/HTML", "UTF-8", null)

            true
        }

        else -> drawerToggle.onOptionsItemSelected(item)
    }

    @TargetApi(19)
    private fun createWebPrintJob(webView: WebView) {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = getString(R.string.app_name) + " Document"
        val printAdapter = WebViewCompat.createPrintDocumentAdapter(webView, jobName)
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.menu_print).isVisible = Build.VERSION.SDK_INT >= 19
        return super.onPrepareOptionsMenu(menu)
    }

    private fun processMenuId(menuId: Int) {
        currentUrl = getURLByMenuId(menuId)
        val newTitle = getString(NavigationDefinitions.getTitleResById(menuId))
        EventTracker.trackContent(getURLByMenuId(menuId), newTitle, "processMenuId")

        supportActionBar?.subtitle = newTitle

        State.lastVisitedSite = NavigationDefinitions.menu2htmlMap[menuId]!!
        val totalWidthPadding = (resources.getDimension(R.dimen.content_padding) * 2).toInt()
        val imageWidth = Math.min(recycler.width - totalWidthPadding, recycler.height)
        val textInput = assets.open(currentUrl)
        val onURLClick: (String) -> Unit = {
            EventTracker.trackContent(it, newTitle, "clicked_in_text")
            if (isImage(it)) {
                val intent = Intent(this, ImageViewActivity::class.java)
                intent.putExtra("URL", it)
                startActivity(intent)
            } else {
                NavigationDefinitions.getMenuResFromURL(it)?.let { processMenuId(it) }
            }
        }
        recycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth, onURLClick)

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
