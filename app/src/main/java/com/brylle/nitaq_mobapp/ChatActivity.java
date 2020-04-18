package com.brylle.nitaq_mobapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;
import com.hypelabs.hype.Message;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements Store.Delegate {

    public static String INTENT_EXTRA_STORE = "com.hypelabs.store";

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
//    Firebase reference1, reference2;

    // HYPELABSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS

    protected Message sendMessage(String text, Instance instance) throws UnsupportedEncodingException {

        // When sending content there must be some sort of protocol that both parties
        // understand. In this case, we simply send the text encoded in UTF-8. The data
        // must be decoded when received, using the same encoding.
        byte[] data = text.getBytes("UTF-8");

        // Sends the data and returns the message that has been generated for it. Messages have
        // identifiers that are useful for keeping track of the message's deliverability state.
        // In order to track message delivery set the last parameter to true. Notice that this
        // is not recommended, as it incurs extra overhead on the network. Use this feature only
        // if progress tracking is really necessary.
        return Hype.send(data, instance, false);
    }

    @Override
    protected void onStart() {

        super.onStart();

        setContentView(R.layout.activity_chat);

//        final ListView listView = (ListView) findViewById(R.id.list_view);
//        final Button sendButton = (Button) findViewById(R.id.send_button);
//        final EditText messageField = (EditText) findViewById(R.id.message_field);

        final Store store = getStore();

//        listView.setAdapter(new ChatViewAdapter(this, store));

//        TextView announcement = findViewById(R.id.chatViewInstanceAnnouncement);
//        try {
//            announcement.setText(new String(store.getInstance().getAnnouncement(), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        // Start listening to message queue events so we can update the UI accordingly
        store.setDelegate(this);

        // Flag all messages as read
        store.setLastReadIndex(store.getMessages().size());

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String text = messageArea.getText().toString();

                // Avoids accidental presses
                if (text == null || text.length() == 0) {
                    return;
                }

                try {
                    Log.v(getClass().getSimpleName(), "send message");
                    Message message = sendMessage(text, store.getInstance());

                    if (message != null) {

                        // Clear message content
                        messageArea.setText("");

                        // Add the message to the store so it shows in the UI
                        store.add(message);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        if(getStore() != null){
            getStore().setLastReadIndex(getStore().getMessages().size());
        }
    }

    @Override
    public void onMessageAdded(Store store, Message message) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update chat bubbles
                final ListView listView = (ListView) findViewById(R.id.list_view);

                ((ChatViewAdapter)listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    protected Store getStore() {

        ChatApplication chatApplication = (ChatApplication)getApplication();
        String storeIdentifier = getIntent().getStringExtra(INTENT_EXTRA_STORE);
        Store store = chatApplication.getStores().get(storeIdentifier);

        return store;
    }

    // HYPELABSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                scrollView.scrollTo(0, scrollView.getBottom());
                if(!messageText.equals("")){
                    addMessageBox(messageText, 2);
                    messageArea.setText("");

                    new CountDownTimer(2000, 1000) {
                        public void onFinish() {
                            addMessageBox("I'm a bot!", 3);

                            new CountDownTimer(2000, 1000) {
                                public void onFinish() {
                                    addMessageBox("I'm another player!", 1);
                                }

                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();
                }
            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(3,3,3,3);
        lp2.weight = 7.0f;
        textView.setPadding(25,15,25,25);
        textView.setTextSize(20);

        // type 1 for other players, type2 for your own message, type 3 for GM
        if(type == 1) {
            TextView textView1 = new TextView(ChatActivity.this);
            textView1.setText("Other Player");
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
            textView.setTextColor(Color.WHITE);
            textView1.setTextColor(Color.GRAY);
            textView1.setLayoutParams(lp2);
            layout.addView(textView1);
        }
        else if (type==2){
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
            textView.setTextColor(Color.WHITE);
        }
        else{
            TextView textView1 = new TextView(ChatActivity.this);
            textView1.setText("Game Master");
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_gm);
            textView.setTextColor(Color.WHITE);
            textView1.setTextColor(Color.GRAY);
            textView1.setLayoutParams(lp2);
            layout.addView(textView1);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.scrollTo(0, scrollView.getBottom());
    }
}
