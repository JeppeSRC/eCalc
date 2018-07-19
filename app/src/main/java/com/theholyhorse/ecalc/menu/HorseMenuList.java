package com.theholyhorse.ecalc.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

import java.util.List;

public class HorseMenuList extends HorseBaseMenuItem {

    private List<HorseBaseMenuItem> items;

    public HorseMenuList(String title, List<HorseBaseMenuItem> items) {
        super(Type.List, title);

        this.items = items;
    }

    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.menu_list_layout, null);
        }

        ExpandableListView lv = convertView.findViewById(R.id.elv_menu);

        MenuListAdapter adapter = new MenuListAdapter(MainActivity.get(), title, items);

        lv.setAdapter(adapter);

        return convertView;
    }
}
