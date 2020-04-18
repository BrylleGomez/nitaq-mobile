package com.brylle.nitaq_mobapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.util.Log;

import com.hypelabs.hype.Error;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;
import com.hypelabs.hype.Message;
import com.hypelabs.hype.MessageInfo;
import com.hypelabs.hype.MessageObserver;
import com.hypelabs.hype.NetworkObserver;
import com.hypelabs.hype.StateObserver;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application implements StateObserver, NetworkObserver, MessageObserver {

    public static String announcement = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
//    private static final String TAG = MyApplication.class.getName();
    private static final String TAG = "DEBUG";
    private Map<String, Store> stores;
    private Activity activity;

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
        Hype.addMessageObserver(this);

// App identifiers are used to segregate the network. Apps with different identifiers
// do not communicate with each other, although they still cooperate on the network.
        Hype.setAppIdentifier("d183569a");

// Requesting Hype to start is equivalent to requesting the device to publish
// itself on the network and start browsing for other devices in proximity. If
// everything goes well, the onHypeStart() observer method gets called, indicating
// that the device is actively participating on the network.
        Hype.start();
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
        final String failedMsg = error == null? "" : String.format("Suggestion: %s\nDescription: %s\nReason: %s",
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

    }

    @Override
    public void onHypeStateChange() {

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

// At this point the instance is ready to communicate. Sending and receiving
// content is possible at any time now.
    }

    @Override
    public void onHypeInstanceFailResolving(Instance instance, Error error) {

    }

    @Override
    public void onHypeMessageReceived(Message message, Instance instance) {

        String text = null;
        try {
            text = new String(message.getData(), "UTF-8");

            // If all goes well, this will log the original text
            Log.i(TAG, String.format("Hype received a message from: %s %s", instance.getStringIdentifier(), text));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onHypeMessageFailedSending(MessageInfo messageInfo, Instance instance, Error error) {

    }

    @Override
    public void onHypeMessageSent(MessageInfo messageInfo, Instance instance, float v, boolean b) {

    }

    @Override
    public void onHypeMessageDelivered(MessageInfo messageInfo, Instance instance, float v, boolean b) {

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Map<String, Store> getStores() {

        if (stores == null) {
            stores = new HashMap<>();
        }

        return stores;
    }

}