package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ChatbotModifySchedule {

    public static String uid = ResultActivity.uid;
    public static String newScheduleKey;

    public static void modifySchedule(String scheduleKey,String title,String date1, String date2,
                                      String ed_date, String st_time, String ed_time,int flag
            ,DatabaseReference databaseRef) {

        //이동시킬 스케쥴키
        Log.d("potatoo", "scheduleKey:" + scheduleKey);
        Log.d("potatoo", "date1:" + date1);
        Log.d("potatoo", "date1:" + date2);


        //삭제할 일정 (원래 날짜)
        databaseRef.child(uid).child("Schedule").child(date1).child(scheduleKey).removeValue();

        //추가할 일정 (새로운 날짜)

        String location = "";
        newScheduleKey = databaseRef.child(uid).child("Schedule").child(date2).push().getKey();
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("title").setValue(title);
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("start_date").setValue(date2);
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("end_date").setValue(ed_date);
        databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("location").setValue(location);
        if(flag==1) {
            databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("start_time").setValue(st_time);
            databaseRef.child(uid).child("Schedule").child(date2).child(newScheduleKey).child("end_time").setValue(ed_time);
        }

    }

    public static void deleteSchedule(String scheduleKey,String date,DatabaseReference databaseRef) {

        Log.d("potato", "scheduleKey:" + scheduleKey);

        //삭제할 일정
        databaseRef.child(uid).child("Schedule").child(date).child(scheduleKey).removeValue();

    }

    public static void deleteDay(String date,DatabaseReference databaseRef) {

        //삭제할 일정
        databaseRef.child(uid).child("Schedule").child(date).removeValue();

    }

}