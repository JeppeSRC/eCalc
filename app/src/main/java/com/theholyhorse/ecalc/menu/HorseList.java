package com.theholyhorse.ecalc.menu;

import android.view.View;

import java.util.ArrayList;

public class HorseList extends ArrayList<HorseBaseMenuItem> {

    public interface OnItemClickListener {
        void onItemClicked(HorseBaseMenuItem item, View v);
    }

    private OnItemClickListener listener;

    public HorseList(OnItemClickListener listener) {
        this.listener = listener;
    }

    public boolean add(HorseBaseMenuItem item) {
        item.setOnClickListener(listener);
        return super.add(item);
    }
}
