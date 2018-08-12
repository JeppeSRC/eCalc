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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.theholyhorse.ecalc.R;

public class OpAmpInverting extends OpAmp {

    public OpAmpInverting() {
        super("OpAmp: Amp Inverting");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        init(inflater.inflate(R.layout.opamp_inverting_layout, container, false), R.drawable.op_amp_inverting);

        vccgnd = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vcc = (getDoubleFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix)) * 0.5;
                gnd = -vcc;

                lblVccSummary.setText("Dual (V+ = " + getDoubleString(vcc) + ", V- = " + getDoubleString(gnd) + ")");

                recalculateVout();
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

                recalculateGain();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        vin_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                vin = getDoubleFromView(edtVin) * getPrefixMultiplier(lblVinPrefix);
                recalculateVout();
            }

            public void afterTextChanged(Editable editable) {

            }
        };


        gain_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gain = -getDoubleFromView(edtGain);

                r1 = -rfb / gain;

                edtR1.removeTextChangedListener(r1rfb);
                edtR1.setText(getDoubleStringWithPrefix(r1, true));
                edtR1.addTextChangedListener(r1rfb);

                spR1.setSelection(getPrefixIndex(r1));

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

    private void recalculateGain() {
        gain = -rfb / r1;

        edtGain.removeTextChangedListener(gain_);
        edtGain.setText(getDoubleString(-gain));
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

        lblVout.setText("Vout: " + getDoubleStringWithPrefix(vout, false));
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vcc = (getDoubleFromView(edtVcc) * getPrefixMultiplier(lblVccPrefix)) * 0.5;
            gnd = -vcc;
            recalculateVout();
        } else if (adapterView.getAdapter() == spRfbAdapter) {
            lblRfbPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            rfb = getDoubleFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
            recalculateGain();
        } else if (adapterView.getAdapter() == spR1Adapter) {
            lblR1Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (edtGain.isFocused()) return;
            r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
            recalculateGain();
        } else if (adapterView.getAdapter() == spVinAdapter) {
            lblVinPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vin = getDoubleFromView(edtVin) * getPrefixMultiplier(lblVinPrefix);
            recalculateVout();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }

}
