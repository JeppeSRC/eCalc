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

public class OpAmpSchmittInverting extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private ImageView imageView;
    private EditText edtVcc;
    private EditText edtR1;
    private EditText edtRfb;
    private EditText edtTh;

    private TextView lblVout;
    private TextView lblVccPrefix;
    private TextView lblOhmPrefix;
    private TextView lblThPrefix;
    private TextView lblVccSummary;
    private TextView lblThSummary;


    private Spinner spVcc;
    private Spinner spOhm;
    private Spinner spTh;

    private float vcc = 0.0f;
    private float gnd = 0.0f;
    private float r1 = 0.0f;
    private float rfb = 0.0f;
    private float threshold = 0.0f;

    private TextWatcher vccgnd;
    private TextWatcher r1rfb;
    private TextWatcher th;

    private ArrayAdapter<CharSequence> spVccAdapter;
    private ArrayAdapter<CharSequence> spOhmAdapter;
    private ArrayAdapter<CharSequence> spThAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.opamp_noninverting_layout, container, false);

        imageView = view.findViewById(R.id.opamp_image);
        imageView.setImageResource(R.drawable.op_amp_noninverting);

        edtVcc = view.findViewById(R.id.edt_vcc);
        edtR1 = view.findViewById(R.id.edt_r1);
        edtRfb = view.findViewById(R.id.edt_rfb);
        edtTh = view.findViewById(R.id.edt_th);

        lblVout = view.findViewById(R.id.lbl_vout);

        lblVccPrefix = view.findViewById(R.id.lbl_vcc_prefix);
        lblOhmPrefix = view.findViewById(R.id.lbl_ohm_prefix);
        lblThPrefix = view.findViewById(R.id.lbl_th_prefix);
        lblVccSummary = view.findViewById(R.id.lbl_vcc_summary);
        lblThSummary = view.findViewById(R.id.lbl_th_summary);

        spVcc = view.findViewById(R.id.sp_vcc);
        spOhm = view.findViewById(R.id.sp_ohm);

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

        spThAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spThAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTh.setAdapter(spOhmAdapter);
        spTh.setSelection(1);
        spTh.setOnItemSelectedListener(this);

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
                r1 = getFloatFromView(edtR1) * getPrefixMultiplier(lblOhmPrefix);
                rfb = getFloatFromView(edtRfb) * getPrefixMultiplier(lblOhmPrefix);

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

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vcc = getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix);
            edtTh.setText(Float.toString(threshold / getPrefixMultiplier(lblThPrefix)));
        } else if (adapterView.getAdapter() == spOhmAdapter) {
            lblOhmPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            r1 = getFloatFromView(edtR1) * getPrefixMultiplier(lblOhmPrefix);
            rfb = getFloatFromView(edtRfb) * getPrefixMultiplier(lblOhmPrefix);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }

}
