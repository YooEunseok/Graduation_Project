package com.example.gp0905;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifySchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_modify_schedule);

        final FirebaseDatabase database;
        final DatabaseReference myRef;

        final EditText et_title = (EditText)findViewById (R.id.m_title);
        final EditText et_start = (EditText)findViewById (R.id.m_start_date);
        final EditText et_end = (EditText)findViewById (R.id.m_end_date);
        final EditText et_loc = (EditText)findViewById (R.id.m_location);
        final EditText et_start_time = (EditText)findViewById (R.id.m_et_st_time);
        final EditText et_end_time = (EditText)findViewById (R.id.m_et_end_time);

        Button btn_back = (Button)findViewById (R.id.m_back_bt);
        Button btn_ok = (Button)findViewById (R.id.m_ok_bt);
        Button btn_delete = (Button)findViewById (R.id.m_delete_bt);

        final String[] db_title = new String[1];
        final String[] db_end_date = new String[1];
        final String[] db_start_date = new String[1];
        final String[] db_location = new String[1];
        final String[] db_end_time = new String[1];
        final String[] db_start_time = new String[1];

        final Intent intent = getIntent ();
        final String start_date = intent.getExtras ().getString ("st_date");
        final String title = intent.getExtras ().getString ("title");


        database = FirebaseDatabase.getInstance ();
        myRef = database.getReference ("User_list");

        myRef.child (ResultActivity.uid).child ("Schedule").child (start_date).addValueEventListener (new ValueEventListener () {
            //myRef.child("title").addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                    String obj = objSnapshot.getKey ();
                    //Toast.makeText (getApplicationContext (), obj, Toast.LENGTH_SHORT).show ();
                    //sKey[count] = obj;
                    String t = objSnapshot.child ("title").getValue ().toString ();
                    //Toast.makeText (getApplicationContext (), t, Toast.LENGTH_SHORT).show ();
                    if ( t.equals (title)) {
                        db_title[0] = objSnapshot.child ("title").getValue (String.class);
                        db_end_date[0] = objSnapshot.child ("end_date").getValue (String.class);
                        db_start_date[0] = objSnapshot.child ("start_date").getValue (String.class);
                        db_location[0] = objSnapshot.child ("location").getValue (String.class);
                        db_end_time[0] = objSnapshot.child ("end_time").getValue (String.class);
                        db_start_time[0] = objSnapshot.child ("start_time").getValue (String.class);
                    }
                    //Toast.makeText (getApplicationContext (), title + end_date + start_date + location, Toast.LENGTH_SHORT).show ();


                    et_title.setText (db_title[0]);
                    et_start.setText (db_start_date[0]);
                    et_end.setText (db_end_date[0]);
                    et_loc.setText (db_location[0]);
                    et_start_time.setText (db_start_time[0]);
                    et_end_time.setText (db_end_time[0]);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                finish ();
            }
        });

        btn_ok.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                final String title_ok = et_title.getText().toString();
                final String st_date_ok = et_start.getText ().toString ();
                final String end_date_ok = et_end.getText ().toString ();
                final String location_ok = et_loc.getText ().toString ();
                final String st_time_ok = et_start_time.getText ().toString ();
                final String end_time_ok = et_end_time.getText ().toString ();
                Toast.makeText (getApplicationContext (), title_ok + st_date_ok + end_date_ok + location_ok, Toast.LENGTH_SHORT).show ();


                myRef.child (ResultActivity.uid).child ("Schedule").child (start_date).addValueEventListener (new ValueEventListener () {
                    //myRef.child("title").addValueEventListener (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                            String obj = objSnapshot.getKey ();

                            String t = objSnapshot.child ("title").getValue ().toString ();
                            Toast.makeText (getApplicationContext (), t, Toast.LENGTH_SHORT).show ();
                            if ( t.equals (title)) {
                                myRef.child (ResultActivity.uid).child ("Schedule").child (start_date).child (obj).removeValue ();
                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("title").setValue (title_ok);
                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("start_date").setValue (st_date_ok);
                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("end_date").setValue (end_date_ok);
                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("location").setValue (location_ok);
                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("start_time").setValue (st_time_ok);
                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("end_time").setValue (end_time_ok);
                                //Toast.makeText (getApplicationContext (), st_time_ok, Toast.LENGTH_SHORT).show ();
//                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("start_time").setValue (st_time_ok);
//                                myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("end_time").setValue (end_time_ok);
//                                if(st_time_ok.isEmpty () || end_time_ok.isEmpty ()) {
//
//
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("title").setValue (title_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("start_date").setValue (st_date_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("end_date").setValue (end_date_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("location").setValue (location_ok);
////                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("start_time").removeValue ();
////                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("end_time").removeValue ();
//                                    finish ();
//                                    break;
//
//                                } else {
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (start_date).child (obj).removeValue ();
//
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("title").setValue (title_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("start_date").setValue (st_date_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("end_date").setValue (end_date_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("location").setValue (location_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("start_time").setValue (st_time_ok);
//                                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date_ok).child (obj).child ("end_time").setValue (end_time_ok);
//                                    finish ();
//                                    break;
//                                }
                                break;
                                //finish ();
                                //break;
                            }
//                            finish ();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish ();
            }
        });

        btn_delete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                myRef.child (ResultActivity.uid).child ("Schedule").child (start_date).addValueEventListener (new ValueEventListener () {
                    //myRef.child("title").addValueEventListener (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                            String obj = objSnapshot.getKey ();

                            String t = objSnapshot.child ("title").getValue ().toString ();
                            //Toast.makeText (getApplicationContext (), t, Toast.LENGTH_SHORT).show ();
                            if ( t.equals (title)) {
                                myRef.child (ResultActivity.uid).child ("Schedule").child (start_date).child (obj).removeValue ();
                                finish ();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}