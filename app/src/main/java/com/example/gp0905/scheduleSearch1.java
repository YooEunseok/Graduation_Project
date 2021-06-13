package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class scheduleSearch1 {

    private static int count;

    public static void scheduleSearch(FirebaseDatabase mDatabase, DatabaseReference databaseRef
            , String date1,String date2) {

        final String[] sKey = new String[100]; // Schedule Key
        final int[] flag = {0};

        try {
            flag[0]=0;
            databaseRef.child(ResultActivity.uid).child("Schedule").child(date1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    count = 0;
                    Log.d("mmmm","date: "+date1+" -> "+date2);

                    for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                        String obj = objSnapshot.getKey();
                        sKey[count] = obj;
                        Log.d("mmmm","obj: "+obj);

                        if(flag[0]==0){
                            String title = objSnapshot.child("title").getValue(String.class);
                            String st_date = objSnapshot.child("start_date").getValue(String.class);
                            String ed_date = " ";
                            if (objSnapshot.child("end_date").getValue(String.class) != null)
                                ed_date = objSnapshot.child("end_date").getValue(String.class);
                            String st_time = " ";
                            String ed_time = " ";
                            int flag = 0;//시간할당일정인지아닌ㄴ지

                            if (objSnapshot.child("start_time").getValue(String.class) != null && objSnapshot.child("end_time").getValue(String.class) != null) {
                                st_time = objSnapshot.child("start_time").getValue(String.class);
                                Log.d("scheduleSearch", "start time: " + st_time);
                                ed_time = objSnapshot.child("end_time").getValue(String.class);
                                Log.d("scheduleSearch", "end time: " + ed_time);
                                flag = 1;
                            }
                            Log.d("mmmm","title: "+title);
                            Log.d("mmmm","st_date: "+st_date);

                            databaseRef.child(ResultActivity.uid).child("Schedule").child(date1).child(obj).removeValue();

                            String newScheduleKey =databaseRef.child(ResultActivity.uid).child ("Schedule").child(date2).push ().getKey ();
                            databaseRef.child(ResultActivity.uid).child ("Schedule").child(date2).child (newScheduleKey).child("title").setValue(title);
                            databaseRef.child(ResultActivity.uid).child ("Schedule").child(date2).child (newScheduleKey).child("start_date").setValue(st_date);
                            databaseRef.child(ResultActivity.uid).child ("Schedule").child(date2).child (newScheduleKey).child("end_date").setValue(ed_date);
                            if(flag==1) {
                                databaseRef.child(ResultActivity.uid).child("Schedule").child(date2).child(newScheduleKey).child("start_time").setValue(st_time);
                                databaseRef.child(ResultActivity.uid).child("Schedule").child(date2).child(newScheduleKey).child("end_time").setValue(ed_time);
                            }
                        }

                        count++;
                    }
                    flag[0] =1;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
            Log.d("scheduleSearch", "Date doesn't exist.");
        }
    }
}

