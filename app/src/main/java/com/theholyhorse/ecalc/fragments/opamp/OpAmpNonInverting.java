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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

public class OpAmpNonInverting extends OpAmp {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        init(inflater.inflate(R.layout.opamp_noninverting_layout, container, false));


        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix);
                gnd = 0.0f;//getFloatFromView(edtGnd);

                lblVccSummary.setText("Single (V+ = " + Float.toString(vcc) + ", V- = 0)");

                recalculateVout();
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

                recalculateGain();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        vin_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vin = getFloatFromView(edtVin) * getPrefixMultiplier(lblVinPrefix);
                recalculateVout();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        gain_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gain = getFloatFromView(edtGain);

                r1 = rfb / (gain - 1);

                float tmp = 1;

                int sel = 2;

                if (r1 > 1000000){
                    tmp = 1000000;
                    sel = 0;
                } else if (r1 > 1000) {
                    tmp = 1000;
                    sel = 1;
                } else if (r1 < 0.000000001) {
                    tmp = 0.000000000001f;
                    sel = 3;
                } else if (r1 < 0.000001) {
                    tmp = 0.000000001f;
                    sel = 4;
                } else if (r1 < 0.001) {
                    tmp = 0.000001f;
                    sel = 5;
                } else if (r1 < 1) {
                    tmp = 0.001f;
                    sel = 6;
                }

                edtR1.removeTextChangedListener(r1rfb);
                edtR1.setText(Float.toString(r1 / tmp));
                edtR1.addTextChangedListener(r1rfb);

                AdapterView.OnItemSelectedListener listener = spR1.getOnItemSelectedListener();
                spR1.setOnItemSelectedListener(null);
                spR1.setSelection(sel);
                spR1.setOnItemSelectedListener(listener);


                recalculateVout();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        edtVcc.addTextChangedListener(vccgnd);
        edtR1.addTextChangedListener(r1rfb);
        edtRfb.addTextChangedListener(r1rfb);
        edtVin.addTextChangedListener(vin_);
        edtGain.addTextChangedListener(gain_);


        if (MainActivity.getSharedPreferences().getBoolean("pref_ads", true) && MainActivity.getSharedPreferences().getBoolean("pref_ads_extra", false)) {
            AdView adView = view.findViewById(R.id.ad_view_noninverting);
            AdRequest request = new AdRequest.Builder().build();

            adView.loadAd(request);
        }

        edtVcc.setText("5");
        edtR1.setText("10");
        edtRfb.setText("10");
        edtVin.setText("1");

        return view;
    }


    protected void recalculateGain() {
        gain = rfb / r1 + 1;

        edtGain.removeTextChangedListener(gain_);
        edtGain.setText(Float.toString(gain));
        edtGain.addTextChangedListener(gain_);

        recalculateVout();
    }

    protected void recalculateVout() {
        vout = vin * gain;

        if (vout > vcc) {
            vout = vcc;
        }

        float tmp = 1.0f;
        String sTmp = "V";

        if (vout > 1000000){
            tmp = 1000000;
            sTmp = "MV";
        } else if (vout > 1000) {
            tmp = 1000;
            sTmp = "KV";
        } else if (vout < 0.000000001) {
            tmp = 0.000000000001f;
            sTmp = "pV";
        } else if (vout < 0.000001) {
            tmp = 0.000000001f;
            sTmp = "nV";
        } else if (vout < 0.001) {
            tmp = 0.000001f;
            sTmp = "µV";
        } else if (vout < 1) {
            tmp = 0.001f;
            sTmp = "mV";
        }

        lblVout.setText("Vout: " + Float.toString(vout / tmp) + sTmp);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       if (adapterView.getAdapter() == spVccAdapter) {
           lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
           vcc = getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix);
           recalculateVout();
       } else if (adapterView.getAdapter() == spRfbAdapter) {
           lblRfbPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
           rfb = getFloatFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
           recalculateGain();
       } else if (adapterView.getAdapter() == spR1Adapter) {
           lblR1Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
           r1 = getFloatFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
           recalculateGain();
       } else if (adapterView.getAdapter() == spVinAdapter) {
           lblVinPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
           vin = getFloatFromView(edtVin) * getPrefixMultiplier(lblVinPrefix);
           recalculateVout();
       }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }
}
