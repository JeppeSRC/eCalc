package com.theholyhorse.ecalc.fragments.opamp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

public class OpAmpSchmittInverting extends OpAmp {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = (getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix)) * 0.5f;
                gnd = -vcc;

                lblVccSummary.setText("Dual (V+ = " + Float.toString(vcc) + ", V- = 0)");

                edtTh.setText(Float.toString(threshold / getPrefixMultiplier(lblThPrefix)));
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        r1rfb = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                r1 = getFloatFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
                rfb = getFloatFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);

                threshold = r1 / (r1 + rfb);

                edtTh.removeTextChangedListener(th);
                edtTh.setText(Float.toString(threshold));
                edtTh.addTextChangedListener(th);

                float vh = threshold * vcc;
                float vl = threshold * gnd;

                lblThSummary.setText("VHth: " + Float.toString(vh) + ", VLth: " + Float.toString(vl));
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        th = new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                threshold = getFloatFromView(edtTh) * getPrefixMultiplier(lblThPrefix);

                if (threshold > 1.0f || threshold < 0.0f) {
                    Toast.makeText(MainActivity.get(), "Th must be between 0 - 1", Toast.LENGTH_LONG).show();
                    threshold = 0.0f;
                }

                r1 = (threshold * rfb) / (1 - threshold);

                edtR1.setText(Float.toString(r1));
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        edtVcc.addTextChangedListener(vccgnd);
        edtR1.addTextChangedListener(r1rfb);
        edtRfb.addTextChangedListener(r1rfb);
        edtTh.addTextChangedListener(th);


        if (MainActivity.getSharedPreferences().getBoolean("pref_ads", true) && MainActivity.getSharedPreferences().getBoolean("pref_ads_extra", false)) {
            AdView adView = view.findViewById(R.id.ad_view_noninverting);
            AdRequest request = new AdRequest.Builder().build();

            adView.loadAd(request);
        }

        edtVcc.setText("5");
        edtR1.setText("10");
        edtRfb.setText("10");

        return view;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vcc = getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix);
            edtTh.setText(Float.toString(threshold / getPrefixMultiplier(lblThPrefix)));
        } else if (adapterView.getAdapter() == spRfbAdapter) {
            lblRfbPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            rfb = getFloatFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
        } else if (adapterView.getAdapter() == spR1Adapter) {
            lblR1Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            rfb = getFloatFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }

}
