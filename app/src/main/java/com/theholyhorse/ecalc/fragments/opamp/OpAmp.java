package com.theholyhorse.ecalc.fragments.opamp;

import android.support.v4.app.Fragment;
import android.text.TextWatcher;
import android.view.View;
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
import com.theholyhorse.ecalc.fragments.HorseBaseFragment;

import java.text.DecimalFormat;

public abstract class OpAmp extends HorseBaseFragment implements AdapterView.OnItemSelectedListener {

    protected View view;
    protected ImageView imageView;
    protected EditText edtVcc;
    protected EditText edtR1;
    protected EditText edtRfb;
    protected EditText edtVin;
    protected EditText edtGain;
    protected EditText edtTh;

    protected TextView lblVout;
    protected TextView lblVccPrefix;
    protected TextView lblRfbPrefix;
    protected TextView lblR1Prefix;
    protected TextView lblVinPrefix;
    protected TextView lblVccSummary;
    protected TextView lblThSummary;

    protected Spinner spVcc;
    protected Spinner spRfb;
    protected Spinner spR1;
    protected Spinner spVin;

    protected double vcc = 5.0f;
    protected double gnd = 0.0f;
    protected double r1 = 0.0f;
    protected double rfb = 0.0f;
    protected double vin = 0.0f;
    protected double vout = 0.0f;
    protected double gain = 0.0f;
    protected double threshold = 0.0f;

    protected TextWatcher vccgnd;
    protected TextWatcher r1rfb;
    protected TextWatcher vout_;
    protected TextWatcher vin_;
    protected TextWatcher gain_;
    protected TextWatcher th;

    protected ArrayAdapter<CharSequence> spVccAdapter;
    protected ArrayAdapter<CharSequence> spRfbAdapter;
    protected ArrayAdapter<CharSequence> spR1Adapter;
    protected ArrayAdapter<CharSequence> spVinAdapter;

    protected OpAmp(String title) {
        super(title);
    }

    protected String getDoubleString(double v) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(6);
        df.setMinimumIntegerDigits(1);
        return df.format(v);
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
        edtRfb = view.findViewById(R.id.edt_rfb);
        edtVin = view.findViewById(R.id.edt_vin);
        edtGain = view.findViewById(R.id.edt_gain);
        edtTh = view.findViewById(R.id.edt_th);

        lblVout = view.findViewById(R.id.lbl_vout);

        lblVccPrefix = view.findViewById(R.id.lbl_vcc_prefix);
        lblRfbPrefix = view.findViewById(R.id.lbl_rfb_prefix);
        lblR1Prefix = view.findViewById(R.id.lbl_r1_prefix);
        lblVinPrefix = view.findViewById(R.id.lbl_vin_prefix);
        lblVccSummary = view.findViewById(R.id.lbl_vcc_summary);
        lblThSummary = view.findViewById(R.id.lbl_th_summary);

        spVcc = view.findViewById(R.id.sp_vcc);
        spRfb = view.findViewById(R.id.sp_rfb);
        spR1 = view.findViewById(R.id.sp_r1);
        spVin = view.findViewById(R.id.sp_vin);

        spVccAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVccAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spRfbAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spRfbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spR1Adapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spR1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVinAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

        if (spVin != null) {
            spVin.setAdapter(spVinAdapter);
            spVin.setSelection(2);
            spVin.setOnItemSelectedListener(this);
        }

        if (MainActivity.getSharedPreferences().getBoolean("pref_ads", true) && MainActivity.getSharedPreferences().getBoolean("pref_ads_extra", false)) {
            AdView adView = view.findViewById(R.id.ad_view);
            AdRequest request = new AdRequest.Builder().build();

            adView.loadAd(request);
        }

    }
}
