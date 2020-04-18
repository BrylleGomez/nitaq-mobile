package com.brylle.nitaq_mobapp;

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


public class HypeApp extends Application implements StateObserver, NetworkObserver, MessageObserver {

    private static final String TAG = HypeApp.class.getName();

    protected void requestHypeToStart() {

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
        Hype.setAppIdentifier("a80e7705");

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

    @Override
    public void onHypeFailedStarting(Error error) {
        Log.i(TAG, String.format("Hype failed starting [%s]", error.toString()));
    }

    @Override
    public void onHypeReady() {

    }

    @Override
    public void onHypeStateChange() {

    }

    @Override
    public String onHypeRequestAccessToken(int userIdentifier) {
        return "a518f3fffa21e803";
    }

    @Override
    public void onHypeMessageReceived(Message message, Instance instance) {

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

    @Override
    public void onHypeInstanceFound(Instance instance) {

    }

    @Override
    public void onHypeInstanceLost(Instance instance, Error error) {

    }

    @Override
    public void onHypeInstanceResolved(Instance instance) {

    }

    @Override
    public void onHypeInstanceFailResolving(Instance instance, Error error) {

    }
}