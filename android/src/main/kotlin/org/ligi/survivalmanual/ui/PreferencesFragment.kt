package org.ligi.survivalmanual.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO
import androidx.preference.PreferenceFragmentCompat
import org.ligi.survivalmanual.R
import org.ligi.survivalmanual.model.State
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class PreferencesFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.preference_key_nightmode)) {

            @AppCompatDelegate.NightMode val nightMode = State.getNightMode()

            if (nightMode == MODE_NIGHT_AUTO) {
                ensureDayNightWithPermissionCheck()
            }

            AppCompatDelegate.setDefaultNightMode(nightMode)
            activity?.recreate()
        }
    }


    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    fun ensureDayNight() {
        // Intentionally empty
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

}
