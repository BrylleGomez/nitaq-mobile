package com.brylle.nitaq_mobapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
//    Firebase reference1, reference2;

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
