package com.mumu.projectli;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MoneyFragment.OnFragmentInteractionListener,
        ElectricityFragment.OnFragmentInteractionListener {

    FloatingActionButton mFab;
    View mCoordinateLayoutView;
    List<MainFragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Save the coordinate layout view for animated elements
        mCoordinateLayoutView = findViewById(R.id.coordinator_layout);

        // construct fragment list
        mFragmentList = new ArrayList<>();
        try {
            mFragmentList.add(ElectricityFragment.class.newInstance());
            mFragmentList.add(MoneyFragment.class.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.home_welcome), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Snackbar.make(mCoordinateLayoutView, getString(R.string.home_welcome), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Not yet", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Create a new fragment and specify the fragment to show based on nav item clicked
        final MainFragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();

        int id = item.getItemId();

        if (id == R.id.nav_money) {
            fragment = mFragmentList.get(1);
            if (actionBar != null) actionBar.setTitle(getString(R.string.drawer_money));
        } else if (id == R.id.nav_electricity) {
            fragment = mFragmentList.get(0);
            if (actionBar != null) actionBar.setTitle(getString(R.string.drawer_electricity));
        } else if (id == R.id.nav_bodyweight) {
            fragment = mFragmentList.get(1);
            if (actionBar != null) actionBar.setTitle(getString(R.string.drawer_body_weight));
        } else if (id == R.id.nav_share) {
            fragment = null;
            showSnackBarMessage("Share function implementing");
        } else if (id == R.id.nav_send) {
            fragment = null;
            showSnackBarMessage("Send function implementing");
        } else {
            return false;
        }

        // null fragment means this action doesn't need to transfer to other fragment
        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            // Setting onClickListener
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.onFabClick(view);
                }
            });
        }

        // Dismiss drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,"Hello from the activity", Toast.LENGTH_SHORT).show();
    }

    public FloatingActionButton getFab() {
        return mFab;
    }

    public void showSnackBarMessage(String msg) {
        Snackbar.make(mCoordinateLayoutView, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
