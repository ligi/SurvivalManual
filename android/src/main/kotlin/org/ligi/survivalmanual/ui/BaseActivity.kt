package org.ligi.survivalmanual.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor

abstract class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        if (org.ligi.survivalmanual.model.State.nightModeString() == "darknight") {
            window.decorView.rootView.setBackgroundColor(getColor(this, android.R.color.black))
        }
    }
}
