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

public class OpAmpInverting extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private ImageView imageView;
    private EditText edtVcc;
    private EditText edtGnd;
    private EditText edtR1;
    private EditText edtRfb;
    private EditText edtVin;
    private EditText edtGain;

    private TextView lblVout;
    private TextView lblVccPrefix;
    private TextView lblOhmPrefix;
    private TextView lblVinPrefix;
    private TextView lblVoutPrefix;

    private Spinner spVcc;
    private Spinner spOhm;
    private Spinner spVin;
    private Spinner spVout;


    private float vcc = 5.0f;
    private float gnd = -5.0f;
    private float r1 = 0.0f;
    private float rfb = 0.0f;
    private float vin = 0.0f;
    private float vout = 0.0f;
    private float gain = 0.0f;

    private TextWatcher vccgnd;
    private TextWatcher r1rfb;
    private TextWatcher vout_;
    private TextWatcher vin_;
    private TextWatcher gain_;

    private ArrayAdapter<CharSequence> spVccAdapter;
    private ArrayAdapter<CharSequence> spOhmAdapter;
    private ArrayAdapter<CharSequence> spVinAdapter;
    private ArrayAdapter<CharSequence> spVoutAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.opamp_inverting_layout, container, false);

        imageView = view.findViewById(R.id.opamp_image);
        imageView.setImageResource(R.drawable.op_amp_inverting);

        edtVcc = view.findViewById(R.id.edt_vcc);
        edtGnd = view.findViewById(R.id.edt_gnd);
        edtR1 = view.findViewById(R.id.edt_r1);
        edtRfb = view.findViewById(R.id.edt_rfb);
        edtVin = view.findViewById(R.id.edt_vin);
        edtGain = view.findViewById(R.id.edt_gain);

        lblVout = view.findViewById(R.id.lbl_vout);

        lblVccPrefix = view.findViewById(R.id.lbl_vcc_prefix);
        lblOhmPrefix = view.findViewById(R.id.lbl_ohm_prefix);
        lblVinPrefix = view.findViewById(R.id.lbl_vin_prefix);
        lblVoutPrefix = view.findViewById(R.id.lbl_vout_prefix);

        spVcc = view.findViewById(R.id.sp_vcc);
        spOhm = view.findViewById(R.id.sp_ohm);
        spVin = view.findViewById(R.id.sp_vin);
        spVout = view.findViewById(R.id.sp_vout);

        spVccAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVccAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVcc.setAdapter(spVccAdapter);
        spVcc.setSelection(2);
        spVcc.setOnItemSelectedListener(this);

        spOhmAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spOhmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spOhm.setAdapter(spOhmAdapter);
        spOhm.setSelection(1);
        spOhm.setOnItemSelectedListener(this);


        spVinAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVin.setAdapter(spVinAdapter);
        spVin.setSelection(2);
        spVin.setOnItemSelectedListener(this);

        spVoutAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVout.setAdapter(spVoutAdapter);
        spVout.setSelection(2);
        spVout.setOnItemSelectedListener(this);

        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix);
                gnd = getFloatFromView(edtGnd) * getPrefixMultiplier(lblVccPrefix);
                recalculateStuff();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        r1rfb = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                r1 = getFloatFromView(edtR1) * getPrefixMultiplier(lblOhmPrefix);
                rfb = getFloatFromView(edtRfb) * getPrefixMultiplier(lblOhmPrefix);



                gain = -r1 / rfb;

                edtGain.removeTextChangedListener(gain_);
                edtGain.setText(Float.toString(gain));
                edtGain.addTextChangedListener(gain_);

                recalculateStuff();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        vin_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vin = getFloatFromView(edtVin) * getPrefixMultiplier(lblVinPrefix);
                recalculateStuff();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        gain_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gain = getFloatFromView(edtGain);

                r1 = -gain * rfb;

                edtR1.removeTextChangedListener(r1rfb);
                edtR1.setText(Float.toString(r1));
                edtR1.addTextChangedListener(r1rfb);

                recalculateStuff();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        edtVcc.addTextChangedListener(vccgnd);
        edtGnd.addTextChangedListener(vccgnd);
        edtR1.addTextChangedListener(r1rfb);
        edtRfb.addTextChangedListener(r1rfb);
        edtVin.addTextChangedListener(vin_);
        edtGain.addTextChangedListener(gain_);

        recalculateStuff();

        if (MainActivity.getSharedPreferences().getBoolean("pref_ads", true) && MainActivity.getSharedPreferences().getBoolean("pref_ads_extra", false)) {
            AdView adView = view.findViewById(R.id.ad_view_inverting);
            AdRequest request = new AdRequest.Builder().build();

            adView.loadAd(request);
        }

        return view;
    }

    private float getFloatFromView(EditText view) {
        String s = view.getText().toString();

        if (s.isEmpty()) return 0.0f;

        float res = 0.0f;

        try {
            res = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            res = -0.123f;
        }


        return res;
    }

    private float getPrefixMultiplier(TextView prefixView) {
        return getPrefixMultiplier(prefixView.getText().toString());
    }

    private float getPrefixMultiplier(String string) {
        if (string.equals("MV") || string.equals("M\u2126")) {
            return 1000000.0f;
        } else if (string.equals("KV") || string.equals("K\u2126")) {
            return 1000.0f;
        } else if (string.equals("V") || string.equals("\u2126")) {
            return 1.0f;
        } else if (string.equals("mV") || string.equals("m\u2126")) {
            return 0.001f;
        } else if (string.equals("µV") || string.equals("µ\u2126")) {
            return 0.000001f;
        } else if (string.equals("nV") || string.equals("n\u2126")) {
            return 0.000000001f;
        } else if (string.equals("pV") || string.equals("p\u2126")) {
            return 0.000000000001f;
        }

        return 0.0f;
    }

    private void recalculateStuff() {
        vout = vin * gain;

        if (vout > vcc) {
            vout = vcc;
        } else if (vout < gnd) {
            vout = gnd;
        }

        lblVout.setText("Vout: " + Float.toString(vout / getPrefixMultiplier(lblVoutPrefix)));
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vcc = getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix);
            gnd = getFloatFromView(edtGnd) * getPrefixMultiplier(lblVccPrefix);
            recalculateStuff();
        } else if (adapterView.getAdapter() == spOhmAdapter) {
            lblOhmPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            r1 = getFloatFromView(edtR1) * getPrefixMultiplier(lblOhmPrefix);
            rfb = getFloatFromView(edtRfb) * getPrefixMultiplier(lblOhmPrefix);
        } else if (adapterView.getAdapter() == spVinAdapter) {
            lblVinPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vin = getFloatFromView(edtVcc) * getPrefixMultiplier(lblVinPrefix);
            recalculateStuff();
        } else if (adapterView.getAdapter() == spVoutAdapter) {
            lblVoutPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            recalculateStuff();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }
}
