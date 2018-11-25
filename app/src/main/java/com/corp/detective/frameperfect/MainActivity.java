package com.corp.detective.frameperfect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.corp.detective.core.CurrentActivity;
import com.corp.detective.database.MainDatabase;
import com.corp.detective.frameperfect.fragments.CharacterListFragment;
import com.corp.detective.frameperfect.helper.Util;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private Util util = new Util();
    CharacterListFragment mCharacterListFragment;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainDatabase.initDatabase(this);

        util.setLanguage(this);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        setCurrentActivity();

        initializeLayout();

        setBackStackChangeListener();

        showMainFragment();
    }

    private void setCurrentActivity() {
        CurrentActivity.setActivity(this);
    }

    private void initializeLayout()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setBackStackChangeListener()
    {
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    private void showMainFragment() {
        mCharacterListFragment = new CharacterListFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, mCharacterListFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.activity_app_preference:
                Intent intent = new Intent(this, AppPreferenceActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        util.setLanguage(this);
        this.recreate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view menuItem clicks here.
        switch(menuItem.getItemId())
        {
            //handle cases
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackStackChanged() {
        drawerToggle.setDrawerIndicatorEnabled(mFragmentManager.getBackStackEntryCount() == 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(mFragmentManager.getBackStackEntryCount() > 0);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if(mFragmentManager.getBackStackEntryCount() != 0){
            // there is something on the stack, pop it
            mFragmentManager.popBackStack();
        } else {
            // I'm on the landing page, exit
            super.onBackPressed();
        }
    }
}

