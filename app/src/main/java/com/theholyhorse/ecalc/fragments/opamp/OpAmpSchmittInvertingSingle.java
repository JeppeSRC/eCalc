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
import android.util.Log;
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

                if (vcc <= 0.0) return;

                double halfHyst = hyst * 0.5;

                if (vswitch >= vcc) {
                    Toast.makeText(MainActivity.get(), "vswitch must < supply voltage", Toast.LENGTH_SHORT).show();
                    edtVswitch.setText("");
                    return;
                }

                if (vswitch + halfHyst >= vcc || vswitch - halfHyst <= gnd) {
                    edtHyst.setText("");
                    return;
                }


                recalculateR1R2Rfb();
            }

            public void afterTextChanged(Editable editable) { }
        };

        r1rfb = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
                rfb = getDoubleFromView(edtRfb) * getPrefixMultiplier(lblRfbPrefix);
                r2 = getDoubleFromView(edtR2) * getPrefixMultiplier(lblR2Prefix);

                if (r1 > 0.0 && r2 > 0.0 && rfb > 0.0)
                    recalculateTh();
            }

            public void afterTextChanged(Editable editable) { }
        };

        vswitch_ = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldVswitch = vswitch;
                vswitch = getDoubleFromView(edtVswitch) * getPrefixMultiplier(lblVswitchPrefix);

                if (vswitch <= 0.0) return;

                double halfHyst = hyst * 0.5;

                if (vswitch >= vcc) {
                    Toast.makeText(MainActivity.get(), "vswitch must < supply voltage", Toast.LENGTH_SHORT).show();
                    edtVswitch.setText("");
                    return;
                }

                if (vswitch + halfHyst >= vcc || vswitch - halfHyst <= gnd) {
                    edtHyst.setText("");
                    return;
                }


                recalculateR1R2Rfb();
            }

            public void afterTextChanged(Editable s) { }
        };

        hyst_ = new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                oldHyst = hyst;
                hyst = getDoubleFromView(edtHyst) * getPrefixMultiplier(lblHystPrefix);

                if (hyst <= 0.0) return;

                double halfHyst = hyst * 0.5;

                if (vswitch + halfHyst >= vcc || vswitch - halfHyst <= gnd) {
                    Toast.makeText(MainActivity.get(), "VHth and VLth must be within supply voltage", Toast.LENGTH_SHORT).show();
                    edtHyst.setText("");
                    return;
                }

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
        edtRfb.setText("10");
        edtR1.setText("10");
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

        int newIndex = getPrefixIndex(vswitch);

        if (newIndex != spVswitch.getSelectedItemPosition()) {
            noCalc++;
            spVswitch.setSelection(newIndex);
        }

        edtHyst.removeTextChangedListener(hyst_);
        edtHyst.setText(getDoubleStringWithPrefix(hyst, true));
        edtHyst.addTextChangedListener(hyst_);

        newIndex = getPrefixIndex(hyst);

        if (newIndex != spHyst.getSelectedItemPosition()) {
            noCalc++;
            spHyst.setSelection(newIndex);
        }

        lblThSummary.setText("VHth: " + getDoubleStringWithPrefix(vth, false) +  ", VLth: " + getDoubleStringWithPrefix(vtl, false));
    }

    private void recalculateR1R2Rfb() {
        double halfHyst = hyst * 0.5;

        vth = vswitch + halfHyst;
        vtl = vswitch - halfHyst;

        double thH = vth / vcc;
        double thL = vtl / vcc;

        double tmpR2 = 0.0;
        double tmpR1 = 0.0;

        for (int i = 0; i < 200000; i++) {
            r2 = (thH * resistorParallel(r1, rfb)) / (1.0 - thH);
            r1 = resistorParallel(r2, rfb) * (1 - thL) / thL;
        }

        edtR2.removeTextChangedListener(r1rfb);
        edtR2.setText(getDoubleStringWithPrefix(r2, true));
        edtR2.addTextChangedListener(r1rfb);

        int newIndex = getPrefixIndex(r2);

        if (newIndex != spR2.getSelectedItemPosition()) {
            noCalc++;
            spR2.setSelection(newIndex);
        }

        edtR1.removeTextChangedListener(r1rfb);
        edtR1.setText(getDoubleStringWithPrefix(r1, true));
        edtR1.addTextChangedListener(r1rfb);

        newIndex = getPrefixIndex(r1);

        if (newIndex != spR1.getSelectedItemPosition()) {
            noCalc++;
            spR1.setSelection(newIndex);
        }


        lblThSummary.setText("VHth: " + getDoubleStringWithPrefix(vth, false) +  ", VLth: " + getDoubleStringWithPrefix(vtl, false));
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
            if (noCalc > 0){
                noCalc--;
                return;
            }
            r1 = getDoubleFromView(edtR1) * getPrefixMultiplier(lblR1Prefix);
            recalculateTh();
        } else if (adapterView.getAdapter() == spR2Adapter) {
            lblR2Prefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (noCalc > 0){
                noCalc--;
                return;
            }
            r2 = getDoubleFromView(edtR2) * getPrefixMultiplier(lblR2Prefix);
            recalculateTh();
        } else if (adapterView.getAdapter() == spHystAdapter) {
            lblHystPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (noCalc > 0 ){
                noCalc--;
                return;
            }
            hyst = getDoubleFromView(edtHyst) * getPrefixMultiplier(lblHystPrefix);

            double halfHyst = hyst * 0.5;

            if (vswitch + halfHyst >= vcc || vswitch - halfHyst <= gnd) {
                edtHyst.setText(getDoubleStringWithPrefix(0, true));
            }

            recalculateR1R2Rfb();
        } else if (adapterView.getAdapter() == spVswitchAdapter) {
            lblVswitchPrefix.setText((CharSequence)adapterView.getItemAtPosition(i));
            if (noCalc > 0){
                noCalc--;
                return;
            }

            vswitch = getDoubleFromView(edtVswitch) * getPrefixMultiplier(lblVswitchPrefix);

            double halfHyst = hyst * 0.5;

            if (vswitch + halfHyst >= vcc || vswitch - halfHyst <= gnd) {
                edtHyst.setText(getDoubleStringWithPrefix(0, true));
            }

            recalculateR1R2Rfb();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) { }


}
