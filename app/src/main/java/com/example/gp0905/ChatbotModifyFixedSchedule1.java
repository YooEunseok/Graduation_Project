package com.example.gp0905;

import com.google.firebase.database.DatabaseReference;

public class ChatbotModifyFixedSchedule1 {
    public static String uid = ResultActivity.uid;
    public static String newScheduleKey;

    public static void modifyFixedSchedule(String key, DatabaseReference databaseRef, String date, String title,
                                           String st_time, String ed_time) {

        newScheduleKey = databaseRef.child(ResultActivity.uid).child("Schedule").child(date).push().getKey();
        //databaseRef.child(ResultActivity.uid).child("Schedule").child(date).child(newScheduleKey).child("fixed_modify").setValue(key);
        databaseRef.child(ResultActivity.uid).child("Schedule").child(date).child(newScheduleKey).child("title").setValue(title);
        databaseRef.child(ResultActivity.uid).child("Schedule").child(date).child(newScheduleKey).child("start_time").setValue(st_time);
        databaseRef.child(ResultActivity.uid).child("Schedule").child(date).child(newScheduleKey).child("end_time").setValue(ed_time);
        databaseRef.child(ResultActivity.uid).child("Schedule").child(date).child(newScheduleKey).child("start_date").setValue(date);
        databaseRef.child(ResultActivity.uid).child("Schedule").child(date).child(newScheduleKey).child("end_date").setValue(date);


    }
}
