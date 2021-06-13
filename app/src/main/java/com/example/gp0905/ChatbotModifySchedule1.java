package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ChatbotModifySchedule1 {

    public static String uid = ResultActivity.uid;
    public static String newScheduleKey;

    public static void modifySchedule(String scheduleKey,String date1,String time
            ,DatabaseReference databaseRef,String[] st) {

        Log.d("ChatbotModifySchedule1", "scheduleKey:" + scheduleKey);
        Log.d("ChatbotModifySchedule1", "time:" + time);

        String st_time=st[0]+":"+st[1];
        databaseRef.child(uid).child("Schedule").child(date1).child(scheduleKey).child("start_time").setValue(st_time);
    }

}