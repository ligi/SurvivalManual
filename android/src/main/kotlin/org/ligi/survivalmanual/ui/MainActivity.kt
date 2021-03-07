package org.ligi.survivalmanual.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.GravityCompat
import org.ligi.compat.WebViewCompat
import org.ligi.kaxt.*
import org.ligi.snackengage.SnackEngage
import org.ligi.snackengage.snacks.DefaultRateSnack
import org.ligi.snackengage.snacks.RateSnack
import org.ligi.survivalmanual.R
import org.ligi.survivalmanual.R.*
import org.ligi.survivalmanual.R.id.*
import org.ligi.survivalmanual.adapter.EditingRecyclerAdapter
import org.ligi.survivalmanual.adapter.MarkdownRecyclerAdapter
import org.ligi.survivalmanual.adapter.SearchResultRecyclerAdapter
import org.ligi.survivalmanual.databinding.ActivityMainBinding
import org.ligi.survivalmanual.databinding.BookmarkBinding
import org.ligi.survivalmanual.functions.CaseInsensitiveSearch
import org.ligi.survivalmanual.functions.convertMarkdownToHtml
import org.ligi.survivalmanual.functions.isImage
import org.ligi.survivalmanual.functions.splitText
import org.ligi.survivalmanual.model.*
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates.observable

class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val drawerToggle by lazy {
        ActionBarDrawerToggle(this, binding.drawerLayout, string.drawer_open, string.drawer_close)
    }

    private val survivalContent by lazy { SurvivalContent(assets) }

    private lateinit var currentUrl: String
    private lateinit var currentTopicName: String
    private lateinit var textInput: MutableList<String>

    private var lastFontSize = State.getFontSize()
    private var lastNightMode = State.nightModeString()
    private var lastAllowSelect = State.allowSelect()

    private val linearLayoutManager by lazy { androidx.recyclerview.widget.LinearLayoutManager(this) }

    private var isInEditMode by observable(false, onChange = { _, _, newMode ->
        if (newMode) {
            binding.fab.setImageResource(drawable.ic_image_remove_red_eye)
            binding.contentRecycler.adapter = EditingRecyclerAdapter(textInput)
        } else {
            binding.fab.setImageResource(drawable.ic_editor_mode_edit)
            binding.contentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
        }

        binding.contentRecycler.scrollToPosition(State.lastScrollPos)
    })

    private fun imageWidth(): Int {
        val totalWidthPadding = (resources.getDimension(dimen.content_padding) * 2).toInt()
        return min(binding.contentRecycler.width - totalWidthPadding, binding.contentRecycler.height)
    }

    val onURLClick: (String) -> Unit = {
        if (it.startsWith("http")) {
            startActivityFromURL(it)
        } else if (!processProductLinks(it, this)) {

            if (isImage(it)) {
                startActivity(Intent(this, ImageViewActivity::class.java).apply {
                    putExtra("URL", it)
                })
            } else {
                processURL(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.drawerLayout.addDrawerListener(drawerToggle)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener { item ->
            binding.drawerLayout.closeDrawers()
            processURL(navigationEntryMap[item.itemId].entry.url)
            true
        }

        binding.contentRecycler.layoutManager = linearLayoutManager

        class RememberPositionOnScroll : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                State.lastScrollPos = (binding.contentRecycler.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition()
                super.onScrolled(recyclerView, dx, dy)
            }
        }

        binding.contentRecycler.addOnScrollListener(RememberPositionOnScroll())

        val rateSnack = DefaultRateSnack().apply {
            setActionColor(getColor(baseContext, color.colorAccentLight))
        }
        SnackEngage.from(binding.fab).withSnack(rateSnack).build().engageWhenAppropriate()

        binding.contentRecycler.post {
            val data = intent.data?.path
            if (data == null || !processURL(data.replace("/", ""))) {
                if (!processURL(State.lastVisitedURL)) {
                    processURL(State.FALLBACK_URL)
                }
            }

            isInEditMode = false
        }

        if (State.isInitialOpening) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            State.isInitialOpening = false
        }

        binding.fab.setOnClickListener {
            isInEditMode = !isInEditMode
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) = super.onPrepareOptionsMenu(menu.apply {
        findItem(action_search)?.let {
            it.isVisible = State.allowSearch()
        }
    })

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (Build.VERSION.SDK_INT >= 19) {
            menuInflater.inflate(R.menu.print, menu)
        }

        val searchView = menu.findItem(action_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(searchTerm: String): Boolean {
                State.searchTerm = searchTerm
                val adapter = binding.contentRecycler.adapter
                if (adapter is MarkdownRecyclerAdapter) {

                    val positionForWord = adapter.getPositionForWord(searchTerm)

                    if (positionForWord != null) {
                        binding.contentRecycler.smoothScrollToPosition(positionForWord)
                    } else {
                        binding.contentRecycler.adapter = SearchResultRecyclerAdapter(searchTerm, survivalContent) {
                            processURL(it)
                            closeKeyboard()
                        }.apply { showToastWhenListIsEmpty() }

                    }

                    adapter.notifyDataSetChanged()
                } else if (adapter is SearchResultRecyclerAdapter) {
                    adapter.changeTerm(searchTerm)
                    adapter.showToastWhenListIsEmpty()
                    if (survivalContent.getMarkdown(currentUrl)!!.contains(searchTerm)) {
                        binding.contentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
                        State.searchTerm = searchTerm
                    }
                }

                return true
            }

            override fun onQueryTextSubmit(query: String?) = true
        })
        return super.onCreateOptionsMenu(menu)
    }

    fun SearchResultRecyclerAdapter.showToastWhenListIsEmpty() {
        if (list.isEmpty()) {
            Toast.makeText(this@MainActivity, State.searchTerm + " not found", Toast.LENGTH_LONG).show()
        }
    }

    private fun MarkdownRecyclerAdapter.getPositionForWord(searchTerm: String): Int? {
        val first = max(linearLayoutManager.findFirstVisibleItemPosition(), 0)
        val search = CaseInsensitiveSearch(searchTerm)

        return (first..list.lastIndex).firstOrNull {
            search.isInContent(list[it])
        }
    }

    private val optionsMap = mapOf(
            menu_settings to { startActivityFromClass(PreferenceActivity::class.java) },
            menu_share to {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, RateSnack().getUri(this).toString())
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, null))
            },

            menu_rate to {
                startActivityFromURL(RateSnack().getUri(this))
            },

            menu_print to {
                AlertDialog.Builder(this)
                        .setSingleChoiceItems(arrayOf("This chapter", "Everything"), 0, null)
                        .setTitle("Print")
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            val text = when ((dialog as AlertDialog).listView.checkedItemPosition) {
                                0 -> convertMarkdownToHtml(survivalContent.getMarkdown(currentUrl)!!)
                                else -> navigationEntryMap.joinToString("<hr/>") {
                                    convertMarkdownToHtml(survivalContent.getMarkdown(it.entry.url)!!)
                                }
                            }

                            val newWebView = if (Build.VERSION.SDK_INT >= 17) {
                                WebView(createConfigurationContext(Configuration()))
                            } else {
                                WebView(this@MainActivity)
                            }

                            newWebView.webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) = false
                                override fun onPageFinished(view: WebView, url: String) = createWebPrintJob(view)
                            }


                            newWebView.loadDataWithBaseURL("file:///android_asset/md/", text, "text/HTML", "UTF-8", null)

                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()


            },
            menu_bookmark to {
                val view = BookmarkBinding.inflate(layoutInflater)
                view.topicText.text = currentTopicName
                AlertDialog.Builder(this)
                        .setView(view.root)
                        .setTitle(string.add_bookmark)
                        .setPositiveButton(string.bookmark) { _: DialogInterface, _: Int ->
                            Bookmarks.persist(Bookmark(currentUrl, view.commentEdit.text.toString(), ""))
                        }
                        .setNegativeButton(string.cancel) { _: DialogInterface, _: Int -> }
                        .show()
            }
    )

    override fun onOptionsItemSelected(item: MenuItem) = if (optionsMap.containsKey(item.itemId)) {
        (optionsMap[item.itemId] ?: error("selected item ${item.itemId} not in optionsMap")).invoke()
        true
    } else {
        drawerToggle.onOptionsItemSelected(item)
    }


    @TargetApi(19)
    private fun createWebPrintJob(webView: WebView) {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val jobName = getString(string.app_name) + " Document"
        val printAdapter = WebViewCompat.createPrintDocumentAdapter(webView, jobName)
        try {
            printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
        } catch (iae: IllegalArgumentException) {
            AlertDialog.Builder(this)
                    .setMessage("Problem printing: " + iae.message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
        }
    }

    private fun processURL(url: String): Boolean {

        binding.appbar.setExpanded(true)
        Timber.i("processing url $url")

        VisitedURLStore.add(url)
        val titleResByURL = getTitleResByURL(url) ?: return false

        currentUrl = url

        currentTopicName = getString(titleResByURL)

        supportActionBar?.subtitle = currentTopicName

        State.lastVisitedURL = url

        survivalContent.getMarkdown(currentUrl)?.let { markdown ->
            textInput = splitText(markdown)

            val newAdapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
            binding.contentRecycler.adapter = newAdapter
            if (!State.searchTerm.isNullOrBlank()) {
                newAdapter.notifyDataSetChanged()
                newAdapter.getPositionForWord(State.searchTerm!!)?.let {
                    binding.contentRecycler.scrollToPosition(it)
                }

            }
            binding.navigationView.refresh()

            return true
        }

        return false
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navigationView)) {
            binding.drawerLayout.closeDrawer(binding.navigationView)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.navigationView.refresh()
        binding.fab.setVisibility(State.allowEdit())
        if (lastFontSize != State.getFontSize()) {
            binding.contentRecycler.adapter?.notifyDataSetChanged()
            lastFontSize = State.getFontSize()
        }
        if (lastAllowSelect != State.allowSelect()) {
            recreate()
            lastAllowSelect = State.allowSelect()
        }
        if (lastNightMode != State.nightModeString()) {
            recreate()
            lastNightMode = State.nightModeString()
        }

        invalidateOptionsMenu()
    }

}
