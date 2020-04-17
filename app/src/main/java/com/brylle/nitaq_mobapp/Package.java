package com.brylle.nitaq_mobapp;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Package {

    // class to represent event entries in the events page of the app
    // the following private members are the same as the fields of an event entry in a database
    private String subject;
    private String topic;
    private String module;
    private ArrayList<String> lessons;
    private ArrayList<String> questions;
    private ArrayList<String> next;

    // constructor
    public Package(
            String subject,
            String topic,
            String module,
            ArrayList<String> lessons,
            ArrayList<String> questions,
            ArrayList<String> next) {
        this.subject = subject;
        this.topic = topic;
        this.module = module;
        this.lessons = lessons;
        this.questions = questions;
        this.next = next;
    }

    // prints a log output of the event object
    public void print() {
        Log.d("Debug", "Package of {" + this.subject + ", " + this.topic + ", " + this.module + "} added!");
    }

    public String getSubject() {
        return subject;
    }

    public String getTopic() {
        return topic;
    }

    public String getModule() {
        return module;
    }

    public ArrayList<String> getLessons() {
        return lessons;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public ArrayList<String> getNext() {
        return next;
    }

}

