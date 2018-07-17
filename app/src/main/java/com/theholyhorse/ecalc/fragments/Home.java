package com.theholyhorse.ecalc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

public class Home extends Fragment {

    private AdRequest adRequest;
    private AdView adView;
    private View homeView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        homeView = inflater.inflate(R.layout.home_layout, container, false);

        if (MainActivity.getSharedPreferences().getBoolean("pref_ads", false) == false) {
            adView = homeView.findViewById(R.id.ad_view_home);
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        return homeView;
    }

}
