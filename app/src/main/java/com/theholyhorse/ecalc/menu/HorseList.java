package com.theholyhorse.ecalc.menu;

import android.view.View;

import java.util.ArrayList;

public class HorseList extends ArrayList<HorseBaseMenuItem> {

    public interface OnItemClickListener {
        void onItemClicked(View v,int listPosition, int altPosition);
    }

    private OnItemClickListener listener;
    private int currentPosition = 0;

    public HorseList(OnItemClickListener listener) {
        this.listener = listener;
    }

    public boolean add(HorseBaseMenuItem item) {
        if (item.getType() != HorseBaseMenuItem.Type.Divider) item.positionId = currentPosition++;
        item.setOnClickListener(listener);
        return super.add(item);
    }
}
