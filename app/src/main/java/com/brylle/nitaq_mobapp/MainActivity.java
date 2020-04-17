package com.brylle.nitaq_mobapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment efragment = new CoursesFragment();
    Fragment pfragment = new BrowseFragment();
    String data;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();                          // retrieve Firestore instance
    private CollectionReference firestorePackageList = firebaseFirestore.collection("packages");    // retrieve reference to "events" collection// recycler view to display objects

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

        // TESTING //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Button button = findViewById(R.id.button_generate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create document
                final HashMap<String,Object> entry = new HashMap<>();
                entry.put(AppUtils.KEY_SUBJECT, "Math");
                entry.put(AppUtils.KEY_TOPIC, "Math");
                entry.put(AppUtils.KEY_MODULE, "Math");
                ArrayList<String> lessons = new ArrayList<>(); lessons.add("L1"); lessons.add("L2"); lessons.add("L3");
                entry.put(AppUtils.KEY_LESSONS, lessons);
                ArrayList<String> questions = new ArrayList<>(); questions.add("Q1"); questions.add("Q2"); questions.add("Q3");
                entry.put(AppUtils.KEY_QUESTIONS, questions);
                ArrayList<String> answers = new ArrayList<>(); answers.add("1,2,3,4"); answers.add("1,2,3,4"); answers.add("1,2,3,4");
                entry.put(AppUtils.KEY_ANSWERS, answers);
                ArrayList<String> correct_answers = new ArrayList<>(); correct_answers.add("a"); correct_answers.add("b"); correct_answers.add("d");
                entry.put(AppUtils.KEY_CORRECT_ANSWERS, correct_answers);
                ArrayList<String> next = new ArrayList<>(); next.add("next1"); next.add("next2"); next.add("end");
                entry.put(AppUtils.KEY_NEXT, next);

                // Upload document to Firestore (nested inside above addOnSuccessListener)
                firestorePackageList.document("package_2").set(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

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
