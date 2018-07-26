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

    public boolean selected;

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
