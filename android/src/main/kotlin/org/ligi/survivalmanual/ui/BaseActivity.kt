package org.ligi.survivalmanual.ui

import android.R
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        if (org.ligi.survivalmanual.model.State.nightModeString() == "darknight") {
            window.decorView.rootView.setBackgroundColor(getColor(this, R.color.black))
        }
    }
}
