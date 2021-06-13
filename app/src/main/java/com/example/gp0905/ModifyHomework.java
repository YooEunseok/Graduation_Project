package com.example.gp0905;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyHomework extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_modify_homework);

        final FirebaseDatabase database;
        final DatabaseReference myRef;

        final Intent intent = getIntent ();
        final String date = intent.getExtras ().getString ("date");
        final String name = intent.getExtras ().getString ("name");

        int i = name.indexOf ("과제명: ");
        final String n_title = name.substring (i+5);

        Toast.makeText (getApplicationContext (), n_title, Toast.LENGTH_SHORT).show ();
        final EditText et_name = (EditText)findViewById (R.id.m_et_name);
        final EditText et_subject = (EditText)findViewById (R.id.m_et_subject);
        final EditText et_date = (EditText)findViewById (R.id.m_et_dueDate);

        Button btn_back = (Button)findViewById (R.id.m_btn_back);
        Button btn_ok = (Button)findViewById (R.id.m_btn_ok);
        Button btn_delete = (Button)findViewById (R.id.m_btn_delete);

        final String[] db_name = new String[1];
        final String[] db_subject = new String[1];
        final String[] db_date = new String[1];


        database = FirebaseDatabase.getInstance ();
        myRef = database.getReference ("User_list");

        myRef.child (ResultActivity.uid).child ("Homework").child (date).addValueEventListener (new ValueEventListener() {
            //myRef.child("title").addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                    String obj = objSnapshot.getKey ();
                    //Toast.makeText (getApplicationContext (), obj, Toast.LENGTH_SHORT).show ();
                    //sKey[count] = obj;
                    String n = objSnapshot.child ("name").getValue ().toString ();
                    Toast.makeText (getApplicationContext (), n, Toast.LENGTH_SHORT).show ();
                    if ( n_title.equals (n)) {
                        Toast.makeText (getApplicationContext (), "n", Toast.LENGTH_SHORT).show ();

                        db_name[0] = objSnapshot.child ("name").getValue (String.class);
                        db_subject[0] = objSnapshot.child ("subject").getValue (String.class);
                        db_date[0] = objSnapshot.child ("due_date").getValue (String.class);
                    }
                    //Toast.makeText (getApplicationContext (), title + end_date + start_date + location, Toast.LENGTH_SHORT).show ();


                    et_name.setText (db_name[0]);
                    et_date.setText (db_date[0]);
                    et_subject.setText (db_subject[0]);

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
                final String name_ok = et_name.getText().toString();
                final String date_ok = et_date.getText ().toString ();
                final String subject_ok = et_subject.getText ().toString ();

                myRef.child (ResultActivity.uid).child ("Homework").child (date).addValueEventListener (new ValueEventListener() {
                    //myRef.child("title").addValueEventListener (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                            String obj = objSnapshot.getKey ();

                            String n = objSnapshot.child ("name").getValue ().toString ();
                            //Toast.makeText (getApplicationContext (), t, Toast.LENGTH_SHORT).show ();
                            if ( n.equals (n_title)) {
                                myRef.child (ResultActivity.uid).child ("Homework").child (date).child (obj).removeValue ();
                                myRef.child (ResultActivity.uid).child ("Homework").child (date_ok).child (obj).child ("name").setValue (name_ok);
                                myRef.child (ResultActivity.uid).child ("Homework").child (date_ok).child (obj).child ("subject").setValue (subject_ok);
                                myRef.child (ResultActivity.uid).child ("Homework").child (date_ok).child (obj).child ("due_date").setValue (date_ok);
                                break;
                            }
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
                myRef.child (ResultActivity.uid).child ("Homework").child (date).addValueEventListener (new ValueEventListener() {
                    //myRef.child("title").addValueEventListener (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                            String obj = objSnapshot.getKey ();

                            String n = objSnapshot.child ("name").getValue ().toString ();
                            //Toast.makeText (getApplicationContext (), t, Toast.LENGTH_SHORT).show ();
                            if ( n.equals (n_title)) {
                                myRef.child (ResultActivity.uid).child ("Homework").child (date).child (obj).removeValue ();
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