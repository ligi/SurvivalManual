package org.ligi.survivalmanual.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.survivalmanual.R
import org.ligi.survivalmanual.model.State

class PreferenceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prefs)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (State.nightModeString() == "darknight") {
            window.decorView.rootView.setBackgroundColor(0)
        }

        supportFragmentManager.commit {
            replace<PreferencesFragment>(R.id.prefs_container)
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
