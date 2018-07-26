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

import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;
import com.theholyhorse.ecalc.fragments.HorseBaseFragment;

import java.text.DecimalFormat;

public abstract class OpAmp extends HorseBaseFragment implements AdapterView.OnItemSelectedListener {

    protected View view;
    protected ImageView imageView;
    protected EditText edtVcc;
    protected EditText edtR1;
    protected EditText edtR2;
    protected EditText edtRfb;
    protected EditText edtVin;
    protected EditText edtGain;
    protected EditText edtHyst;
    protected EditText edtVswitch;

    protected TextView lblVout;
    protected TextView lblVccPrefix;
    protected TextView lblRfbPrefix;
    protected TextView lblR1Prefix;
    protected TextView lblR2Prefix;
    protected TextView lblVinPrefix;
    protected TextView lblHystPrefix;
    protected TextView lblVswitchPrefix;
    protected TextView lblVccSummary;
    protected TextView lblThSummary;

    protected Spinner spVcc;
    protected Spinner spRfb;
    protected Spinner spR1;
    protected Spinner spR2;
    protected Spinner spVin;
    protected Spinner spHyst;
    protected Spinner spVswitch;

    protected double vcc = 0.0;
    protected double gnd = 0.0;
    protected double r1 = 0.0;
    protected double r2 = 0.0;
    protected double rfb = 0.0;
    protected double vin = 0.0;
    protected double vout = 0.0;
    protected double gain = 0.0;
    protected double threshold = 0.0;
    protected double hyst = 0.0;
    protected double vth = 0.0;
    protected double vtl = 0.0;
    protected double vswitch = 0.0;

    protected TextWatcher vccgnd;
    protected TextWatcher r1rfb;
    protected TextWatcher r2_;
    protected TextWatcher vout_;
    protected TextWatcher vin_;
    protected TextWatcher gain_;
    protected TextWatcher hyst_;
    protected TextWatcher vswitch_;

    protected ArrayAdapter<CharSequence> spVccAdapter;
    protected ArrayAdapter<CharSequence> spRfbAdapter;
    protected ArrayAdapter<CharSequence> spR1Adapter;
    protected ArrayAdapter<CharSequence> spR2Adapter;
    protected ArrayAdapter<CharSequence> spVinAdapter;
    protected ArrayAdapter<CharSequence> spHystAdapter;
    protected ArrayAdapter<CharSequence> spVswitchAdapter;

    protected OpAmp(String title) {
        super(title);
    }

    protected double resistorParallel(double r1, double r2) {
        return 1.0 / (1.0 / r1 + 1.0 / r2);
    }

    protected String getDoubleString(double v) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        df.setMinimumIntegerDigits(1);
        return df.format(v);
    }

    protected int getPrefixIndex(double v) {
        if (v > 1000000) {
            return 0;
        } else if (v > 1000) {
            return 1;
        } else if (v < 0.000000001) {
            return 6;
        } else if (v < 0.000001) {
            return 5;
        } else if (v < 0.001) {
            return 4;
        } else if (v < 1) {
            return 3;
        }

        return 2;
    }

    protected String getDoubleStringWithPrefix(double v, boolean noPrefix) {
        double tmp = 1;
        String sTmp = "V";

        switch (getPrefixIndex(v)) {
            case 0:
                tmp = 1000000;
                sTmp = "MV";
                break;
            case 1:
                tmp = 1000;
                sTmp = "KV";
                break;
            case 6:
                tmp = 0.000000000001;
                sTmp = "pV";
                break;
            case 5:
                tmp = 0.000000001;
                sTmp = "nV";
                break;
            case 4:
                tmp = 0.000001;
                sTmp = "µV";
                break;
            case 3:
                tmp = 0.001;
                sTmp = "mV";
                break;
        }

        if (noPrefix) {
            return getDoubleString(v / tmp);
        }


        return getDoubleString(v / tmp) + sTmp;
    }

    protected double getDoubleFromView(EditText view) {
        String s = view.getText().toString();

        if (s.isEmpty()) return 0.0;

        double res = 0.0;

        try {
            res = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            res = -0.123;
        }


        return res;
    }

    protected double getPrefixMultiplier(TextView prefixView) {
        return getPrefixMultiplier(prefixView.getText().toString());
    }

    protected double getPrefixMultiplier(String string) {
        if (string.equals("MV") || string.equals("M\u2126")) {
            return 1000000.0;
        } else if (string.equals("KV") || string.equals("K\u2126")) {
            return 1000.0;
        } else if (string.equals("V") || string.equals("\u2126")) {
            return 1.0;
        } else if (string.equals("mV") || string.equals("m\u2126")) {
            return 0.001;
        } else if (string.equals("µV") || string.equals("µ\u2126")) {
            return 0.000001;
        } else if (string.equals("nV") || string.equals("n\u2126")) {
            return 0.000000001;
        } else if (string.equals("pV") || string.equals("p\u2126")) {
            return 0.000000000001;
        }

        return 0.0;
    }

    protected void init(View view, int imageId) {
        this.view = view;
        imageView = view.findViewById(R.id.opamp_image);
        imageView.setImageResource(imageId);

        edtVcc = view.findViewById(R.id.edt_vcc);
        edtR1 = view.findViewById(R.id.edt_r1);
        edtR2 = view.findViewById(R.id.edt_r2);
        edtRfb = view.findViewById(R.id.edt_rfb);
        edtVin = view.findViewById(R.id.edt_vin);
        edtGain = view.findViewById(R.id.edt_gain);
        edtHyst = view.findViewById(R.id.edt_hyst);
        edtVswitch = view.findViewById(R.id.edt_vswitch);

        lblVout = view.findViewById(R.id.lbl_vout);

        lblVccPrefix = view.findViewById(R.id.lbl_vcc_prefix);
        lblRfbPrefix = view.findViewById(R.id.lbl_rfb_prefix);
        lblR1Prefix = view.findViewById(R.id.lbl_r1_prefix);
        lblR2Prefix = view.findViewById(R.id.lbl_r2_prefix);
        lblHystPrefix = view.findViewById(R.id.lbl_hyst_prefix);
        lblVswitchPrefix = view.findViewById(R.id.lbl_vswitch_prefix);
        lblVinPrefix = view.findViewById(R.id.lbl_vin_prefix);
        lblVccSummary = view.findViewById(R.id.lbl_vcc_summary);
        lblThSummary = view.findViewById(R.id.lbl_th_summary);

        spVcc = view.findViewById(R.id.sp_vcc);
        spRfb = view.findViewById(R.id.sp_rfb);
        spR1 = view.findViewById(R.id.sp_r1);
        spR2 = view.findViewById(R.id.sp_r2);
        spHyst = view.findViewById(R.id.sp_hyst);
        spVin = view.findViewById(R.id.sp_vin);
        spVswitch = view.findViewById(R.id.sp_vswitch);

        spVccAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVccAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spRfbAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spRfbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spR1Adapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spR1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spR2Adapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spR2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVinAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spHystAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spHystAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVswitchAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVswitchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (spVcc != null) {
            spVcc.setAdapter(spVccAdapter);
            spVcc.setSelection(2);
            spVcc.setOnItemSelectedListener(this);
        }

        if (spRfb != null) {
            spRfb.setAdapter(spRfbAdapter);
            spRfb.setSelection(1);
            spRfb.setOnItemSelectedListener(this);
        }

        if (spR1 != null) {
            spR1.setAdapter(spR1Adapter);
            spR1.setSelection(1);
            spR1.setOnItemSelectedListener(this);
        }

        if (spR2 != null) {
            spR2.setAdapter(spR2Adapter);
            spR2.setSelection(1);
            spR2.setOnItemSelectedListener(this);
        }

        if (spVin != null) {
            spVin.setAdapter(spVinAdapter);
            spVin.setSelection(2);
            spVin.setOnItemSelectedListener(this);
        }

        if (spHyst != null) {
            spHyst.setAdapter(spHystAdapter);
            spHyst.setSelection(2);
            spHyst.setOnItemSelectedListener(this);
        }

        if (spVswitch != null) {
            spVswitch.setAdapter(spHystAdapter);
            spVswitch.setSelection(2);
            spVswitch.setOnItemSelectedListener(this);
        }

        if (MainActivity.getSharedPreferences().getBoolean("pref_ads", true) && MainActivity.getSharedPreferences().getBoolean("pref_ads_extra", false)) {
            AdView adView = view.findViewById(R.id.ad_view);
            AdRequest request = new AdRequest.Builder().build();

            adView.loadAd(request);
        }
    }
}
