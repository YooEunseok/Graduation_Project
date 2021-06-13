package com.example.gp0905;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Listview extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List fileList = new ArrayList<>();
    static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_listview);
        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }

        listView= (ListView)  findViewById(R.id.lv_fileList);

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listitem, fileList);
        listView.setAdapter(adapter);

        //adapter.add ("title");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference();

         //Read from the database

        databaseRef.addChildEventListener (new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String mykey = snapshot.getKey ();
//                Toast.makeText (getApplicationContext (), mykey, Toast.LENGTH_SHORT).show ();
//                String end_date = (String) snapshot.child ("Schedule").child ("2020 9 19").child (EnterSchedule.newScheduleKey).child ("title").getValue ();
                String end_date = (String) snapshot.child ("Schedule").child ("2020 9 19").child (EnterSchedule.newScheduleKey).child ("title").getValue ();
                Toast.makeText (getApplicationContext (), end_date, Toast.LENGTH_SHORT).show ();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}