package com.theholyhorse.ecalc.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.theholyhorse.ecalc.R;

public class HorseMenuDivider extends HorseBaseMenuItem {

    public HorseMenuDivider(String title) {
        super(Type.Divider, title);
        this.title = title;
    }

    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null || (convertView.getId() != R.layout.menu_divider_layout)) {
            convertView = inflater.inflate(R.layout.menu_divider_layout, null);
        }

        TextView lbl = convertView.findViewById(R.id.lbl_menu_item);

        lbl.setText(title);

        convertView.setOnClickListener(null);

        return convertView;
    }
}
