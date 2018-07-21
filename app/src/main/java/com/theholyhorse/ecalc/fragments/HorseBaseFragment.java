package com.theholyhorse.ecalc.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public abstract class HorseBaseFragment extends PreferenceFragmentCompat {

    protected String title;

    protected HorseBaseFragment(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }

    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }
}
