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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container_layout, new Home()).commit();

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

    public boolean onNavigationItemSelected(MenuItem item) {

        if (item == null || currentItem == item) {
            drawerLayout.closeDrawers();
            return false;
        }

        item.setChecked(true);

        if (currentItem != null) {
            currentItem.setChecked(false);
        }

        if (currentItem == null && item.getItemId() == R.id.navigation_home) {
            currentItem = item;
            return false;
        }

        currentItem = item;

        Fragment frag = null;

       switch (item.getItemId()) {
           case R.id.navigation_home:
               frag = new Home();
               break;
           case R.id.navigation_settings:
               frag = new PreferenceFrag();
               break;
           case R.id.navigation_opamp_inverting:
               break;
           case R.id.navigation_opamp_noninverting:
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
        trans.setReorderingAllowed(true);
        trans.commit();

        drawerLayout.closeDrawers();

        return false;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
