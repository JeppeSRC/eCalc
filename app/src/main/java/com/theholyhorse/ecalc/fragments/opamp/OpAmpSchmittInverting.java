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
import android.widget.Toast;

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

                recalculateTh();
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

        hyst_ = new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hyst = getDoubleFromView(edtHyst) * getPrefixMultiplier(lblHystPrefix);

                calculateR1();
            }

            public void afterTextChanged(Editable editable) {

            }
        };

        edtVcc.addTextChangedListener(vccgnd);
        edtR1.addTextChangedListener(r1rfb);
        edtRfb.addTextChangedListener(r1rfb);
        edtHyst.addTextChangedListener(hyst_);

        edtVcc.setText("5");
        edtR1.setText("10");
        edtRfb.setText("10");

        return view;
    }

    protected void calculateR1() {
        vth = hyst * 0.5f;
        vtl = hyst * -0.5f;

        threshold = vth / vcc;

        r1 = (threshold * rfb) / (1 - threshold);

        edtR1.removeTextChangedListener(r1rfb);
        edtR1.setText(getDoubleStringWithPrefix(r1, true));
        edtR1.addTextChangedListener(r1rfb);

        spR1.setSelection(getPrefixIndex(r1));

        recalculateThSummary();
    }

    private void recalculateTh() {
        threshold = r1 / (r1 + rfb);

        vth = threshold * vcc;
        vtl = threshold * gnd;

        hyst = vth - vtl;

        edtHyst.removeTextChangedListener(hyst_);
        edtHyst.setText(getDoubleStringWithPrefix(hyst, true));
        edtHyst.addTextChangedListener(hyst_);

        spHyst.setSelection(getPrefixIndex(hyst));

        recalculateThSummary();
    }

    private void recalculateThSummary() {
        lblThSummary.setText("VHth: " + getDoubleStringWithPrefix(vth, false) +  ", VLth: " + getDoubleStringWithPrefix(-vtl, false));
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
            if (edtHyst.isFocused()) return;
            r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
            recalculateTh();
        }else if (adapterView.getAdapter() == spHystAdapter) {
            lblHystPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (edtR1.isFocused()) return;
            hyst = getDoubleFromView(edtHyst) * getPrefixMultiplier(lblHystPrefix);
            calculateR1();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }

}
