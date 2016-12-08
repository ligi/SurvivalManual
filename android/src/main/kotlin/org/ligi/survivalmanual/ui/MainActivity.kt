package org.ligi.survivalmanual.ui

import android.R
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.design.widget.NavigationView
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import com.github.rjeschke.txtmark.Processor
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.compat.HtmlCompat
import org.ligi.compat.WebViewCompat
import org.ligi.kaxt.*
import org.ligi.snackengage.SnackEngage
import org.ligi.snackengage.snacks.DefaultRateSnack
import org.ligi.survivalmanual.BuildConfig
import org.ligi.survivalmanual.EventTracker
import org.ligi.survivalmanual.adapter.EditingRecyclerAdapter
import org.ligi.survivalmanual.adapter.MarkdownRecyclerAdapter
import org.ligi.survivalmanual.adapter.SearchResultRecyclerAdapter
import org.ligi.survivalmanual.functions.isImage
import org.ligi.survivalmanual.functions.splitText
import org.ligi.survivalmanual.model.NavigationDefinitions
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.model.SurvivalContent

class MainActivity : AppCompatActivity() {

    private val drawerToggle by lazy { ActionBarDrawerToggle(this, drawer_layout, org.ligi.survivalmanual.R.string.drawer_open, org.ligi.survivalmanual.R.string.drawer_close) }

    val survivalContent by lazy { SurvivalContent(assets) }

    lateinit var currentUrl: String
    lateinit var textInput: MutableList<String>

    fun imageWidth(): Int {
        val totalWidthPadding = (resources.getDimension(org.ligi.survivalmanual.R.dimen.content_padding) * 2).toInt()
        return Math.min(contentRecycler.width - totalWidthPadding, contentRecycler.height)
    }

    val onURLClick: (String) -> Unit = {
        supportActionBar?.subtitle?.let { subtitle ->
            EventTracker.trackContent(it, subtitle.toString())
        }

        if (isImage(it)) {
            val intent = Intent(this, ImageViewActivity::class.java)
            intent.putExtra("URL", it)
            startActivity(intent)
        } else {
            processURL(it)
        }
    }

    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.ligi.survivalmanual.R.layout.activity_main)

        drawer_layout.addDrawerListener(drawerToggle)
        setSupportActionBar(findViewById(org.ligi.survivalmanual.R.id.toolbar) as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById(org.ligi.survivalmanual.R.id.navigationView) as NavigationView

        navigationView.setNavigationItemSelectedListener { item ->
            drawer_layout.closeDrawers()
            processURL(NavigationDefinitions.content[item.itemId].entry.url)
            true
        }

        contentRecycler.layoutManager = linearLayoutManager

        class RememberPositionOnScroll : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                State.lastScrollPos = (contentRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                super.onScrolled(recyclerView, dx, dy)
            }
        }

        contentRecycler.addOnScrollListener(RememberPositionOnScroll())

        SnackEngage.from(this).withSnack(DefaultRateSnack()).build().engageWhenAppropriate()

        contentRecycler.post {
            if (intent.data == null || !processURL(intent.data.path.replace("/", ""))) {
                if (!processURL(State.lastVisitedURL)) {
                    processURL(State.FALLBACK_URL)
                }
            }

            switchMode(false)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(org.ligi.survivalmanual.R.id.action_search).isVisible = State.allowSearch()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(org.ligi.survivalmanual.R.menu.main, menu)
        if (Build.VERSION.SDK_INT >= 19) {
            menuInflater.inflate(org.ligi.survivalmanual.R.menu.print, menu)
        }

        val searchView = MenuItemCompat.getActionView(menu.findItem(org.ligi.survivalmanual.R.id.action_search)) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(searchTerm: String): Boolean {
                State.searchTerm = searchTerm
                val adapter = contentRecycler.adapter
                if (adapter is MarkdownRecyclerAdapter) {

                    val positionForWord = adapter.getPositionForWord(searchTerm)

                    if (positionForWord != null) {
                        contentRecycler.smoothScrollToPosition(positionForWord)
                    } else {
                        contentRecycler.adapter = SearchResultRecyclerAdapter(searchTerm, survivalContent, {
                            processURL(it)
                            closeKeyboard()
                        }).toast()

                    }

                    adapter.notifyDataSetChanged()
                } else if (adapter is SearchResultRecyclerAdapter) {
                    adapter.changeTerm(searchTerm)
                    adapter.toast()
                    if (survivalContent.getMarkdown(currentUrl).contains(searchTerm)) {
                        contentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
                        State.searchTerm = searchTerm
                    }
                }

                return true
            }

            override fun onQueryTextSubmit(query: String?) = true
        })
        return super.onCreateOptionsMenu(menu)
    }

    fun SearchResultRecyclerAdapter.toast(): SearchResultRecyclerAdapter {
        if (this.list.isEmpty()) {
            Toast.makeText(this@MainActivity, State.searchTerm + " not found", Toast.LENGTH_LONG).show()
        }
        return this
    }

    private fun MarkdownRecyclerAdapter.getPositionForWord(searchTerm: String): Int? {
        val first = Math.max(linearLayoutManager.findFirstVisibleItemPosition(), 0)
        val searchRegex = Regex("(?i)$searchTerm")

        val next = (first..list.lastIndex).firstOrNull {
            list[it].contains(searchRegex)
        }
        return next
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        org.ligi.survivalmanual.R.id.menu_help -> {
            EventTracker.trackGeneric("help")
            val textView = TextView(this)
            val helpText = getString(org.ligi.survivalmanual.R.string.help_text).replace("\$VERSION", BuildConfig.VERSION_NAME)
            textView.text = HtmlCompat.fromHtml(helpText)
            textView.movementMethod = LinkMovementMethod.getInstance()
            val padding = resources.getDimensionPixelSize(org.ligi.survivalmanual.R.dimen.help_padding)
            textView.setPadding(padding, padding, padding, padding)
            AlertDialog.Builder(this)
                    .setTitle(org.ligi.survivalmanual.R.string.help_title)
                    .setView(textView)
                    .setPositiveButton(R.string.ok, null)
                    .show()
            true
        }

        org.ligi.survivalmanual.R.id.menu_settings -> {
            startActivityFromClass(PreferenceActivity::class.java)
            true
        }

        org.ligi.survivalmanual.R.id.menu_share -> {
            EventTracker.trackGeneric("share")
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, null))
            true
        }

        org.ligi.survivalmanual.R.id.menu_rate -> {
            EventTracker.trackGeneric("rate")
            startActivityFromURL("market://details?id=" + BuildConfig.APPLICATION_ID)
            true
        }

        org.ligi.survivalmanual.R.id.menu_print -> {
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
        val jobName = getString(org.ligi.survivalmanual.R.string.app_name) + " Document"
        val printAdapter = WebViewCompat.createPrintDocumentAdapter(webView, jobName)
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }

    private fun processURL(url: String): Boolean {

        val titleResByURL = NavigationDefinitions.getTitleResByURL(url) ?: return false

        currentUrl = url

        val newTitle = getString(titleResByURL)
        EventTracker.trackContent(url, newTitle)

        supportActionBar?.subtitle = newTitle

        State.lastVisitedURL = url

        textInput = splitText(survivalContent.getMarkdown(currentUrl))

        val newAdapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
        contentRecycler.adapter = newAdapter
        if (!State.searchTerm.isNullOrBlank()) {
            newAdapter.notifyDataSetChanged()
            newAdapter.getPositionForWord(State.searchTerm!!)?.let {
                contentRecycler.scrollToPosition(it)
            }

        }
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    var lastFontSize = State.getFontSize()
    var lastNightMode = State.getNightMode()

    override fun onResume() {
        super.onResume()
        fab.setVisibility(State.allowEdit())
        if (lastFontSize != State.getFontSize()) {
            contentRecycler.adapter?.notifyDataSetChanged()
            lastFontSize = State.getFontSize()
        }
        if (lastNightMode != State.getNightMode()) {
            recreateWhenPossible()
            lastNightMode = State.getNightMode()
        }

        supportInvalidateOptionsMenu()
    }

    fun switchMode(editing: Boolean) {
        fab.setOnClickListener {
            switchMode(!editing)
        }

        if (editing) {
            fab.setImageResource(org.ligi.survivalmanual.R.drawable.ic_image_remove_red_eye)
            contentRecycler.adapter = EditingRecyclerAdapter(textInput)
        } else {
            fab.setImageResource(org.ligi.survivalmanual.R.drawable.ic_editor_mode_edit)
            contentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
        }

        contentRecycler.scrollToPosition(State.lastScrollPos)

    }
}
