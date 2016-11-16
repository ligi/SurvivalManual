package org.ligi.survivalmanual;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceFragmentCompat;
import org.jetbrains.annotations.NotNull;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO;

@RuntimePermissions
public class PreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_key_nightmode))) {

            @AppCompatDelegate.NightMode final int nightMode = State.INSTANCE.getNightMode();

            if (nightMode == MODE_NIGHT_AUTO) {
                PreferencesFragmentPermissionsDispatcher.ensureDayNightWithCheck(this);
            }

            AppCompatDelegate.setDefaultNightMode(nightMode);
            recreateActivity();
        }
    }


    @Override
    public void onCreatePreferences(final Bundle bundle, final String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @TargetApi(11)
    public void recreateActivity() {
        if (Build.VERSION.SDK_INT >= 11) {
            getActivity().recreate();
        }
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    void ensureDayNight() {
        // Intentionally empty
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PreferencesFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
