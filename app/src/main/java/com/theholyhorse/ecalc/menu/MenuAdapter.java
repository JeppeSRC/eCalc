package com.theholyhorse.ecalc.menu;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

public class MenuAdapter extends ArrayAdapter {

    private Context context;
    private List<HorseBaseMenuItem> items;
    private LayoutInflater inflater;


    public MenuAdapter(Context context, List<HorseBaseMenuItem> items) {
        super(context, 0);
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).getView(inflater, convertView);
    }

    public Object getItem(int i) {
        return items.get(i);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getCount() {
        return items.size();
    }

    public long getItemId(int i) {
        return 0;
    }
}
