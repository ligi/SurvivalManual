package org.ligi.survivalmanual

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.QuoteSpan
import android.text.style.URLSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.rjeschke.txtmark.Processor
import org.ligi.snackengage.SnackEngage
import org.ligi.snackengage.snacks.DefaultRateSnack

class MainActivity : AppCompatActivity() {

    @VisibleForTesting
    val textView by lazy { findViewById(R.id.textView) as TextView }

    val content by lazy { findViewById(R.id.content) as ViewGroup }

    private val drawerLayout by lazy { findViewById(R.id.drawer_layout) as DrawerLayout }
    private val drawerToggle by lazy { ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout.addDrawerListener(drawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView.movementMethod = LinkMovementMethod.getInstance()

        val navigationView = findViewById(R.id.navigationView) as NavigationView

        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            processMenuId(item.itemId)
            true
        }

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

        class LoadAsyncTask : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String {
                return Processor.process(assets.open(urlByMenuId).bufferedReader().readText())
            }

            override fun onPostExecute(result: String) {
                textView.text = result
                setTextViewHTML(textView, result)
            }
        }
        LoadAsyncTask().execute()

        supportActionBar?.setSubtitle(NavigationDefinitions.getTitleResById(menuId))
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                val menuId = NavigationDefinitions.getMenuResFromURL(span.url)
                if (menuId != null) {
                    processMenuId(menuId)
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    private fun replaceQuoteSpans(spannable: Spannable) {
        val quoteSpans = spannable.getSpans(0, spannable.length, QuoteSpan::class.java)
        for (quoteSpan in quoteSpans) {
            val start = spannable.getSpanStart(quoteSpan)
            val end = spannable.getSpanEnd(quoteSpan)
            val flags = spannable.getSpanFlags(quoteSpan)
            spannable.removeSpan(quoteSpan)
            spannable.setSpan(CustomQuoteSpan(this), start, end, flags)
        }
    }

    private fun setTextViewHTML(text: TextView, html: String) {
        class CustomImageGetter : Html.ImageGetter {
            override fun getDrawable(source: String?): Drawable {

                val bitmapDrawable = BitmapDrawable.createFromStream(assets.open("md/" + source), source) as BitmapDrawable

                val ratio = bitmapDrawable.bitmap.height.toFloat() / bitmapDrawable.bitmap.width

                val width = Math.min(content.width, content.height)
                bitmapDrawable.setBounds(0, 0, width, (width * ratio).toInt())
                return bitmapDrawable
            }

        }

        val sequence = Html.fromHtml(html, CustomImageGetter(), null)
        val spannable = SpannableStringBuilder(sequence)
        val urls = spannable.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {

            if (NavigationDefinitions.getMenuResFromURL(span.url) != null) {
                makeLinkClickable(spannable, span)
            } else {
                if (!span.url.startsWith("#")) {
                    throw(Exception("Err cannot handle " + span.url))
                }
            }
        }

        replaceQuoteSpans(spannable)
        text.text = spannable
        text.movementMethod = LinkMovementMethod.getInstance()
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
