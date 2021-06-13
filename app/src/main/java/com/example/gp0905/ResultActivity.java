package com.example.gp0905;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnRevoke, btnLogout, btnCal,btnWeek,btnChat,btnTeam,btnMail,btnAssignment,btnTeamSchedule;
    private FirebaseAuth mAuth ;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btnCal = (Button)findViewById (R.id.btn_calendar);
        btnWeek = (Button)findViewById (R.id.btn_weekender);
        btnChat = (Button)findViewById (R.id.btn_chat);
        btnLogout = (Button)findViewById(R.id.btn_logout);
        btnMail = (Button)findViewById(R.id.mailButton);
        btnTeam = (Button)findViewById(R.id.btn_team);
        //btnTeamSchedule = (Button)findViewById(R.id.btn_team_schedule);

       // btnAssignment = (Button)findViewById(R.id.btn_assignment);

        mAuth = FirebaseAuth.getInstance();

        btnCal.setOnClickListener (this);
        btnWeek.setOnClickListener (this);
        btnChat.setOnClickListener (this);
        btnLogout.setOnClickListener(this);
        btnMail.setOnClickListener(this);
        btnTeam.setOnClickListener(this);
        //btnTeamSchedule.setOnClickListener(this);

       // btnAssignment.setOnClickListener(this);


        uid = mAuth.getCurrentUser ().getUid ();
        String uid_test="4YCp1lYaHRhznJTkH5mxYdPCt7z2";
        String email = mAuth.getCurrentUser ().getEmail ();
        String name = mAuth.getCurrentUser ().getDisplayName ();
        //Toast.makeText (this, "uid:"+uid, Toast.LENGTH_SHORT).show ();

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("User_list");
        myRef.child(uid).child ("Email").setValue(email);
        myRef.child(uid).child ("Name").setValue(name);
        myRef.child(uid_test).child("Email").setValue("gdhong@gmail.com");
        myRef.child(uid_test).child("Name").setValue("홍길동");

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    } //로그아웃

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    } //탈퇴

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_calendar: //캘린더
                Intent intent = new Intent(getApplicationContext (), CalendarActivity.class);
                startActivity (intent);
                break;
            case R.id.btn_weekender: //위켄더
                Intent intent1 = new Intent(getApplicationContext (), timetable.class);
                startActivity (intent1);
                break;
            case R.id.btn_team: //팀플
                Intent intent2 = new Intent(getApplicationContext (), Team.class);
                startActivity (intent2);
                break;
            //case R.id.btn_assignment: //과제
            //    Intent intent4 = new Intent(getApplicationContext (), AssignmentActivity.class);
             //   startActivity (intent4);
             ///   break;
            case R.id.btn_chat: //챗봇
                Intent intent3 = new Intent(getApplicationContext (), MainActivity.class);
                startActivity (intent3);
                break;
            case R.id.mailButton: //과제
                Intent intent5 = new Intent(getApplicationContext (), MailActivity.class);
                startActivity (intent5);
                break;
            case R.id.btn_logout: //로그아웃
                  Intent intent4 = new Intent(getApplicationContext (), AssignmentActivity.class);
                   startActivity (intent4);

                break;
          //  case R.id.btn_revoke: //탈퇴
          //      revokeAccess();
            //    finishAffinity();
             //   break;
        }
    }
}