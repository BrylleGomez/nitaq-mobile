package com.brylle.nitaq_mobapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.hypelabs.hype.Error;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;
import com.hypelabs.hype.Message;
import com.hypelabs.hype.MessageInfo;
import com.hypelabs.hype.MessageObserver;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements MessageObserver {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    final String introMessage = "Welcome!";

    // lesson elements (only used in multiplayer mode, ignored in chat mode)
    private String mode;
    private String subject;
    private String topic;
    private String adventure;
    private ArrayList<String> concepts;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private ArrayList<String> correct_answers;
    private ArrayList<String> titles;

//    Firebase reference1, reference2;

    public static String INTENT_EXTRA_STORE = "com.hypelabs.store";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final Store store = getStore();
        Hype.addMessageObserver(this);
        Hype.addMessageObserver(this);

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
            Log.d("DEBUG","Subject: " + subject);
            Log.d("DEBUG","Topic: " + topic);
            Log.d("DEBUG","Adventure: " + adventure);
            Log.d("DEBUG","Concepts: " + concepts);
            Log.d("DEBUG","Questions: " + questions);
            Log.d("DEBUG","Answers: " + answers);
            Log.d("DEBUG","Correct_answers: " + correct_answers);
            Log.d("DEBUG","Titles: " + titles);

        }

        Log.d("DEBUG",store.toString());
        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        // Hardcoded bot message
        generateBotMessage(introMessage);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                scrollView.scrollTo(0, scrollView.getBottom());
                if(!messageText.equals("")){
                    try {
                        sendMessage(messageText, store.getInstance(),true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    addMessageBox(messageText, 2, "Me");
                    messageArea.setText("");

                    // BotResponse with delay
                    new CountDownTimer(2000, 1000) {
                        public void onFinish() {
                            // bot response
                            generateBotMessage("Stop talking!");
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();

                }
            }
        });
    }

    public void addMessageBox(final String message, final int type, final String sender){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
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
                    textView1.setText(sender);
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
                scrollBottom();
            }
        });

    }

    // BOT MESSAGE
    private void generateBotMessage(String message) {

        final Store store = getStore();
        if(!message.equals("")) {
            addMessageBox(message, 3, "Me");
            try {
                message = "~" + message; // append ~ to signify bot message
                if (message.equals("~" + introMessage)) {  // if welcome message, signify other players that you're joining their room (if exists)
                    // do so by sending ~<n>
                    message = "~<n>";
                }
                sendMessage(message, store.getInstance(), true);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            messageArea.setText("");
        }

        scrollBottom();

    }

    private void scrollBottom() {
        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.scrollTo(0, scrollView.getBottom());
    }

    protected Message sendMessage(String text, Instance instance, boolean acknowledge) throws UnsupportedEncodingException {

        // When sending content there must be some sort of protocol that both parties
        // understand. In this case, we simply send the text encoded in UTF-8. The data
        // must be decoded when received, using the same encoding.
        byte[] data = text.getBytes("UTF-8");

        return Hype.send(data, instance, acknowledge);
    }

    @Override
    public void onHypeMessageReceived(Message message, Instance instance) {

        String text = null;
        try {
            text = new String(message.getData(), "UTF-8");

            // Bonus display name
            String contactName = instance.getStringIdentifier();
            String displayName = "";
            boolean isBot = false;
            if (text.charAt(0) == '~') {                                // Check if it's from bot (from other device)
                text = text.substring(1);                               // remove the tilda (just for messaging protocol purposes)

                // check if special message from bot
                if (text.equals("<n>")) {                        // <n> means new friend has joined
                    text = getNameFromID(contactName) + " has joined your room!";
                }

                isBot = true;
            } else {                                                    // from another device!
                displayName = getNameFromID(contactName);
            }

            // Different appearance for bot and non-bot
            if (isBot) {
                addMessageBox(text, 3, "Me");
            } else {
                addMessageBox(text, 1, displayName);
            }


            // If all goes well, this will log the original text
            Log.i("DEBUG", String.format("Hype received a message from: %s %s", instance.getStringIdentifier(), text));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private String getNameFromID(String id) {
        String name;
        if (id.equals("D183569AE0483287")) {      // Samsung J7
            name = "Sara";
        } else if (id.equals("D183569ADA6F6084")) {      // Samsung S6 Edge
            name = "Marco";
        } else if (id.equals("D183569AC61F6749")) {      // Samsung S6 Edge
            name = "Aiva";
        } else {    // unrecognized device
            name = "Unknown Device";
        }
        return name;
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
    protected Store getStore() {

        MyApplication chatApplication = (MyApplication)getApplication();
        String storeIdentifier = getIntent().getStringExtra("store");
        Log.d("DEBUG","Identifierrr  " + storeIdentifier);
        Store store = chatApplication.getStores().get(storeIdentifier);

        return store;
    }
}
