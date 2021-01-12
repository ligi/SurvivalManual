package org.ligi.survivalmanual.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import org.ligi.kaxt.inflate
import org.ligi.kaxt.startActivityFromURL
import org.ligi.survivalmanual.R

val PRODUCT_MAP = mapOf(
        "SolarUSBCharger" to "B01EXWCPLC/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B01EXWCPLC&linkCode=as2&tag=ligi-20&linkId=91a10115f5e5cfae9b2529ca1a96c074",
        "CampStoveUSB" to "B00BQHET9O/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B00BQHET9O&linkCode=as2&tag=ligi-20&linkId=d949a1aca04d67b5e61d3bf77ce89d22",
        "HandCrankUSB" to "B08D9KLNV3/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B08D9KLNV3&linkCode=as2&tag=ligi-20&linkId=2f8f83d3bb8ec615be3f8849abf751c2",
        "CarUSBCharger" to "B00VH84L5E/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B00VH84L5E&linkCode=as2&tag=ligi-20&linkId=41a56b9c800ed019a0af367a49050502",
        "OHTMultiTool" to "B008069YXA/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B008069YXA&linkCode=as2&tag=ligi-20&linkId=a286d415f362e4650652f63a4dae9a3e",
        "LifeStraw" to "B006QF3TW4/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B006QF3TW4&linkCode=as2&tag=ligi-20&linkId=140954cab61f21b92f27217172f3ec35",
        "TreadMultiTool" to "B018IOXXP8/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B018IOXXP8&linkCode=as2&tag=ligi-20&linkId=1c14c69653606e308b9f4b98372a5a51",
        "PandaDubLionsDen" to "B00F48QJUS/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B00F48QJUS&linkCode=as2&tag=ligi-20&linkId=3e9b42f0dee5e03da96ea1f14be223ea",
        "Audible" to "B00NB86OYE/?ref_=assoc_tag_ph_1485906643682&_encoding=UTF8&camp=1789&creative=9325&linkCode=pf4&tag=ligi-20&linkId=38dacab0a96e0ecaa625fa375f70c517"
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