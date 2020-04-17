package com.brylle.nitaq_mobapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment pfragment = new BrowseFragment();
    Fragment efragment = new CoursesFragment();
    String data;

    Fragment active = efragment;

    /* Initializer functions */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize navigation toolbar
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationView);

        // Get intent from QR activity
        Intent intent = getIntent();                                // get intent from QR activity (in the case it was started by QRScanActivity)
        data = intent.getStringExtra("qrData");      // will be null if this activity was NOT started by QRScanActivity
        Bundle bundle = new Bundle();                             // create Bundle to pass to QRFragment info from QRScanActivity (in the case this activity was started by QRScanActivity)

        fragmentManager.beginTransaction().add(R.id.frameLayout, pfragment, "2").hide(pfragment).commit();
        fragmentManager.beginTransaction().add(R.id.frameLayout, efragment, "1").commit();
        active = efragment;

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    /* Action functions */

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            // Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.eventlistBtn:
                    fragmentManager.beginTransaction().hide(active).show(efragment).commit();
                    active = efragment;
                    return true;

                case R.id.profileBtn:
                    fragmentManager.beginTransaction().hide(active).show(pfragment).commit();
                    active = pfragment;
                    return true;
            }
            return false;
        }
    };

}
