package com.theholyhorse.ecalc;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.theholyhorse.ecalc.fragments.*;
import com.theholyhorse.ecalc.fragments.opamp.OpAmpInverting;
import com.theholyhorse.ecalc.fragments.opamp.OpAmpNonInverting;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static SharedPreferences sharedPreferences;
    private static MainActivity mainActivity;

    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container_layout, new Home()).commit();

        currentItem = navigationView.getMenu().getItem(0);
        currentItem.setChecked(true);

        navigationView.setNavigationItemSelectedListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private MenuItem currentItem = null;
    private ArrayList<MenuItem> prevItems = new ArrayList<>();

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
           case R.id.navigation_opamp_schmitt:
               break;
           case R.id.navigation_opamp_differential:
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
        int prevIndex = prevItems.size()-1;

        if (prevIndex < 0) return;

        currentItem.setChecked(false);
        currentItem = prevItems.get(prevIndex);
        currentItem.setChecked(true);

        prevItems.remove(prevIndex);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static MainActivity get() {
        return mainActivity;
    }
}
