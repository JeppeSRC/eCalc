package com.theholyhorse.ecalc.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

public class HorseMenuItem extends HorseBaseMenuItem {

    private int iconId;

    public HorseMenuItem(int iconId, String title, int itemId) {
        super(Type.Item, title, itemId);
        this.iconId = iconId;
    }

    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null || (convertView.getId() != R.layout.menu_item_layout)) {
            convertView = inflater.inflate(R.layout.menu_item_layout, null);
        }

        ImageView img = convertView.findViewById(R.id.img_menu_item);
        final TextView lbl = convertView.findViewById(R.id.lbl_menu_item);

        img.setImageResource(iconId);
        lbl.setText(title);

        convertView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                listener.onItemClicked(self, view);
            }
        });

        if (selected) {
            convertView.setBackgroundColor(0xFFDDDDDD);
        } else {
            convertView.setBackgroundColor(0xFFFFFFFF);
        }

        return convertView;
    }

    public int getIconId() { return iconId; }
}
