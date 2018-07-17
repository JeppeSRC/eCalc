package com.theholyhorse.ecalc.fragments.opamp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.theholyhorse.ecalc.R;

public class OpAmpNonInverting extends Fragment {

    private View view;
    private ImageView imageView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        view = inflater.inflate(R.layout.opamp_noninverting_layout, container, false);

        imageView = view.findViewById(R.id.opamp_noninverting_image);
        imageView.setImageResource(R.drawable.op_amp_noninverting);

        return view;
    }

}
