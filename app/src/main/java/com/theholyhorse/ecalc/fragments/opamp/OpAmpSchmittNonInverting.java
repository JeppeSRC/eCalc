package com.theholyhorse.ecalc.fragments.opamp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

public class OpAmpSchmittNonInverting extends OpAmp {

    public OpAmpSchmittNonInverting() {
        super("OpAmp: Schmitt NonInverting");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        init(inflater.inflate(R.layout.opamp_schmitt_noninverting_layout, container, false), R.drawable.op_amp_schmitt_noninverting);


        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = getFloatFromView(edtVcc) * 0.5f;
                gnd = -vcc;

                lblVccSummary.setText("Dual (V+ = " + Float.toString(vcc) + ", V- = " + Float.toString(gnd) + ")");
                recalculateThSummary();

                vcc *= getPrefixMultiplier(lblVccPrefix);
                gnd *= getPrefixMultiplier(lblVccPrefix);
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

                threshold = -r1 / (r1 + rfb);

                edtTh.removeTextChangedListener(th);
                edtTh.setText(Float.toString(threshold));
                edtTh.addTextChangedListener(th);

                recalculateThSummary();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        th = new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                threshold = getFloatFromView(edtTh);

                if (threshold < -1.0f || threshold > 0.0f) {
                    Toast.makeText(MainActivity.get(), "Th must be between -1 -> 0", Toast.LENGTH_LONG).show();
                    threshold = 0.0f;
                }

                r1 = (-threshold * rfb) / (1 - -threshold);

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

                recalculateThSummary();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        edtVcc.addTextChangedListener(vccgnd);
        edtR1.addTextChangedListener(r1rfb);
        edtRfb.addTextChangedListener(r1rfb);
        edtTh.addTextChangedListener(th);

        edtVcc.setText("5");
        edtR1.setText("10");
        edtRfb.setText("10");

        return view;
    }

    private void recalculateThSummary() {
        float vh = threshold * gnd;
        float vl = threshold * vcc;

        float tmp = 1;
        String sTmp = "V";

        if (vh > 1000000){
            tmp = 1000000;
            sTmp = "MV";
        } else if (vh > 1000) {
            tmp = 1000;
            sTmp = "KV";
        } else if (vh < 0.000000001) {
            tmp = 0.000000000001f;
            sTmp = "pV";
        } else if (vh < 0.000001) {
            tmp = 0.000000001f;
            sTmp = "nV";
        } else if (vh < 0.001) {
            tmp = 0.000001f;
            sTmp = "µV";
        } else if (vh < 1) {
            tmp = 0.001f;
            sTmp = "mV";
        }

        lblThSummary.setText("VHth: " + Float.toString(vh / tmp) + sTmp +  ", VLth: " + Float.toString(vl / tmp) + sTmp);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vcc = (getFloatFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix)) * 0.5f;
            gnd = -vcc;
            recalculateThSummary();
        } else if (adapterView.getAdapter() == spRfbAdapter) {
            lblRfbPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            r1rfb.onTextChanged(null, 0, 0, 0);
        } else if (adapterView.getAdapter() == spR1Adapter) {
            lblR1Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            r1rfb.onTextChanged(null, 0, 0, 0);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }


}
