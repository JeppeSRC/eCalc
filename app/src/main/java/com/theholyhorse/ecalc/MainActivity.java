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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements HorseList.OnItemClickListener {

    private DrawerLayout drawerLayout;
    // private NavigationView navigationView;
    private static SharedPreferences sharedPreferences;
    private static MainActivity mainActivity;


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

        MenuAdapter adapter = new MenuAdapter(this, items);

        lv.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container_layout, new Home()).commit();

        //  currentItem = navigationView.getMenu().getItem(0);
        // currentItem.setChecked(true);

        // navigationView.setNavigationItemSelectedListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemClicked(HorseBaseMenuItem item, View v) {

        Fragment frag = null;

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

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.container_layout, frag, null);
        trans.addToBackStack(null);
        trans.commit();

        drawerLayout.closeDrawers();
    }

/*
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item == null || currentItem == item) {
            drawerLayout.closeDrawers();
            return false;
        }

        item.setChecked(true);

        currentItem.setChecked(false);
        prevItems.add(currentItem);

        currentItem = item;

        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                frag = new Home();
                break;
            case R.id.navigation_settings:
                frag = new PreferenceFrag();
                break;
            case R.id.navigation_opamp_noninverting:
                frag = new OpAmpNonInverting();
                break;
            case R.id.navigation_opamp_inverting:
                frag = new OpAmpInverting();
                break;
            case R.id.navigation_opamp_schmitt_inverting:
                frag = new OpAmpSchmittInverting();
                break;
            case R.id.navigation_opamp_schmitt_noninverting:
                frag = new OpAmpSchmittNonInverting();
                break;
            case R.id.navigation_opamp_schmitt_inverting_single:
                frag = new OpAmpSchmittInvertingSingle();
                break;
        }

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        trans.replace(R.id.container_layout, frag);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        trans.addToBackStack(null);
        trans.commit();

        drawerLayout.closeDrawers();

        return false;
    }

    public void onBackPressed() {
        super.onBackPressed();
        int prevIndex = prevItems.size() - 1;

        if (prevIndex < 0)
            return;

        currentItem.setChecked(false);
        currentItem = prevItems.get(prevIndex);
        currentItem.setChecked(true);

        prevItems.remove(prevIndex);
    }
*/

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static MainActivity get() {
        return mainActivity;
    }
}
