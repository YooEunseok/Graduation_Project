package com.example.gp0905;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    private ListView scheduleListView; // 스케줄 리스트를 간단히 저장 화면에 출력할 때 사용
    private ScheduleListAdapter Adapter;
    private List<Schedule1> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //어플리케이션을 세로로 고정

        scheduleListView = (ListView) findViewById(R.id.scheduleListView);
        scheduleList = new ArrayList<Schedule1>(); //초기화
        scheduleList.add(new Schedule1("졸업작품프로젝트","IT대학","2020-10-11"));
        scheduleList.add(new Schedule1("졸업작품프로젝트","IT대학","2020-10-11"));
        scheduleList.add(new Schedule1("졸업작품프로젝트","IT대학","2020-10-11"));
        scheduleList.add(new Schedule1("졸업작품프로젝트","IT대학","2020-10-11"));

        //임의로 추가 나중에 데이터 불러와서 추가하면 사용가능 지금은 데이터 불러와서 게시해주는 기능 구현X
        //메인 화면에 아직 무엇을 넣을지 정하지 못해서 임의로 작성했음

        Adapter = new ScheduleListAdapter(getApplicationContext(),scheduleList); // 리스트 해당 내역 넣어주는 역할
        scheduleListView.setAdapter(Adapter); //최종적으로 보여주는 역할

        final Button calandarButton = (Button) findViewById(R.id.calandarButton); //캘린더 Fragment 실행
        final Button timetableButton = (Button) findViewById(R.id.timetableButton); // 시간표 Fragment 실행
        final Button chatbotButton = (Button) findViewById(R.id.chatbotButton); //챗봇 Fragment 실행
        final LinearLayout first = (LinearLayout) findViewById(R.id.first);


        calandarButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                first.setVisibility(View.GONE);
                calandarButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); //눌린 버튼 색깔 변화
                timetableButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                chatbotButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                FragmentManager fragmentManager= getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new CalandarFragment());
                fragmentTransaction.commit();
            }
        });

        timetableButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                first.setVisibility(View.GONE);
                calandarButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                timetableButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); //눌린 버튼 색깔 변화
                chatbotButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                FragmentManager fragmentManager= getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new TimeTableFragment());
                fragmentTransaction.commit();
            }
        });

        chatbotButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                first.setVisibility(View.GONE);
                calandarButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                timetableButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                chatbotButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); //눌린 버튼 색깔 변화

                FragmentManager fragmentManager= getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ChatBotFragment());
                fragmentTransaction.commit();
            }
        });

    }
   /* public void ReadData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                               // new ScheduleAdd(document.getString("date"), document.getDouble("start_time"), document.getString("dateOfDay"),
                                        //document.getDouble("end_time"), document.getString("location"),document.getString("title"));

                                a=document.getString("title");
                                b=document.getString("location");
                                c=document.getString("date");

                                Schedule schedule = new Schedule(a,b,c);

                                scheduleList.add(schedule);
                                Log.d(TAG,"=>"+a+b+c);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }코드 사용이 잘 안돼서 일단 주석처리*/
   //임의로 Firestore에 불러와서 메인화면에 공지하는 식으로 코드 작성했었는데 오류가 생겨서 주석처리함
    // 나중에 사용할 기능이면 수정해서 구현

}