package com.theholyhorse.ecalc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class PreferenceFrag extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    public void onCreatePreferences(Bundle savedInstance, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_ads")) {
            Preference pref = findPreference(key);

            if (sharedPreferences.getBoolean(key, false)) {
                pref.setSummary("Ads will be hidden");
            } else {
                pref.setSummary("Ads will be shown!");
            }
        }
    }
}
