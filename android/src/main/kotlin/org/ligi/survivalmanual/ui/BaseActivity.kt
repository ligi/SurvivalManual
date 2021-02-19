package org.ligi.survivalmanual.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import org.ligi.survivalmanual.model.State

abstract class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        if (State.nightModeString() == "darknight") {
            window.decorView.rootView.setBackgroundColor(getColor(this, android.R.color.black))
        }
    }
}
