package org.ligi.survivalmanual.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import org.ligi.kaxt.inflate
import org.ligi.kaxt.startActivityFromURL
import org.ligi.survivalmanual.R

val PRODUCT_MAP = mapOf(
        "SolarUSBCharger" to "B012YUJJM8",
        "CampStoveUSB" to "B00BQHET9O",
        "HandCrankUSB" to "B01AD7IN4O",
        "CarUSBCharger" to "B00VH84L5E",
        "OHTMultiTool" to "B008P8EYWE",
        "LifeStraw" to "B006QF3TW4",
        "TreadMultiTool" to "B018IOXXP8",
        "PandaDubLionsDen" to "B00F48QJUS",
        "Audible" to "B00NB86OYE"
)

fun processProductLinks(it: String, activity: Activity): Boolean {

    if (PRODUCT_MAP.containsKey(it)) {
        val url = "https://www.amazon.com/gp/product/" + PRODUCT_MAP[it]
        val view = activity.inflate(R.layout.alert_product_link)

        AlertDialog.Builder(activity)
                .setTitle(R.string.amazon_link_title)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.to_amazon) { _: DialogInterface, _: Int ->
                    activity.startActivityFromURL(url)
                }
                .setNeutralButton(R.string.send_link) { _: DialogInterface, _: Int ->
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, url)
                        type = "text/plain"
                    }
                    activity.startActivity(Intent.createChooser(sendIntent, "Send link to"))
                }
                .show()
        return true
    } else {
        return false
    }
}