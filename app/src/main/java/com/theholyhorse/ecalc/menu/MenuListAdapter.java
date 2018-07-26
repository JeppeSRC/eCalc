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

            view.setPadding(40,0 ,0 ,0);
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
