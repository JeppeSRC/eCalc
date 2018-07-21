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

    private boolean noThRecalc = false;

    public OpAmpSchmittInverting() {
        super("OpAmp: Schmitt Inverting");
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        init(inflater.inflate(R.layout.opamp_schmitt_inverting_layout, container, false), R.drawable.op_amp_schmitt_inverting);


        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = getDoubleFromView(edtVcc)* 0.5;
                gnd = -vcc;

                lblVccSummary.setText("V+ = " + getDoubleString(vcc) + ", V- = " + getDoubleString(gnd));

                vcc *= getPrefixMultiplier(lblVccPrefix);
                gnd *= getPrefixMultiplier(lblVccPrefix);

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

                recalculateTh();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        th = new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                threshold = getDoubleFromView(edtTh);


                if (threshold >= 1.0 || threshold <= 0.0) {
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
                    sel = 6;
                } else if (r1 < 0.000001) {
                    tmp = 0.000000001;
                    sel = 5;
                } else if (r1 < 0.001) {
                    tmp = 0.000001;
                    sel = 4;
                } else if (r1 < 1) {
                    tmp = 0.001;
                    sel = 3;
                }

                edtR1.removeTextChangedListener(r1rfb);
                edtR1.setText(getDoubleString(r1 / tmp));
                edtR1.addTextChangedListener(r1rfb);

                spR1.setSelection(sel);

                recalculateThSummary();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        edtVcc.addTextChangedListener(vccgnd);
        edtR1.addTextChangedListener(r1rfb);
        edtRfb.addTextChangedListener(r1rfb);
        edtTh.addTextChangedListener(th);

        edtTh.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean b) {
                noThRecalc = b;
            }
        });

        edtVcc.setText("5");
        edtR1.setText("10");
        edtRfb.setText("10");

        return view;
    }

    private void recalculateTh() {
        threshold = r1 / (r1 + rfb);

        edtTh.removeTextChangedListener(th);
        edtTh.setText(getDoubleString(threshold));
        edtTh.addTextChangedListener(th);

        recalculateThSummary();
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
            rfb = getDoubleFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
            recalculateTh();
        } else if (adapterView.getAdapter() == spR1Adapter) {
            lblR1Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (noThRecalc) return;
            r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
            recalculateTh();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }

}
