package org.ligi.survivalmanual.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import org.ligi.survivalmanual.R
import org.ligi.survivalmanual.databinding.ActivityPrefsBinding
import org.ligi.survivalmanual.model.State

class PreferenceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityPrefsBinding = ActivityPrefsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.prefsToolbar.toolbar)
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
