package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class scheduleSearch2 {

    private static int count;

    public static void scheduleSearch(FirebaseDatabase mDatabase, DatabaseReference databaseRef
            , String date) {

        final String[] sKey = new String[100]; // Schedule Key


        try {
            databaseRef.child(ResultActivity.uid).child("Schedule").child(date).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    count = 0;

                    for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                        String obj = objSnapshot.getKey();
                        sKey[count] = obj;

                        databaseRef.child(ResultActivity.uid).child("Schedule").child(date).child(obj).removeValue();

                        count++;
                    }
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

