package com.theholyhorse.ecalc.menu;

import android.view.LayoutInflater;
import android.view.View;

public abstract class HorseBaseMenuItem {

    public enum Type {
        Item,
        List,
        Divider,
        ListItem
    }

    protected Type type;
    protected String title;
    protected HorseList.OnItemClickListener listener;
    public int positionId;

    protected HorseBaseMenuItem(Type type, String title) {
        this.type = type;
        this.title = title;

    }

    public abstract View getView(LayoutInflater inflater, View convertView);


    public Type getType() { return type; }
    public String getTitle() { return title; }

    public void setOnClickListener(HorseList.OnItemClickListener listener) { this.listener = listener; }

}
