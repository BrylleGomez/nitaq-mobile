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

import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;
import com.hypelabs.hype.Message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivitySingle extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    private String subject;
    private String topic;
    private String adventure;
    private ArrayList<String> concepts;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private ArrayList<String> correct_answers;
    private ArrayList<String> titles;

    public static String INTENT_EXTRA_STORE = "com.hypelabs.store";
    private int counter = 1;

    private void scrollDown() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void processInput1(final String messageText, final String answer) {
        scrollDown();
        new CountDownTimer(1000, 1000) {
            public void onFinish() {

                if (!messageText.equals(answer)){
                    addMessageBox("Try again...", 3, "Me");
                    addMessageBox(questions.get(0), 3, "Me");
                    String[] arrOfStr = answers.get(0).split(",", 10);
                    String answersTemp = "";
                    answersTemp += "a) " + arrOfStr[0] + "\n";
                    answersTemp += "b) " + arrOfStr[1] + "\n";
                    answersTemp += "c) " + arrOfStr[2] + "\n";
                    answersTemp += "d) " + arrOfStr[3] + "\n";
                    addMessageBox(answersTemp, 3, "Me");
                    scrollDown();
                } else {
                    addMessageBox("Congratulations! Your answer is correct!", 3, "Me");
                    counter++;
                    // ask second question
                    addMessageBox("Clue 2: " + titles.get(0), 3, "Me");
                    addMessageBox(concepts.get(1), 3, "Me");
                    addMessageBox("Second question is...", 3, "Me");
                    addMessageBox(questions.get(1), 3, "Me");
                    String[] arrOfStr = answers.get(1).split(",", 10);
                    String answersTemp = "";
                    answersTemp += "a) " + arrOfStr[0] + "\n";
                    answersTemp += "b) " + arrOfStr[1] + "\n";
                    answersTemp += "c) " + arrOfStr[2] + "\n";
                    answersTemp += "d) " + arrOfStr[3] + "\n";
                    addMessageBox(answersTemp, 3, "Me");
                    scrollDown();
                }

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

    }

    private void processInput2(final String messageText, final String answer) {
        scrollDown();
        new CountDownTimer(1000, 1000) {
            public void onFinish() {
                // reply

                if (!messageText.equals(answer)){
                    addMessageBox("Try again...", 3, "Me");
                    addMessageBox(questions.get(1), 3, "Me");
                    String[] arrOfStr = answers.get(1).split(",", 10);
                    String answersTemp = "";
                    answersTemp += "a) " + arrOfStr[0] + "\n";
                    answersTemp += "b) " + arrOfStr[1] + "\n";
                    answersTemp += "c) " + arrOfStr[2] + "\n";
                    answersTemp += "d) " + arrOfStr[3] + "\n";
                    addMessageBox(answersTemp, 3, "Me");
                    scrollDown();
                } else {
                    addMessageBox("Congratulations! Your answer is correct! YOU FINISHED!",3, "Me");
                    scrollDown();
                    counter++;
                }

//                // ask second question
//                addMessageBox(concepts.get(1), 3, "Me");
//                addMessageBox("First question is...", 3, "Me");
//                addMessageBox(questions.get(1), 3, "Me");

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

    }

    private void processInput3(String messageText, String answer) {

        new CountDownTimer(1000, 1000) {
            public void onFinish() {
                // reply

            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        // Retrieve details from intent
        Bundle extras = getIntent().getExtras();
        subject = extras.getString("pkgSubject");
        topic = extras.getString("pkgTopic");
        adventure = extras.getString("pkgModule");
        concepts = (ArrayList<String>) extras.get("pkgLessons");
        questions = (ArrayList<String>) extras.get("pkgQuestions");
        answers = (ArrayList<String>) extras.get("pkgAnswers");
        correct_answers = (ArrayList<String>) extras.get("pkgCorrectAnswers");
        titles = (ArrayList<String>) extras.get("pkgNext");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                scrollView.scrollTo(0, scrollView.getBottom());
                if(!messageText.equals("")){
                    addMessageBox(messageText, 2, "Me");
                    if (counter == 1) {
                        processInput1(messageText, correct_answers.get(0));
                    } else if (counter == 2) {
                        processInput2(messageText, correct_answers.get(1));
                    } else if (counter == 3) {
                        processInput3(messageText, correct_answers.get(2));
                    }
                    messageArea.setText("");
                }
            }
        });

        Log.d("DEBUG", subject);
        Log.d("DEBUG", topic);
        Log.d("DEBUG", adventure);
        Log.d("DEBUG", String.valueOf(concepts));
        Log.d("DEBUG", String.valueOf(questions));
        Log.d("DEBUG", String.valueOf(answers));
        Log.d("DEBUG", String.valueOf(correct_answers));
        Log.d("DEBUG", String.valueOf(titles));

        addMessageBox("Welcome to "+subject+" module, "+adventure+"\n", 3, "Me");
        addMessageBox("You open your eyes. You look around, only to realize that this room is not your own. You are trapped in a strange, cold, gloomy room. Who could be behind this?\n", 3, "Me");
        addMessageBox("You are here on your own. You must find a way out.\n", 3, "Me");
        addMessageBox("Several clues lie around to help you get out. The first one is...\n", 3, "Me");
        addMessageBox("Clue 1: " + titles.get(0), 3, "Me");
        addMessageBox(concepts.get(0), 3, "Me");
        addMessageBox("First question is...", 3, "Me");
        addMessageBox(questions.get(0), 3, "Me");
        String[] arrOfStr = answers.get(0).split(",", 10);
        String answersTemp = "";
        answersTemp += "a) " + arrOfStr[0] + "\n";
        answersTemp += "b) " + arrOfStr[1] + "\n";
        answersTemp += "c) " + arrOfStr[2] + "\n";
        answersTemp += "d) " + arrOfStr[3] + "\n";
        addMessageBox(answersTemp, 3, "Me");

    }

    public void addMessageBox(final String message, final int type, final String sender){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = new TextView(ChatActivitySingle.this);
                textView.setText(message);

                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp2.setMargins(3,3,3,3);
                lp2.weight = 7.0f;
                textView.setPadding(25,15,25,25);
                textView.setTextSize(20);

                // type 1 for other players, type2 for your own message, type 3 for GM
                if(type == 1) {
                    TextView textView1 = new TextView(ChatActivitySingle.this);
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
                    TextView textView1 = new TextView(ChatActivitySingle.this);
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

}