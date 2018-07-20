package com.theholyhorse.ecalc.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;

import com.theholyhorse.ecalc.MainActivity;
import com.theholyhorse.ecalc.R;

import java.util.List;

public class HorseMenuList extends HorseBaseMenuItem {

    private List<HorseBaseMenuItem> items;

    private static ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView listView, View view, int ii, long l) {

            MenuListAdapter adapter = (MenuListAdapter)listView.getExpandableListAdapter();

            int width = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);

            View groupView = adapter.getGroupView(0, false, null, null);
            groupView.measure(width, View.MeasureSpec.UNSPECIFIED);

            int height = groupView.getMeasuredHeight();

            if (!listView.isGroupExpanded(0)) {

                View v = adapter.getChildView(0, 0, false, null, null);
                v.measure(width, View.MeasureSpec.UNSPECIFIED);

               height += v.getMeasuredHeight() * adapter.getChildrenCount(0) - 1;

            }

            LayoutParams params = listView.getLayoutParams();
            params.height = height;

            listView.setLayoutParams(params);
            listView.requestLayout();

            return false;
        }
    };

    public HorseMenuList(String title, List<HorseBaseMenuItem> items) {
        super(Type.List, title, -1);

        this.items = items;
    }

    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null || (convertView.getId() != R.layout.menu_list_layout)) {
            convertView = inflater.inflate(R.layout.menu_list_layout, null);
        }

        final ExpandableListView lv = convertView.findViewById(R.id.elv_menu);

        MenuListAdapter adapter = new MenuListAdapter(MainActivity.get(), title, items);

        lv.setAdapter(adapter);
        lv.setOnGroupClickListener(onGroupClickListener);

        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                listener.onItemClicked(items.get(i1), view);
                return false;
            }
        });

        return convertView;
    }
}
