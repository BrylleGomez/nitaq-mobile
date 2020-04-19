//
// MIT License
//
// Copyright (C) 2018 HypeLabs Inc.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package com.brylle.nitaq_mobapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypelabs.hype.Error;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;
import com.hypelabs.hype.Message;
import com.hypelabs.hype.MessageInfo;
import com.hypelabs.hype.MessageObserver;
import com.hypelabs.hype.NetworkObserver;
import com.hypelabs.hype.StateObserver;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactActivity extends Activity implements Store.Delegate, StateObserver, NetworkObserver {

    private static final int REQUEST_ACCESS_COARSE_LOCATION_ID = 0;
    private String displayName;
    private static WeakReference<ContactActivity> defaultInstance;

    public static String announcement = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    //    private static final String TAG = MyApplication.class.getName();
    private static final String TAG = "DEBUG";
    private Map<String, Store> stores;
    private Activity activity;
    private boolean isConfigured = false;
    private MyApplication chatApplication;
    boolean playerFound = false;

    // lesson elements (placeholders only for multiplayer)
    private String mode;
    private String subject;
    private String topic;
    private String adventure;
    private ArrayList<String> concepts;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private ArrayList<String> correct_answers;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ListView listView;
        chatApplication = (MyApplication) getApplication();
        //final MyApplication chatApplication = (MyApplication)getApplication();
        final ContactActivity contactActivity = this;
        //chatApplication.setActivity(this);

        requestPermissions(this);

        // retrieve lesson content from CoursesFragment to pass on to ChatActivity
        // Retrieve lesson content from intent if multiplayer
        Bundle extras = getIntent().getExtras();
        mode = extras.getString("mode");
        if (mode.equals("multiplayer")) {
            subject = extras.getString("pkgSubject");
            topic = extras.getString("pkgTopic");
            adventure = extras.getString("pkgModule");
            concepts = (ArrayList<String>) extras.get("pkgLessons");
            questions = (ArrayList<String>) extras.get("pkgQuestions");
            answers = (ArrayList<String>) extras.get("pkgAnswers");
            correct_answers = (ArrayList<String>) extras.get("pkgCorrectAnswers");
            titles = (ArrayList<String>) extras.get("pkgNext");
            Log.d("DEBUG","Started ChatActivity in Multiplayer mode, retrieved lesson content!");
        }

        setContentView(R.layout.activity_contact);
        listView = (ListView) findViewById(R.id.contact_view);
        Log.d("DEBUG", "Creat");
        listView.setAdapter(new ContactViewAdapter(this, chatApplication.getStores(), new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {

                    Intent intent = new Intent(ContactActivity.this, ChatActivity.class);

                    TextView displayName = (TextView) view.findViewById(R.id.hype_id);
                    CharSequence charSequence = displayName.getText();
                    TextView actualDisplayName = (TextView)view.findViewById(R.id.hype_name);   // get actual name textview
                    CharSequence charSequence2 = actualDisplayName.getText();                   // get string of it

                    setDisplayName(charSequence.toString());

                    Store store = chatApplication.getStores().get(getDisplayName());
                    store.setDelegate(contactActivity);
                    intent.putExtra("store", store.getInstance().getStringIdentifier());
                    intent.putExtra("name", charSequence2); // pass retrieved actual name as string extra to intent
                    if (mode.equals("multiplayer")) {
                        intent.putExtra("mode", "multiplayer"); // pass indicator that this is multiplayer!
                        intent.putExtra("pkgSubject", subject);
                        intent.putExtra("pkgTopic", topic);
                        intent.putExtra("pkgModule", adventure);
                        intent.putExtra("pkgLessons", concepts);
                        intent.putExtra("pkgQuestions", questions);
                        intent.putExtra("pkgAnswers", answers);
                        intent.putExtra("pkgCorrectAnswers", correct_answers);
                        intent.putExtra("pkgNext", titles);
                    } else {
                        intent.putExtra("mode", "chat"); // pass indicator that this is chat!
                    }

                    // check if multiplayer or chat

                    startActivity(intent);
                }

                return true;
            }
        }));

        // Gives access to MyApplication for notifying when instances are found
        setContactActivity(this);

        TextView announcementView = findViewById(R.id.hype_announcement);
        announcementView.setText("Device: " + MyApplication.announcement);

        // check for other learners
        String mode = getIntent().getStringExtra("mode");
        if (mode != null) {

            if (mode.equals("chat")) {
                // do nothing
                Log.d("DEBUG", "Chat mode!");
            } else if (mode.equals("multiplayer")) {
                // wait for players
                Log.d("DEBUG", "Multiplayer mode!");
            }

        }


    }

    @Override
    protected void onResume() {

        super.onResume();

        // Updates the UI on the press of a back button
        updateInterface();
    }

    protected void requestHypeToStart() {
        Log.i(TAG, "request");
// The application context is used to query the user for permissions, such as using
// the Bluetooth adapter or enabling Wi-Fi. The context must be set before anything
// else is attempted, otherwise resulting in an exception being thrown.
        Hype.setContext(getApplicationContext());

// Adding itself as an Hype state observer makes sure that the application gets
// notifications for lifecycle events being triggered by the Hype SDK. These
// events include starting and stopping, as well as some error handling.
        Hype.addStateObserver(this);

// Network observer notifications include other devices entering and leaving the
// network. When a device is found all observers get a onHypeInstanceFound
// notification, and when they leave onHypeInstanceLost is triggered instead.
// This observer also gets notifications for onHypeInstanceResolved when an
// instance is resolved.
        Hype.addNetworkObserver(this);

// Message notifications indicate when messages are received, sent, or delivered.
// Such callbacks are called with progress tracking indication.

// App identifiers are used to segregate the network. Apps with different identifiers
// do not communicate with each other, although they still cooperate on the network.
        Hype.setAppIdentifier("d183569a");

// Requesting Hype to start is equivalent to requesting the device to publish
// itself on the network and start browsing for other devices in proximity. If
// everything goes well, the onHypeStart() observer method gets called, indicating
// that the device is actively participating on the network.
        Hype.start();
    }

    private void configureHype() {
        Log.d("DEBUG", "Reached config");

        if (isConfigured)
            return;

        Hype.setContext(getApplicationContext());

        // Add this as an Hype observer
        Hype.addStateObserver(this);
        Hype.addNetworkObserver(this);



        // Generate an app identifier in the HypeLabs dashboard (https://hypelabs.io/apps/),
        // by creating a new app. Copy the given identifier here.
        Hype.setAppIdentifier("d183569a");

        // Set Hype announcement
        try {
            Hype.setAnnouncement(MyApplication.announcement.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Hype.setAnnouncement(null);
            e.printStackTrace();
        }


        // Update contacts interface
//        ContactActivity contactActivity = ContactActivity.getDefaultInstance();
//        contactActivity.requestPermissions(contactActivity);

        isConfigured = true;
    }

    @Override
    public void onHypeStart() {
        Log.i(TAG, "Hype started");
    }

    @Override
    public void onHypeStop(Error error) {

    }

    public void onHypeFailedStarting(Error error) {
        Log.i(TAG, String.format("Hype failed starting [%s]", error.toString()));
        // Obtain information of error
        final String failedMsg = error == null ? "" : String.format("Suggestion: %s\nDescription: %s\nReason: %s",
                error.getSuggestion(), error.getDescription(), error.getReason());

        // Prints an Error message to the application screen
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Hype failed starting");
                builder.setMessage(failedMsg);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }
        });
    }

    @Override
    public void onHypeReady() {
        Log.i(TAG, String.format("Hype is ready"));

        requestHypeToStart();
    }

    @Override
    public void onHypeStateChange() {
        Log.i(TAG, String.format("Hype changed state to [%d] (Idle=0, Starting=1, Running=2, Stopping=3)", Hype.getState().getValue()));
    }

    @Override
    public String onHypeRequestAccessToken(int userIdentifier) {
        return "98d32539e0a97050";
    }

    boolean shouldResolveInstance(Instance instance) {

// This method should decide whether an instance is interesting for communicating.
// For that purpose, the implementation could use instance.userIdentifier, but it's
// noticeable that announcements may not be available yet. Announcements are only
// exchanged during the handshake.
        return true;
    }

    @Override
    public void onHypeInstanceFound(Instance instance) {

        Log.i(TAG, String.format("Hype found instance: %s", instance.getStringIdentifier()));

        // Instances need to be resolved before being ready for communicating. This will
        // force the two of them to perform an handshake.
        if (shouldResolveInstance(instance)) {
            Hype.resolve(instance);
        }
    }

    @Override
    public void onHypeInstanceLost(Instance instance, Error error) {

        Log.i(TAG, String.format("Hype lost instance: %s [%s]", instance.getStringIdentifier(), error.toString()));

        // This instance is no longer available for communicating. If the instance
        // is somehow being tracked, such as by a map of instances, this would be
        // the proper time for cleanup.

    }

    @Override
    public void onHypeInstanceResolved(Instance instance) {

        Log.i(TAG, String.format("Hype resolved instance: %s", instance.getStringIdentifier()));
        addToResolvedInstancesMap(instance);
// At this point the instance is ready to communicate. Sending and receiving
// content is possible at any time now.
        playerFound = true;
    }

    public void addToResolvedInstancesMap(Instance instance) {

        // Instances should be strongly kept by some data structure. Their identifiers
        // are useful for keeping track of which instances are ready to communicate.
        Log.d("DEBUG","Heheh puutting store");
        chatApplication.getStores().put(instance.getStringIdentifier(), new Store(instance));
        notifyContactsChanged();
    }

    @Override
    public void onHypeInstanceFailResolving(Instance instance, Error error) {

    }



    public void setActivity(Activity activity) {
        this.activity = activity;
    }

//    public Map<String, Store> getStores() {
//
//        if (stores == null) {
//            stores = new HashMap<>();
//        }
//
//        return stores;
//    }

    @Override
    public void onMessageAdded(Store store, Message message) {

        updateInterface();
    }

    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(String displayName) {

        this.displayName = displayName;
    }

    public static ContactActivity getDefaultInstance() {

        return defaultInstance != null ? defaultInstance.get() : null;
    }

    private static void setContactActivity(ContactActivity instance) {

        defaultInstance = new WeakReference<>(instance);
    }

    protected void notifyContactsChanged() {

        updateInterface();
    }

    protected void notifyAddedMessage() {

        updateInterface();
    }

    protected void updateInterface() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = (ListView) findViewById(R.id.contact_view);
                updateHypeInstancesLabel(listView.getAdapter().getCount());

                ((ContactViewAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    private void updateHypeInstancesLabel(int nHypeInstances) {
        TextView hypeInstancesText = (TextView) findViewById(R.id.hype_instances_label);

        if (nHypeInstances == 0)
            hypeInstancesText.setText("No Hype Devices Found");
        else
            hypeInstancesText.setText("Hype Devices Found: " + nHypeInstances);
    }

    public void requestPermissions(Activity activity) {
        Log.d("DEBUG", "Perm");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("DEBUG", "If true");
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION_ID);
        } else {
            Log.d("DEBUG", "If flase");
            requestHypeToStart();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_COARSE_LOCATION_ID:
                requestHypeToStart();

                break;
        }
    }


}
