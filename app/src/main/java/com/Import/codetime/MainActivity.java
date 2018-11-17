package com.Import.codetime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.Import.codetime.work.FetchDataWork;
import com.Import.codetime.work.FreshDataWork;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FragmentChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static boolean isPrefChanged = true;
    private SharedPreferences sharedPreferences;

    private Toolbar toolbar;
    private int backPressCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        startPeriodicWork();

        isPrefChanged = initPrefChangedValue();

        //initially display home fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, new HomeFragment());
        transaction.commit();
    }

    private boolean initPrefChangedValue() {
        return sharedPreferences.getBoolean(FreshDataWork.PREF_CHANGED_KEY, true);
    }

    private void startPeriodicWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest syncDataWork = new PeriodicWorkRequest.Builder(
                FetchDataWork.class,
                24,
                TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("fetch data", ExistingPeriodicWorkPolicy.KEEP, syncDataWork);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        sharedPreferences.edit().putBoolean(FreshDataWork.PREF_CHANGED_KEY, isPrefChanged).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count == 0 && backPressCount == 0) {
            Snackbar.make(drawer, "Press again to exit", Snackbar.LENGTH_SHORT).show();
            backPressCount++;
        } else {
            backPressCount = 0;
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item);
        return true;
    }

    void displaySelectedScreen(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        switch (id){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_github:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/Ni3verma/CodeTime"));
                startActivity(intent);
                break;
            case R.id.nav_credits:
                Toast.makeText(this, "to be implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share_app:
                Toast.makeText(this, "app will be opened in play store", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_feedback:
                Intent intent1 = new Intent(Intent.ACTION_SENDTO);
                intent1.setData(Uri.parse("mailto:canvas.nv@gmail.com"));
                intent1.putExtra(Intent.EXTRA_SUBJECT, "feedback of CodeTime app");
                if (intent1.resolveActivity(getPackageManager()) != null)
                    startActivity(intent1);
                break;
            case R.id.nav_rate_app:
                Toast.makeText(this, "app will be opened in play store", Toast.LENGTH_SHORT).show();
        }

        if (fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            if (fragment instanceof SettingsFragment && getSupportFragmentManager().getBackStackEntryCount() == 0)
                transaction.addToBackStack("settings");

            transaction.replace(R.id.content_frame,fragment);
            transaction.commit();

            toolbar.setTitle(item.getTitle());
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void openListFragment(Fragment fragment, ImageView imageView) {
        //TODO: clear selected item in nav drawer

        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(fragment.toString());
        if (imageView != null) {
            fragmentTransaction.addSharedElement(imageView, ViewCompat.getTransitionName(imageView));
        }

        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment prevFragment = fragmentManager.findFragmentById(R.id.content_frame);
        prevFragment.setExitTransition(new Fade());

        fragmentTransaction.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        isPrefChanged = true;
    }
}
