package com.example.gp0905;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;


public class timetable extends Activity {
    private Context context;

    private Schedule schedule;
    private TimetableView timetable;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    MyCallback mcb;


    TextView d1, d2, d3, d4, d5;
    Button next_btn, back_btn, add_btn;

    int count = 0;
    int flag1 = 0;//고정일정 변경 여부 확인
    int removeCount = 0;
    int noClass = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        d1 = findViewById(R.id.day1);
        d2 = findViewById(R.id.day2);
        d3 = findViewById(R.id.day3);
        d4 = findViewById(R.id.day4);
        d5 = findViewById(R.id.day5);
        next_btn = findViewById(R.id.next);
        back_btn = findViewById(R.id.back);
        add_btn = findViewById(R.id.add);

        load();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;
                timetable.removeAll();
                load();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                timetable.removeAll();
                load();
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EnterFixedSchedule.class);
                startActivity(intent);
            }
        });
    }


    public void load() {
        removeCount = 0;
        String[] days = new String[6];
        int[] days1 = new int[6];
        String[] days2 = new String[6];

        Date ddd = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String today = sdf.format(ddd);
        Log.d("요일요일", today);
        Log.d("요일요일", sdf.toString());


        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, count);/////////////
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                Log.d("날짜", sdf.format(cal.getTime()));
                days1[i] = cal.get(Calendar.DAY_OF_WEEK);
                days[i] = sdf.format(cal.getTime());
                continue;
            }
            cal.add(Calendar.DATE, 1);
            days1[i] = cal.get(Calendar.DAY_OF_WEEK);
            Log.d("날짜", sdf.format(cal.getTime()));
            days[i] = sdf.format(cal.getTime());
        }

        String[] dayOfWeeks = {"월요일", "화요일", "수요일", "목요일", "금요일"};

        for (int i = 0; i < 5; i++) {
            switch (days1[i]) {
                case 2:
                    days2[i] = "(Mon)";
                    dayOfWeeks[i] = "월요일";
                    break;
                case 3:
                    days2[i] = "(Tue)";
                    dayOfWeeks[i] = "화요일";
                    break;
                case 4:
                    days2[i] = "(Wed)";
                    dayOfWeeks[i] = "수요일";
                    break;
                case 5:
                    days2[i] = "(Thu)";
                    dayOfWeeks[i] = "목요일";
                    break;
                case 6:
                    days2[i] = "(Fri)";
                    dayOfWeeks[i] = "금요일";
                    break;
                case 7:
                    days2[i] = "(Sat)";
                    dayOfWeeks[i] = "토요일";
                    break;
                case 1:
                    days2[i] = "(Sun)";
                    dayOfWeeks[i] = "일요일";
                    break;

            }
        }

        String[] temp = days[0].split("-");
        String t = temp[1] + "/" + temp[2] + "\n" + days2[0];
        d1.setText(t);
        temp = days[1].split("-");
        t = temp[1] + "/" + temp[2] + "\n" + days2[1];
        d2.setText(t);
        temp = days[2].split("-");
        t = temp[1] + "/" + temp[2] + "\n" + days2[2];
        d3.setText(t);
        temp = days[3].split("-");
        t = temp[1] + "/" + temp[2] + "\n" + days2[3];
        d4.setText(t);
        temp = days[4].split("-");
        t = temp[1] + "/" + temp[2] + "\n" + days2[4];
        d5.setText(t);
/*
        Log.d("요일요일","입력한 날짜 : "+sdf.format(cal.getTime()));
        cal.add(Calendar.DATE, 2 - cal.get(Calendar.DAY_OF_WEEK));
        Log.d("요일요일","첫번째 요일(월요일)날짜:"+sdf.format(cal.getTime()));
        cal.setTime(ddd);
        cal.add(Calendar.DATE, 8 - cal.get(Calendar.DAY_OF_WEEK));
        Log.d("요일요일","마지막 요일(일요일)날짜:"+sdf.format(cal.getTime()));

 */

        init();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("User_list");

        final String uid =ResultActivity.uid;
        final int[] count = new int[1];

        final String[] sKey = new String[10]; // Schedule Key

        for (int j = 0; j < 5; j++) {
            int finalJ = j;
            databaseRef.child(uid).child("Schedule").child(days[j]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //   adapter.clear ();
                    for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                        String obj = objSnapshot.getKey();
                        sKey[count[0]] = obj;

                        String title = objSnapshot.child("title").getValue(String.class);
                        String st_time = " ";
                        String ed_time = " ";
                        int flag = 0;//시간할당일정인지아닌지

                        if (objSnapshot.child("start_time").getValue(String.class) != null && objSnapshot.child("end_time").getValue(String.class) != null) {
                            st_time = objSnapshot.child("start_time").getValue(String.class);
                            Log.d("scheduleSearch", "start time: " + st_time);
                            ed_time = objSnapshot.child("end_time").getValue(String.class);
                            Log.d("scheduleSearch", "end time: " + ed_time);
                            flag = 1;
                        }

                        if (objSnapshot.child("fixed_modify").getValue(String.class) != null) {
                            flag = 0;
                        }

                        if (flag == 1) {
                            String[] st = st_time.split(":");
                            String[] ed = ed_time.split(":");

                            schedule = new Schedule();
                            schedule.setStartTime(new Time(Integer.parseInt(st[0]), Integer.parseInt(st[1])));
                            schedule.setEndTime(new Time(Integer.parseInt(ed[0]), Integer.parseInt(ed[1])));
                            schedule.setDay(finalJ);
                            schedule.setClassTitle(title);
                            //schedule.setClassPlace("");
                            //schedule.setProfessorName("");
                            schedules.add(schedule);
                            timetable.add(schedules);

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        String[] removeKey = new String[10];
        String[] removeDate = new String[10];

        for (int j = 0; j < 5; j++) {
            int finalJ = j;
            Log.d("xxxx", days[j]);

            databaseRef.child(uid).child("Schedule").child(days[j]).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot objSnapshot : snapshot.getChildren()) {

                        String obj = objSnapshot.getKey();


                        if (objSnapshot.child("fixed_modify").getValue(String.class) != null) {
                            //if (obj.equals(objSnapshot.child("fixed_modify").getValue(String.class))) {
                            //Log.d("xxxx", "1111");
                            //Log.d("xxxx", "obj1: " + objSnapshot.child("fixed_modify").getValue(String.class));

                            removeKey[removeCount] = objSnapshot.child("fixed_modify").getValue(String.class);
                            Log.d("xxxx", "removeKey[removeCount]: " + removeKey[removeCount]);

                            removeDate[removeCount] = days[finalJ];
                            Log.d("xxxx", "removeDate[removeCount]: " + removeDate[removeCount]);

                            removeCount++;
                            Log.d("xxxx","removeCount: " +removeCount+"");
                            //}
                        }
                    }
                    if(finalJ==4){
                        for (int q = 0; q < 5; q++) {
                            int finalQ = q;
                            databaseRef.child(uid).child("FixedSchedule").child(dayOfWeeks[q]).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //removeCount=0;
                                    for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                                        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                                        String obj = objSnapshot.getKey();

                                        String title = objSnapshot.child("title").getValue(String.class);
                                        Log.d("timetable", "title: " + title);
                                        String st_time = objSnapshot.child("start_time").getValue(String.class);
                                        Log.d("timetable", "start time: " + st_time);
                                        String ed_time = objSnapshot.child("end_time").getValue(String.class);
                                        Log.d("timetable", "end time: " + ed_time);

                                        String[] st = st_time.split(":");
                                        String[] ed = ed_time.split(":");

                                        //
                                        noClass = 0;
                                        ////////////////////////////////////////////////////////////////////////////

                                        //mcb = (rk, rd) -> {
                                        Log.d("xxxx", " ");
                                        Log.d("xxxx", "removeCount:::" + removeCount);


                                        for (int h = 0; h < removeCount; h++) {
                                            Log.d("xxxx", "removeKey[h]:" + removeKey[h]);
                                            Log.d("xxxx", "obj:" + obj);
                                            Log.d("xxxx", "removeDate[h]:" + removeDate[h]);
                                            Log.d("xxxx", "days[finalJ]:" + days[finalQ]);
                                            Log.d("xxxx", " ");


                                            if (removeKey[h].equals(obj) && removeDate[h].equals(days[finalQ])) {
                                                Log.d("xxxx", "loop");

                                                noClass = 1;
                                            } else {

                                            }

                                        }

                                        if (noClass == 0) {
                                            schedule = new Schedule();
                                            schedule.setStartTime(new Time(Integer.parseInt(st[0]), Integer.parseInt(st[1])));
                                            schedule.setEndTime(new Time(Integer.parseInt(ed[0]), Integer.parseInt(ed[1])));
                                            schedule.setDay(finalQ);
                                            schedule.setClassTitle(title);
                                            //schedule.setClassPlace(""); //장소?
                                            //schedule.setProfessorName(""); //메모?
                                            schedules.add(schedule);
                                            timetable.add(schedules);
                                            noClass = 0;
                                        }

                                    }
                                    ;
                                    ////////////////////////////////////////////////////////////////////////////


                                }

                                //}

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    //mcb.onCallback(removeKey, removeDate);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });



            /////////////////////
        }
/*
        for (int j = 0; j < removeCount; j++) {

            Log.d("xxxx", "removeKey[j]: " + removeKey[j]);
            Log.d("xxxx", "removeDate[j]: " + removeDate[j]);

        }

        */

    }

    public interface MyCallback {
        void onCallback(String[] key, String[] date);
        //void onCallback(String value);
    }

    private void init() {
        this.context = this;

        timetable = findViewById(R.id.timetable);
        timetable.setHeaderHighlight(2);
    }
}
