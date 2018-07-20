package com.theholyhorse.ecalc.menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.theholyhorse.ecalc.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class MenuListAdapter extends BaseExpandableListAdapter {

    private String title;
    private List<HorseBaseMenuItem> items;
    private LayoutInflater inflater;
    private Context context;

    public MenuListAdapter(Context context, String title, List<HorseBaseMenuItem> items) {
        this.context = context;
        this.title = title;
        this.items = items;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getGroupCount() {
        return 1;
    }

    public int getChildrenCount(int i) {
        return items.size();
    }

    public Object getGroup(int i) {
        if (i != 0) {
            Log.d("MenuListAdapter", "Only one group supported");
        }

        return items;
    }

    public Object getChild(int group, int child) {
        return ((List<HorseMenuListItem>)getGroup(group)).get(child);
    }

    public long getGroupId(int i) {
        return 0;
    }

    public long getChildId(int i, int i1) {
        return i1;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null || (view.getId() != R.layout.menu_list_group_layout)) {
            view = inflater.inflate(R.layout.menu_list_group_layout, null);

            TextView lbl = view.findViewById(R.id.lbl_menu_item);

            lbl.setText(title);
        }

        return view;
    }

    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        HorseBaseMenuItem item = items.get(i1);

        if (view == null || (view.getId() != R.layout.menu_list_item_layout)) {
            view = inflater.inflate(R.layout.menu_list_item_layout, null);


            TextView lbl = view.findViewById(R.id.lbl_menu_item);
            lbl.setText(item.getTitle());
        }

        if (item.selected) {
            view.setBackgroundColor(0xFFDDDDDD);
        } else {
            view.setBackgroundColor(0xFFFFFFFF);
        }

        return view;
    }

    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
