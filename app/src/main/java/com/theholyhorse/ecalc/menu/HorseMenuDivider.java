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
import android.widget.TextView;

import com.theholyhorse.ecalc.R;

public class HorseMenuDivider extends HorseBaseMenuItem {

    public HorseMenuDivider(String title) {
        super(Type.Divider, title, -1);
        this.title = title;
    }

    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null || (convertView.getId() != R.layout.menu_divider_layout)) {
            convertView = inflater.inflate(R.layout.menu_divider_layout, null);
        }

        TextView lbl = convertView.findViewById(R.id.lbl_menu_item);

        lbl.setText(title);

        convertView.setOnClickListener(null);

        return convertView;
    }
}
