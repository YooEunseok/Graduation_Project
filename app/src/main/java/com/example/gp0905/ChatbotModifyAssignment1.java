package com.example.gp0905;

import com.google.firebase.database.DatabaseReference;

public class ChatbotModifyAssignment1 {
    public static String uid = ResultActivity.uid;
    public static String newScheduleKey;

    public static void modifyAssignment(String key, String date,DatabaseReference databaseRef){
        databaseRef.child(uid).child("Homework").child(key).child("due_date").setValue(date);
    }
}
