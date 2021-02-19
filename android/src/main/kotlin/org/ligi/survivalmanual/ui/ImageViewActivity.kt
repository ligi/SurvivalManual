package org.ligi.survivalmanual.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.ligi.survivalmanual.databinding.ActivityImageBinding
import org.ligi.survivalmanual.functions.getSurvivalDrawable


class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityImageBinding = ActivityImageBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.imageToolbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val source = intent.getStringExtra("URL")
                ?: throw IllegalArgumentException("ImageViewActivity called without URL extra")
        val bitmap = getSurvivalDrawable(this, source)
        binding.imageTouchView.setImageDrawable(bitmap)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)

    }
}
