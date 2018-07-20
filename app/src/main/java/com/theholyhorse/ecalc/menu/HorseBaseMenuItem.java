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
    public int itemId;
    protected HorseBaseMenuItem self;

    protected HorseBaseMenuItem(Type type, String title, int itemId) {
        this.type = type;
        this.title = title;
        this.itemId = itemId;
        this.self = this;
    }

    public abstract View getView(LayoutInflater inflater, View convertView);


    public Type getType() { return type; }
    public String getTitle() { return title; }

    public void setOnClickListener(HorseList.OnItemClickListener listener) { this.listener = listener; }

}
