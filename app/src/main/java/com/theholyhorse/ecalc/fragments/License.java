package com.theholyhorse.ecalc.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theholyhorse.ecalc.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class License extends HorseBaseFragment {

    public License() {
        super("License(s)");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
       View v = inflater.inflate(R.layout.menu_license, container, false);

        TextView tv = v.findViewById(R.id.tv_info);

        String data = "<strong>Third-party licenses</strong><br/><br/>";

        InputStream in = getResources().openRawResource(R.raw.belgrano_license);

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String tmp;

        try {

            while ((tmp = reader.readLine()) != null) {
                data += tmp + "<br/>";
            }

        } catch (Exception e) {

        }

        tv.setText(Html.fromHtml(data));

       return v;
    }

}
