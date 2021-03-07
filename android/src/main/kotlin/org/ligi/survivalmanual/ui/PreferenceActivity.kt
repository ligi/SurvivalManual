package org.ligi.survivalmanual.ui

import android.os.Bundle
import android.view.MenuItem
import org.ligi.survivalmanual.databinding.ActivityPrefsBinding

class PreferenceActivity : BaseActivity() {

    private val binding by lazy { ActivityPrefsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
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
