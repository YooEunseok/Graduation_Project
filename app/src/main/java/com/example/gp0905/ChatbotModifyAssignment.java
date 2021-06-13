package com.example.gp0905;

import com.google.firebase.database.DatabaseReference;

public class ChatbotModifyAssignment {
    public static String uid = ResultActivity.uid;
    public static String newScheduleKey;

    public static void finishAssignment(String key, DatabaseReference databaseRef){
        databaseRef.child(uid).child("Homework").child(key).removeValue();
    }
}
