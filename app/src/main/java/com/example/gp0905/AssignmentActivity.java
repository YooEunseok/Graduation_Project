package com.example.gp0905;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AssignmentActivity extends AppCompatActivity {

    ////완료 표시하는거 만들기기

   private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;

    String uid=ResultActivity.uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        database = FirebaseDatabase.getInstance ();
        databaseRef = database.getReference ("User_list");

        databaseRef.child (uid).child ("Homework").addValueEventListener (new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                    String obj = objSnapshot.getKey ();

                    String name = objSnapshot.child ("name").getValue (String.class);
                    String subject = objSnapshot.child ("subject").getValue (String.class);
                    String due_date = objSnapshot.child ("due_date").getValue (String.class);
                    String due_time = objSnapshot.child ("due_time").getValue (String.class);


                    fileList.add("\n[ "+subject+" ]"+"\n"+name+"  \n("+due_date+"   "+due_time+")\n");
                }
                adapter.notifyDataSetChanged ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView = (ListView) findViewById (R.id.assignment_list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList);
        listView.setAdapter (adapter);
    }
}
