package com.brylle.nitaq_mobapp;

import android.content.Context;
import android.os.Build;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

//    protected void onCreate(Bundle savedInstanceState) {
//
//    }
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_browse, container, false);
        // Inflate the layout for this fragment
        final SwipeRefreshLayout pullToRefresh = v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        return v;
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

    public void Refresh(){
        //refreshFragment();
        browseView = Objects.requireNonNull(getView()).findViewById(R.id.browse_recyclerview);
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

    private void refreshFragment() {
        browseList = new ArrayList<>(); // reset arraylist
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

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

        // determine downloaded status
        String temp = prefs.getString(pkgModule, "0");
        boolean downloaded = !temp.equals("0");

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
                        next,
                        downloaded
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

                // Check if already downloaded
                String downloaded = prefs.getString(pkg.getModule(), "0");
                if (downloaded.equals("0")) {
                    // save the instance variables
                    Editor editor = prefs.edit();
                    editor.putString(pkg.getModule(), pkg.getModule());
                    Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    editor.apply();

                    // refresh -- very conky pls replace
                    refreshFragment();

                } else {
                    Toast.makeText(getContext(), "Already downloaded...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        browseView.setLayoutManager(new LinearLayoutManager(getContext()));
        browseView.setAdapter(browseAdapter);

    }

}
