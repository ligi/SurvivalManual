package org.ligi.survivalmanual.ui

import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.survivalmanual.R

class PreferenceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prefs)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (org.ligi.survivalmanual.model.State.nightModeString() == "darknight") {
            window.decorView.rootView.setBackgroundColor(0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
