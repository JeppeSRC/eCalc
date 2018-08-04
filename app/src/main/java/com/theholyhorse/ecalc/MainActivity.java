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
package com.theholyhorse.ecalc;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.theholyhorse.ecalc.fragments.*;
import com.theholyhorse.ecalc.fragments.opamp.OpAmpInverting;
import com.theholyhorse.ecalc.fragments.opamp.OpAmpNonInverting;
import com.theholyhorse.ecalc.fragments.opamp.OpAmpSchmittInverting;
import com.theholyhorse.ecalc.fragments.opamp.OpAmpSchmittInvertingSingle;
import com.theholyhorse.ecalc.fragments.opamp.OpAmpSchmittNonInverting;
import com.theholyhorse.ecalc.menu.HorseBaseMenuItem;
import com.theholyhorse.ecalc.menu.HorseList;
import com.theholyhorse.ecalc.menu.HorseMenuDivider;
import com.theholyhorse.ecalc.menu.HorseMenuItem;
import com.theholyhorse.ecalc.menu.HorseMenuList;
import com.theholyhorse.ecalc.menu.HorseMenuListItem;
import com.theholyhorse.ecalc.menu.MenuAdapter;
import com.theholyhorse.ecalc.menu.MenuItemID;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HorseList.OnItemClickListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    // private NavigationView navigationView;
    private static SharedPreferences sharedPreferences;
    private static MainActivity mainActivity;
    private MenuAdapter adapter;
    private HorseBaseFragment frag = null;
    private FloatingActionButton fabInfo = null;

    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        //  navigationView = findViewById(R.id.navigation_view);

       ListView lv = findViewById(R.id.lv_menu);

        List<HorseBaseMenuItem> items = new HorseList(this);

        items.add(new HorseMenuItem(R.drawable.ic_home_black, "Home", MenuItemID.HOME_ID));
        items.add(new HorseMenuItem(R.drawable.ic_settings, "Settings", MenuItemID.SETTINGS_ID));
        items.add(new HorseMenuDivider("Op-Amps"));

        List<HorseBaseMenuItem> list2 = new ArrayList<>();

        list2.add(new HorseMenuListItem("Inverting", MenuItemID.OPAMP_AMP_INVERTING_ID));
        list2.add(new HorseMenuListItem("Non-Inverting", MenuItemID.OPAMP_AMP_NONINVERTING_ID));

        items.add(new HorseMenuList("Amplifiers", list2));

        List<HorseBaseMenuItem> list3 = new ArrayList<>();

        list3.add(new HorseMenuListItem("Inverting", MenuItemID.OPAMP_SCHMITT_TRIGGER_INVERTING_ID));
        list3.add(new HorseMenuListItem("Inverting Single", MenuItemID.OPAMP_SCHMITT_TRIGGER_INVERTING_SINGLE_ID));
        list3.add(new HorseMenuListItem("Non-Inverting", MenuItemID.OPAMP_SCHMITT_TRIGGER_NONINVERTING_ID));

        items.add(new HorseMenuList("Schmitt Triggers", list3));

        adapter = new MenuAdapter(this, items);

        lv.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black);
        actionBar.setTitle("Dank");

        onItemClicked(items.get(0), null);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        CoordinatorLayout lay = findViewById(R.id.frame_layout);

        fabInfo = lay.findViewById(R.id.fab_info);
        fabInfo.setVisibility(View.INVISIBLE);
        fabInfo.setOnClickListener(this);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private List<HorseBaseMenuItem> prevItems = new ArrayList<>();
    private HorseBaseMenuItem currentItem;

    public void onItemClicked(HorseBaseMenuItem item, View v) {
        Log.i("Test", v == null ? "Shits null" : "Not null");
        if (currentItem == item) {
            drawerLayout.closeDrawers();
            return;
        }

        item.selected = true;
        if (currentItem != null) {
            currentItem.selected = false;
            prevItems.add(currentItem);
        }

        currentItem = item;

        frag = null;

        switch (item.itemId) {
            case MenuItemID.HOME_ID:
                frag = new Home();
                break;
            case MenuItemID.SETTINGS_ID:
                frag = new PreferenceFrag();
                break;
            case MenuItemID.OPAMP_AMP_INVERTING_ID:
                frag = new OpAmpInverting();
                break;
            case MenuItemID.OPAMP_AMP_NONINVERTING_ID:
                frag = new OpAmpNonInverting();
                break;
            case MenuItemID.OPAMP_SCHMITT_TRIGGER_INVERTING_ID:
                frag = new OpAmpSchmittInverting();
                break;
            case MenuItemID.OPAMP_SCHMITT_TRIGGER_INVERTING_SINGLE_ID:
                frag = new OpAmpSchmittInvertingSingle();
                break;
            case MenuItemID.OPAMP_SCHMITT_TRIGGER_NONINVERTING_ID:
                frag = new OpAmpSchmittNonInverting();
                break;
        }

        if (frag == null) return;

        setFabVisibility(item);

        getSupportActionBar().setTitle(frag.getTitle());

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.container_layout, frag, null);
        if (v != null)
            trans.addToBackStack(null);
        trans.commit();

        drawerLayout.closeDrawers();
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.fab_info) {
            if (currentItem.itemId == MenuItemID.OPAMP_AMP_INVERTING_ID) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Info");
                builder.setPositiveButton("OK", null);
                builder.setMessage(R.string.opamp_inverting_help);
                builder.show();
            } else if (currentItem.itemId == MenuItemID.OPAMP_AMP_NONINVERTING_ID) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Info");
                builder.setPositiveButton("OK", null);
                builder.setMessage(R.string.opamp_noninverting_help);
                builder.show();
            } else if (currentItem.itemId == MenuItemID.OPAMP_SCHMITT_TRIGGER_INVERTING_ID) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Info");
                builder.setPositiveButton("OK", null);
                builder.setMessage(R.string.opamp_schmitt_inverting_help);
                builder.show();
            } else if (currentItem.itemId == MenuItemID.OPAMP_SCHMITT_TRIGGER_NONINVERTING_ID) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Info");
                builder.setPositiveButton("OK", null);
                builder.setMessage(R.string.opamp_schmitt_noninverting_help);
                builder.show();
            } else if (currentItem.itemId == MenuItemID.OPAMP_SCHMITT_TRIGGER_INVERTING_SINGLE_ID) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Info");
                builder.setPositiveButton("OK", null);
                builder.setMessage(R.string.opamp_schmitt_inverting_single_help);
                builder.show();
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();

        int lastIndex = prevItems.size() - 1;

        if (lastIndex < 0) return;

        currentItem.selected = false;
        currentItem = prevItems.get(lastIndex);
        currentItem.selected = true;

        getSupportActionBar().setTitle(currentItem.getTitle());

        prevItems.remove(lastIndex);

        adapter.notifyDataSetChanged();

        setFabVisibility(currentItem);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static MainActivity get() {
        return mainActivity;
    }

    private void setFabVisibility(HorseBaseMenuItem item) {
        if (item == null || fabInfo == null) return;

        int visible = View.VISIBLE;

        if (item.itemId == MenuItemID.HOME_ID || item.itemId == MenuItemID.SETTINGS_ID) {
            visible = View.INVISIBLE;
        }

        fabInfo.setVisibility(visible);
    }
}
