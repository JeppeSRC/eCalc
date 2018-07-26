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

        vswitch_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vswitch = getDoubleFromView(edtVswitch) * getPrefixMultiplier(lblVswitchPrefix);

                if (vswitch > 0.0)
                    recalculateR1R2Rfb();
            }

            public void afterTextChanged(Editable s) { }
        };

        hyst_ = new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hyst = getDoubleFromView(edtHyst) * getPrefixMultiplier(lblHystPrefix);

                if (hyst > 0.0)
                    recalculateR1R2Rfb();
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
        edtR2.setText("10");

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

    private void recalculateR1R2Rfb() {
        double halfHyst = hyst * 0.5;

        vth = vswitch + halfHyst;
        vtl = vswitch - halfHyst;

        double thH = vth / vcc;
        double thL = vtl / vcc;

        r2 = (thH * resistorParallel(r1, rfb)) / (1.0 - thH);
        r1 = resistorParallel(r2, rfb) * (1 - thL) / thL;

        edtR2.removeTextChangedListener(r1rfb);
        edtR2.setText(getDoubleStringWithPrefix(r2, true));
        edtR2.addTextChangedListener(r1rfb);

        spR2.setSelection(getPrefixIndex(r2));

        edtR1.removeTextChangedListener(r1rfb);
        edtR1.setText(getDoubleStringWithPrefix(r1, true));
        edtR1.addTextChangedListener(r1rfb);

        spR1.setSelection(getPrefixIndex(r1));

        lblThSummary.setText("VHth: " + getDoubleStringWithPrefix(vth, false) +  ", VLth: " + getDoubleStringWithPrefix(vtl, false));
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getAdapter() == spVccAdapter) {
            lblVccPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            vccgnd.onTextChanged(null, 0, 0, 0);
        } else if (adapterView.getAdapter() == spRfbAdapter) {
            lblRfbPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            rfb = getDoubleFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
        } else if (adapterView.getAdapter() == spR1Adapter) {
            lblR1Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (edtHyst.isFocused() || edtVswitch.isFocused()) return;
            r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
            recalculateTh();
        } else if (adapterView.getAdapter() == spR2Adapter) {
            lblR2Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (edtHyst.isFocused() || edtVswitch.isFocused()) return;
            r2 = getDoubleFromView(edtR2) * getPrefixMultiplier(lblR2Prefix);
            recalculateTh();
        } else if (adapterView.getAdapter() == spHystAdapter) {
            lblHystPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (edtR1.isFocused() || edtR2.isFocused() || edtRfb.isFocused()) return;
            hyst = getDoubleFromView(edtHyst) * getPrefixMultiplier(lblHystPrefix);
            recalculateR1R2Rfb();
        } else if (adapterView.getAdapter() == spVswitchAdapter) {
            lblVswitchPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (edtR1.isFocused() || edtR2.isFocused() || edtRfb.isFocused()) return;
            vswitch = getDoubleFromView(edtVswitch) * getPrefixMultiplier(lblVswitchPrefix);
            recalculateR1R2Rfb();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }


}
