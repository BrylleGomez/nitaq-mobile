package com.brylle.nitaq_mobapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class BrowseFragment extends Fragment {

    /* Variables */

    private ArrayList<Package> browseList = new ArrayList<>();                                                // list to store event objects retrieved from Firebase
    private RecyclerView browseView;
    private BrowseAdapter browseAdapter;                                                                     // adapter to bind event objects in array list to recycler view
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();                          // retrieve Firestore instance
    private CollectionReference firestorePackageList = firebaseFirestore.collection("packages");    // retrieve reference to "events" collection// recycler view to display objects
    // set up preferences
    private SharedPreferences prefs;

    /* Initializer Functions */

    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Objects
        browseView = Objects.requireNonNull(getView()).findViewById(R.id.browse_recyclerview);
        // get the default SharedPreferences object
        prefs = getContext().getSharedPreferences("com.brylle.nitaq_mobapp.prefs", Context.MODE_PRIVATE);

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
                            addFetchedEventToArrayList(fetchedPackage);
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
        browseList.add(
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
//        Collections.sort(browseList, new Package.EventStartDateComparator());

        // Set up recycler view
        browseAdapter = new BrowseAdapter(browseList, new BrowseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Package pkg) {
                // Bind a click listener to the reyclerview item

                Toast.makeText(getContext(), "Wow", Toast.LENGTH_SHORT).show();

                // Check if already downloaded
                String downloaded = prefs.getString(pkg.getModule(), "0");
                if (downloaded.equals("0")) {
                    // save the instance variables
                    Editor editor = prefs.edit();
                    editor.putString(pkg.getModule(), pkg.getModule());
                    Log.d("BrowseFragment", "Saving " + pkg.getModule() + " to SharedPrefs");
                    editor.apply();
                } else {
                    Log.d("BrowseFragment", pkg.getModule() + " already downloaded!");
                }

            }
        });
        browseView.setLayoutManager(new LinearLayoutManager(getContext()));
        browseView.setAdapter(browseAdapter);

    }

}
