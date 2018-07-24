package com.theholyhorse.ecalc.fragments.opamp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

public class OpAmpSchmittInvertingSingle extends OpAmp {

    public OpAmpSchmittInvertingSingle() {
        super("OpAmp: Schmitt Inverting Single");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        init(inflater.inflate(R.layout.opamp_schmitt_inverting_single_layout, container, false), R.drawable.op_amp_schmitt_inverting_single);


        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = getDoubleFromView(edtVcc);
                gnd = 0;

                lblVccSummary.setText("V+ = " + getDoubleString(vcc) + ", V- = 0");

                vcc *= getPrefixMultiplier(lblVccPrefix);

                recalculateTh();
            }

            public void afterTextChanged(Editable editable) { }
        };

        r1rfb = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
                rfb = getDoubleFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
                r2 = getDoubleFromView(edtR2) * getPrefixMultiplier(lblR2Prefix);

                recalculateTh();
            }

            public void afterTextChanged(Editable editable) { }
        };

      /*  r2_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                r2 = getDoubleFromView(edtR2) * getPrefixMultiplier(lblR2Prefix);

                recalculateTh();
            }

            public void afterTextChanged(Editable editable) { }
        };*/

        vswitch_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) { }
        };

        hyst_ = new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                threshold = getDoubleFromView(edtHyst);


                if (threshold > 1.0 || threshold < 0.0) {
                    Toast.makeText(MainActivity.get(), "Th must be between 0 -> 1", Toast.LENGTH_LONG).show();
                    threshold = 0.0;
                }

                double vswitch = threshold * vcc;



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

                AdapterView.OnItemSelectedListener listener = spR1.getOnItemSelectedListener();
                spR1.setOnItemSelectedListener(null);
                spR1.setSelection(sel);
                spR1.setOnItemSelectedListener(listener);


            }

            public void afterTextChanged(Editable editable) { }
        };

        edtVcc.addTextChangedListener(vccgnd);
        edtR1.addTextChangedListener(r1rfb);
        edtR2.addTextChangedListener(r1rfb);
        edtRfb.addTextChangedListener(r1rfb);
        edtHyst.addTextChangedListener(hyst_);
        edtVswitch.addTextChangedListener(vswitch_);

        edtVcc.setText("5");
        edtR1.setText("10");
        edtRfb.setText("10");

        return view;
    }

    private void recalculateTh() {
        double r2rfbParallel = resistorParallel(r2, rfb);

        vth = vcc * r2 / (r2 + resistorParallel(r1, rfb));
        vtl = vcc * r2rfbParallel / (r1 + r2rfbParallel);

        hyst = vth - vtl;

        vswitch = ((vth + vtl) * 0.5);
        threshold = vswitch / vcc;

        edtVswitch.removeTextChangedListener(vswitch_);
        edtVswitch.setText(getDoubleStringWithPrefix(vswitch, true));
        edtVswitch.addTextChangedListener(vswitch_);

        spVswitch.setSelection(getPrefixIndex(vswitch));

        edtHyst.removeTextChangedListener(hyst_);
        edtHyst.setText(getDoubleStringWithPrefix(hyst, true));
        edtHyst.addTextChangedListener(hyst_);

        spHyst.setSelection(getPrefixIndex(hyst));

        lblThSummary.setText("VHth: " + getDoubleStringWithPrefix(vth, false) +  ", VLth: " + getDoubleStringWithPrefix(vtl, false));
    }

    private

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vccgnd.onTextChanged(null, 0, 0, 0);
        } else if (adapterView.getAdapter() == spRfbAdapter) {
            lblRfbPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            rfb = getDoubleFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
        } else if (adapterView.getAdapter() == spR1Adapter) {
            lblR1Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));

            if (edtHyst.isFocused()) return;
            r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
            recalculateTh();
        } else if (adapterView.getAdapter() == spR2Adapter) {
            lblR2Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (edtHyst.isFocused()) return;
            r2 = getDoubleFromView(edtR2) * getPrefixMultiplier(lblR2Prefix);
            recalculateTh();
        }else if (adapterView.getAdapter() == spHystAdapter) {
            lblHystPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            hyst = getDoubleFromView(edtHyst) * getPrefixMultiplier(lblHystPrefix);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }


}
