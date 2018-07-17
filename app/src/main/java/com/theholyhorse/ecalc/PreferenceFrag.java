/*MIT License

Copyright (c) 2018 TheHolyHorse

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
        Preference pref = findPreference(key);
        if (key.equals("pref_ads")) {
            if (sharedPreferences.getBoolean(key, false)) {
                pref.setSummary("Ads will be shown!");
                pref = findPreference("pref_ads_extra");

                pref.setEnabled(true);
            } else {
                pref.setSummary("All Ads will be hidden");
                pref = findPreference("pref_ads_extra");

                pref.setEnabled(false);
            }
        } else if (key.equals("pref_ads_extra")) {
            if (sharedPreferences.getBoolean(key, false)) {
                pref.setSummary("Ads will be shown in the calculator!");
            } else {
                pref.setSummary("Ads will only be shown at the home menu");
            }

        }
    }
}
