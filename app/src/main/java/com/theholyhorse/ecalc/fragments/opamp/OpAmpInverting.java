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

public class OpAmpInverting extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private ImageView imageView;
    private EditText edtVcc;
    private EditText edtR1;
    private EditText edtRfb;
    private EditText edtVin;
    private EditText edtGain;

    private TextView lblVout;
    private TextView lblVccPrefix;
    private TextView lblRfbPrefix;
    private TextView lblR1Prefix;
    private TextView lblVinPrefix;
    private TextView lblVccSummary;

    private Spinner spVcc;
    private Spinner spRfb;
    private Spinner spR1;
    private Spinner spVin;


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
    private ArrayAdapter<CharSequence> spRfbAdapter;
    private ArrayAdapter<CharSequence> spR1Adapter;
    private ArrayAdapter<CharSequence> spVinAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.opamp_inverting_layout, container, false);

        imageView = view.findViewById(R.id.opamp_image);
        imageView.setImageResource(R.drawable.op_amp_inverting);

        edtVcc = view.findViewById(R.id.edt_vcc);
        edtR1 = view.findViewById(R.id.edt_r1);
        edtRfb = view.findViewById(R.id.edt_rfb);
        edtVin = view.findViewById(R.id.edt_vin);
        edtGain = view.findViewById(R.id.edt_gain);

        lblVout = view.findViewById(R.id.lbl_vout);

        lblVccPrefix = view.findViewById(R.id.lbl_vcc_prefix);
        lblRfbPrefix = view.findViewById(R.id.lbl_rfb_prefix);
        lblR1Prefix = view.findViewById(R.id.lbl_r1_prefix);
        lblVinPrefix = view.findViewById(R.id.lbl_vin_prefix);
        lblVccSummary = view.findViewById(R.id.lbl_vcc_summary);

        spVcc = view.findViewById(R.id.sp_vcc);
        spRfb = view.findViewById(R.id.sp_rfb);
        spR1 = view.findViewById(R.id.sp_r1);
        spVin = view.findViewById(R.id.sp_vin);

        spVccAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVccAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVcc.setAdapter(spVccAdapter);
        spVcc.setSelection(2);
        spVcc.setOnItemSelectedListener(this);

        spRfbAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spRfbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spRfb.setAdapter(spRfbAdapter);
        spRfb.setSelection(1);
        spRfb.setOnItemSelectedListener(this);

        spR1Adapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.ohms, android.R.layout.simple_spinner_item);
        spR1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spR1.setAdapter(spR1Adapter);
        spR1.setSelection(1);
        spR1.setOnItemSelectedListener(this);


        spVinAdapter = ArrayAdapter.createFromResource(MainActivity.get(), R.array.volts, android.R.layout.simple_spinner_item);
        spVinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spVin.setAdapter(spVinAdapter);
        spVin.setSelection(2);
        spVin.setOnItemSelectedListener(this);

        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = (getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix)) * 0.5f;
                gnd = -vcc;

                lblVccSummary.setText("Dual (V+ = " + Float.toString(vcc) + ", V- = " + Float.toString(gnd) + ")");

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

                r1 = -gain * rfb;

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

        edtVcc.setText("5");
        edtR1.setText("10");
        edtRfb.setText("10");
        edtVin.setText("1");


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

    private void recalculateGain() {
        gain = -rfb / r1;

        edtGain.removeTextChangedListener(gain_);
        edtGain.setText(Float.toString(gain));
        edtGain.addTextChangedListener(gain_);

        recalculateVout();
    }

    private void recalculateVout() {
        vout = vin * gain;

        if (vout > vcc) {
            vout = vcc;
        } else if (vout < gnd) {
            vout = gnd;
        }

        float sign = 1.0f;

        if (vout < 0.0f) {
            sign = -1.0f;
            vout *= sign;
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

        lblVout.setText("Vout: " + Float.toString((vout * sign) / tmp) + sTmp);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vcc = (getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix)) * 0.5f;
            gnd = -vcc;
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
