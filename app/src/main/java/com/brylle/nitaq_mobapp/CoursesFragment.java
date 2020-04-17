package com.brylle.nitaq_mobapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.brylle.aus_cs_app_android_j.AppUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CoursesFragment extends Fragment {

    /* Variables */

    private ArrayList<Package> eventsList = new ArrayList<>();                                                // list to store event objects retrieved from Firebase
    private RecyclerView eventsView;
    private CoursesAdapter eventsAdapter;                                                                     // adapter to bind event objects in array list to recycler view
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();                          // retrieve Firestore instance
    private CollectionReference firestorePackageList = firebaseFirestore.collection("packages");    // retrieve reference to "events" collection// recycler view to display objects
    private SharedPreferences prefs;

    /* Initializer Functions */

    public CoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Objects
        eventsView = Objects.requireNonNull(getView()).findViewById(R.id.events_recyclerview);
        prefs = getContext().getSharedPreferences("com.brylle.nitaq_mobapp.prefs", Context.MODE_PRIVATE);

        // test - clear /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

//        // Fetches all event database entries and stores them in an array list of event objects
//        for (int i = 0; i < 10; i++) {
//            addRandomEvents();
//        }
//        loadRecyclerView();

        firestorePackageList.get()                                                // Fetch all event entries from database
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot fetchedPackages) {

                    for (DocumentSnapshot fetchedPackage : fetchedPackages) {       // Iterate through all fetched events

                        // check if package is part of downloaded
                        String tempModule = fetchedPackage.getString(AppUtils.KEY_MODULE);
                        String downloaded = prefs.getString(tempModule, "0");
                        if (downloaded.equals("0")) {
                            addFetchedEventToArrayList(fetchedPackage);
                        }

                    }

                    // load recycler view from adapter
                    loadRecyclerView();

                }

            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("CoursesFragment", "Error fetching packages: ", e);
                }
            });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /* Helper Functions */

    private void addFetchedEventToArrayList(DocumentSnapshot fetchedPackage) {

        // Store info of each fetched event in temp variable
        String pkgSubject = fetchedPackage.getString(AppUtils.KEY_SUBJECT);
        String pkgTopic = fetchedPackage.getString(AppUtils.KEY_TOPIC);
        String pkgModule = fetchedPackage.getString(AppUtils.KEY_MODULE);
        ArrayList<String> lessons = (ArrayList<String>) fetchedPackage.get(AppUtils.KEY_LESSONS);
        ArrayList<String> questions = (ArrayList<String>) fetchedPackage.get(AppUtils.KEY_QUESTIONS);
        ArrayList<String> answers = (ArrayList<String>) fetchedPackage.get(AppUtils.KEY_ANSWERS);
        ArrayList<String> correct_answers = (ArrayList<String>) fetchedPackage.get(AppUtils.KEY_CORRECT_ANSWERS);
        ArrayList<String> next = (ArrayList<String>) fetchedPackage.get(AppUtils.KEY_NEXT);

        // Create an Package object with the retrieved event info (in temp variables)
        // Add created Package object to the container
        eventsList.add(
                new Package(
                        pkgSubject,
                        pkgTopic,
                        pkgModule,
                        lessons,
                        questions,
                        answers,
                        correct_answers,
                        next
                )
        );
        Log.d("CoursesFragment ", fetchedPackage.toString() + " added!");

    }

    private void loadRecyclerView() {

//        // sort events array list according to start date
//        Collections.sort(eventsList, new Package.EventStartDateComparator());

        // Set up recycler view
        eventsAdapter = new CoursesAdapter(eventsList, new CoursesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Package pkg) {
                // Bind a click listener to the reyclerview item

                // create intent, pass event object members as extras, and start activity
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("pkgSubject", pkg.getSubject());
                intent.putExtra("pkgTopic", pkg.getTopic());
                intent.putExtra("pkgModule", pkg.getModule());
                intent.putExtra("pkgLessons", pkg.getLessons());
                intent.putExtra("pkgQuestions", pkg.getQuestions());
                intent.putExtra("pkgAnswers", pkg.getAnswers());
                intent.putExtra("pkgCorrectAnswers", pkg.getCorrect_answers());
                intent.putExtra("pkgNext", pkg.getNext());
                startActivity(intent);
            }
        });
        eventsView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsView.setAdapter(eventsAdapter);

    }

}
