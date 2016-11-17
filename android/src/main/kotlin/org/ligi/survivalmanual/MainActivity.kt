package org.ligi.survivalmanual

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.design.widget.NavigationView
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
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.compat.HtmlCompat
import org.ligi.compat.WebViewCompat
import org.ligi.kaxt.setVisibility
import org.ligi.kaxt.startActivityFromClass
import org.ligi.snackengage.SnackEngage
import org.ligi.snackengage.snacks.DefaultRateSnack
import org.ligi.survivalmanual.ImageLogic.isImage

class MainActivity : AppCompatActivity() {

    private val drawerToggle by lazy { ActionBarDrawerToggle(this, drawer_layout, R.string.drawer_open, R.string.drawer_close) }

    lateinit var currentUrl: String
    lateinit var textInput: MutableList<String>

    fun imageWidth(): Int {
        val totalWidthPadding = (resources.getDimension(R.dimen.content_padding) * 2).toInt()
        return Math.min(contentRecycler.width - totalWidthPadding, contentRecycler.height)
    }

    val onURLClick: (String) -> Unit = {
        supportActionBar?.subtitle?.let { subtitle ->
            EventTracker.trackContent(it, subtitle.toString(), "clicked_in_text")
        }

        if (isImage(it)) {
            val intent = Intent(this, ImageViewActivity::class.java)
            intent.putExtra("URL", it)
            startActivity(intent)
        } else {
            NavigationDefinitions.getMenuResFromURL(it)?.let { processMenuId(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer_layout.addDrawerListener(drawerToggle)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById(R.id.navigationView) as NavigationView

        navigationView.setNavigationItemSelectedListener { item ->
            drawer_layout.closeDrawers()
            processMenuId(item.itemId)
            true
        }

        contentRecycler.layoutManager = LinearLayoutManager(this)

        class RememberPositionOnScroll : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                State.lastScroll = (contentRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                super.onScrolled(recyclerView, dx, dy)
            }
        }

        contentRecycler.addOnScrollListener(RememberPositionOnScroll())

        SnackEngage.from(this).withSnack(DefaultRateSnack()).build().engageWhenAppropriate()

        contentRecycler.post {
            processMenuId(NavigationDefinitions.getMenuResFromURL(State.lastVisitedSite)!!)
            switchMode(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (Build.VERSION.SDK_INT >= 19) {
            menuInflater.inflate(R.menu.print, menu)
        }
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

        R.id.menu_settings -> {
            startActivityFromClass(PreferenceActivity::class.java)
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
            EventTracker.trackGeneric("print", currentUrl)
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

    private fun processMenuId(menuId: Int) {
        currentUrl = getURLByMenuId(menuId)
        val newTitle = getString(NavigationDefinitions.getTitleResById(menuId))
        EventTracker.trackContent(getURLByMenuId(menuId), newTitle, "processMenuId")

        supportActionBar?.subtitle = newTitle

        State.lastVisitedSite = NavigationDefinitions.menu2htmlMap[menuId]!!

        textInput = TextSplitter.split(assets.open(currentUrl))

        contentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
    }

    private fun getURLByMenuId(menuId: Int): String {
        val urlFragmentByMenuId = NavigationDefinitions.menu2htmlMap[menuId]
        return "md/$urlFragmentByMenuId.md"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onResume() {
        super.onResume()
        fab.setVisibility(State.allowEdit())
        contentRecycler.adapter.notifyDataSetChanged() // to apply maybe changed font size
    }

    fun switchMode(editing: Boolean) {
        if (editing) {
            fab.setOnClickListener {
                switchMode(false)
            }

            fab.setImageResource(R.drawable.ic_image_remove_red_eye)
            contentRecycler.adapter = EditingRecyclerAdapter(textInput)
        } else {
            fab.setOnClickListener {
                switchMode(true)
            }
            fab.setImageResource(R.drawable.ic_editor_mode_edit)
            contentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
        }

        contentRecycler.scrollToPosition(State.lastScroll)

    }
}
