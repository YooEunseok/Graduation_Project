package com.example.gp0905;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//import com.google.firebase.functions.FirebaseFunctions;

public class Team extends AppCompatActivity {
    ArrayAdapter arrayAdapter;
    ArrayList arrayList = new ArrayList<>();

    public static String uid = ResultActivity.uid;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseRef;
    public static String newTeamKey;

    Button copy_btn;
    Button add_btn;
    Button tp_btn;
    EditText uid_tv;
    Button team_add_btn;
    EditText team_tv;
    Spinner spinner;

    String[] ids = new String[10];
    String member_uid;
    String team_name;
    String selected_team;

    int count = 0;
    String subject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_team);

        copy_btn = findViewById(R.id.copy_btn);
        add_btn = findViewById(R.id.add_btn);
        uid_tv = findViewById(R.id.uid_tv);
        team_add_btn = findViewById(R.id.team_add_btn);
        team_tv = findViewById(R.id.team_tv);
        spinner = (Spinner) findViewById(R.id.spinner);

        tp_btn = (Button)findViewById (R.id.team_shedule_btn);

        ids[0] = "4YCp1lYaHRhznJTkH5mxYdPCt7z1";
        ids[1] = "4YCp1lYaHRhznJTkH5mxYdPCt7z2";
        //이부분 모든 멤버의 uid를 디비에서 읽어오는걸로 수정해야함

        mDatabase = FirebaseDatabase.getInstance();
        databaseRef = mDatabase.getReference("User_list");

        uid_tv.setText("4YCp1lYaHRhznJTkH5mxYdPCt7z2");
        uid ="4YCp1lYaHRhznJTkH5mxYdPCt7z1";
        Log.d("(uid)",uid);


        databaseRef.child(uid).child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList = new ArrayList<>();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    subject = objSnapshot.child("subject").getValue(String.class);
                    //Log.d("qqqq", subject);
                    arrayList.add(subject);
                }

                Log.d("qqqq", arrayList.toString());
                arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayList);

                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), arrayList.get(i) + "가 선택되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        selected_team = (String) arrayList.get(i);
                        Log.d("qqqq", selected_team);

                        databaseRef.child(uid).child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                                    String obj = objSnapshot.getKey();
                                    Log.d("qqqq", obj);
                                    subject = objSnapshot.child("subject").getValue(String.class);
                                    //Log.d("qqqq", subject);
                                    int c = objSnapshot.child("count").getValue(int.class);

                                    Log.d("qqqq", c + "");
                                    if (subject.equals(selected_team)) {
                                        Log.d("qqqq", "ok");
                                        newTeamKey = obj;
                                        count = c;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        team_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team_name = team_tv.getText().toString();
                newTeamKey = databaseRef.child(uid).child("Team").push().getKey();
                databaseRef.child(uid).child("Team").child(newTeamKey).child("subject").setValue(team_name);
               // databaseRef.child(uid).child("Team").child(newTeamKey).child("count").setValue(0);
                count++;
                databaseRef.child(uid).child("Team").child(newTeamKey).child("count").setValue(count);
                databaseRef.child(uid).child("Team").child(newTeamKey).child("member" + count).setValue(uid);

                databaseRef.child(uid).child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList = new ArrayList<>();
                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            subject = objSnapshot.child("subject").getValue(String.class);
                            Log.d("qqqq", subject);
                            arrayList.add(subject);
                        }

                        Log.d("qqqq", arrayList.toString());
                        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                arrayList);

                        spinner.setAdapter(arrayAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Toast.makeText(getApplicationContext(), arrayList.get(i) + "가 선택되었습니다.",
                                        Toast.LENGTH_SHORT).show();
                                selected_team = (String) arrayList.get(i);
                                Log.d("qqqq", selected_team);

                                databaseRef.child(uid).child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                                            String obj = objSnapshot.getKey();
                                            Log.d("qqqq", obj);
                                            subject = objSnapshot.child("subject").getValue(String.class);
                                            Log.d("qqqq", subject);
                                            int c = objSnapshot.child("count").getValue(int.class);
                                            Log.d("qqqq", c + "");
                                            if (subject.equals(selected_team)) {
                                                Log.d("qqqq", "ok");
                                                newTeamKey = obj;
                                                count = c;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;

                databaseRef.child(uid).child("Team").child(newTeamKey).child("count").setValue(count);
                member_uid = uid_tv.getText().toString();
                databaseRef.child(uid).child("Team").child(newTeamKey).child("member" + count).setValue(member_uid);

                databaseRef.child(member_uid).child("Team").child(newTeamKey).child("subject").setValue(team_name);
                databaseRef.child(member_uid).child("Team").child(newTeamKey).child("count").setValue(count);
                databaseRef.child(member_uid).child("Team").child(newTeamKey).child("member" + count).setValue(uid);


                //이부분 한 멤버가 늘어날때마다 ++되고 그게 그대로 새 멤버 디비에 저장되도록 수정해줘야함

            }
        });

        tp_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext (), Team_schedule.class);
                intent.putExtra ("subject", subject);
                intent.putExtra ("count", count);
                startActivity (intent);
            }
        });

    }
}