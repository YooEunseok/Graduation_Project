package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ChatbotModifySchedule2 {

    public static String uid = ResultActivity.uid;
    public static String newScheduleKey;

    public static void modifySchedule(String scheduleKey,String title,String date1, String date2
           , String ed_date, String st_time, String ed_time,DatabaseReference databaseRef,String[] st) {

        //이동시킬 스케쥴키
        Log.d("potato", "scheduleKey:" + scheduleKey);
        String st_time_=st[0]+":"+st[1];
        //삭제할 일정 (원래 날짜)
        databaseRef.child(uid).child("Schedule").child(date1).child(scheduleKey).removeValue();

        //추가할 일정 (새로운 날짜)
        newScheduleKey = databaseRef.child(uid).child("Schedule").child(date2).push().getKey();
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("title").setValue(title);
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("start_date").setValue(date2);
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("end_date").setValue(ed_date);
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("start_time").setValue(st_time_);
        //databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("end_time").setValue(ed_time);
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("end_time").setValue("18:00");

    }
}