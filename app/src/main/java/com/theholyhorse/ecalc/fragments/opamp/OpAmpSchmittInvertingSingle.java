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

public class OpAmpSchmittInvertingSingle extends OpAmp {

    public OpAmpSchmittInvertingSingle() {
        super("OpAmp: Schmitt Inverting Single");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        init(inflater.inflate(R.layout.opamp_schmitt_inverting_single_layout, container, false), R.drawable.op_amp_schmitt_inverting);


        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = getDoubleFromView(edtVcc);
                gnd = 0;

                lblVccSummary.setText("V+ = " + getDoubleString(vcc) + ", V- = 0");

                vcc *= getPrefixMultiplier(lblVccPrefix);

                recalculateThSummary();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        r1rfb = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
                rfb = getDoubleFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);

                threshold = r1 / (r1 + rfb);

                edtTh.removeTextChangedListener(th);
                edtTh.setText(getDoubleString(threshold));
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
                threshold = getDoubleFromView(edtTh);


                if (threshold > 1.0 || threshold < 0.0) {
                    Toast.makeText(MainActivity.get(), "Th must be between 0 -> 1", Toast.LENGTH_LONG).show();
                    threshold = 0.0;
                }

                r1 = (threshold * rfb) / (1 - threshold);

                double tmp = 1;

                int sel = 2;

                if (r1 > 1000000){
                    tmp = 1000000;
                    sel = 0;
                } else if (r1 > 1000) {
                    tmp = 1000;
                    sel = 1;
                } else if (r1 < 0.000000001) {
                    tmp = 0.000000000001;
                    sel = 3;
                } else if (r1 < 0.000001) {
                    tmp = 0.000000001;
                    sel = 4;
                } else if (r1 < 0.001) {
                    tmp = 0.000001;
                    sel = 5;
                } else if (r1 < 1) {
                    tmp = 0.001;
                    sel = 6;
                }

                edtR1.removeTextChangedListener(r1rfb);
                edtR1.setText(getDoubleString(r1 / tmp));
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
        double vh = threshold * vcc;
        double vl = threshold * gnd;

        double tmp = 1;
        String sTmp = "V";

        if (vh > 1000000) {
            tmp = 1000000;
            sTmp = "MV";
        } else if (vh > 1000) {
            tmp = 1000;
            sTmp = "KV";
        } else if (vh < 0.000000001) {
            tmp = 0.000000000001;
            sTmp = "pV";
        } else if (vh < 0.000001) {
            tmp = 0.000000001;
            sTmp = "nV";
        } else if (vh < 0.001) {
            tmp = 0.000001;
            sTmp = "µV";
        } else if (vh < 1) {
            tmp = 0.001;
            sTmp = "mV";
        }

        lblThSummary.setText("VHth: " + getDoubleString(vh / tmp) + sTmp +  ", VLth: " + getDoubleString(vl / tmp) + sTmp + ", Vhst: " + getDoubleString((vh - vl) / tmp) + sTmp);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vccgnd.onTextChanged(null, 0, 0, 0);
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
