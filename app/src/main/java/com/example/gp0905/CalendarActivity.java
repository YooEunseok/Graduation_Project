package com.example.gp0905;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gp0905.EventDecorator;
import com.example.gp0905.OneDayDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;


public class CalendarActivity extends AppCompatActivity {

    String time,kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;

    String shot_Day;
    String clicked_day;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    //    private FirebaseDatabase database2;
//    private DatabaseReference databaseRef2;
    private ListView listView;
    List fileList = new ArrayList<> ();
    ArrayAdapter adapter;

    private ListView listView2;
    List fileList2 = new ArrayList<> ();
    ArrayAdapter adapter2;

    static boolean calledAlready = false;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if(calledAlready){
            FirebaseDatabase.getInstance ().setPersistenceEnabled (true);
            calledAlready = true;
        }

        final Button add_btn = (Button) findViewById (R.id.add_btn);
        final Button hw_btn = (Button) findViewById (R.id.hw_btn);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        listView = (ListView) findViewById (R.id.schedule_list);
        listView2 = (ListView) findViewById (R.id.hw_list);

        adapter = new ArrayAdapter<String> (this, R.layout.schedule_item, fileList);
        listView.setAdapter (adapter);

        adapter2 = new ArrayAdapter<String> (this, R.layout.hw_item, fileList2);
        listView2.setAdapter (adapter2);
        //adapter.add (z);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY) // 일, 월, 화 .... 식으로 display
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS) // default 화면 MONTHS: 이번달 WEEKS: 이번주
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(), // 일요일 빨간 색으로 표현
                new SaturdayDecorator(), // 토요일 파란색으로 표현
                oneDayDecorator); // 오늘 날짜 deco

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                shot_Day = Year + "/" + Month + "/" + Day;
                clicked_day = Year + " " + Month + " " + Day;

                Log.i("shot_Day test", shot_Day + "");

                Toast.makeText(getApplicationContext(), clicked_day , Toast.LENGTH_SHORT).show();


                database = FirebaseDatabase.getInstance ();
                databaseRef = database.getReference ("User_list");

                database = FirebaseDatabase.getInstance ();
                databaseRef = database.getReference ("User_list");

                final String uid_ = ResultActivity.uid;
                String Schedule_ = EnterSchedule.newScheduleKey;

                final String[] sKey = new String[10]; // Schedule Key
                databaseRef.child (uid_).child ("Schedule").child (clicked_day).addValueEventListener (new ValueEventListener () {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        adapter.clear ();
                        count=0;
                        for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                            String obj = objSnapshot.getKey ();
                            //Toast.makeText (getApplicationContext (), obj, Toast.LENGTH_SHORT).show ();
                            sKey[count] = obj;

                            String title = objSnapshot.child ("title").getValue (String.class);

                            count++;
                            fileList.add(title);
                        }
                        adapter.notifyDataSetChanged ();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final String[] hwKey = new String[10]; // Schedule Key

                databaseRef.child (uid_).child ("Homework").child (clicked_day).addValueEventListener (new ValueEventListener () {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        adapter2.clear ();
                        count=0;
                        for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                            String obj = objSnapshot.getKey ();
                            //Toast.makeText (getApplicationContext (), obj, Toast.LENGTH_SHORT).show ();
                            hwKey[count] = obj;

                            String name = objSnapshot.child ("name").getValue (String.class);
                            String subject = objSnapshot.child ("subject").getValue (String.class);

                            count++;
                            fileList2.add("과목명: " + subject + " " + "과제명: " + name);
                        }
                        adapter2.notifyDataSetChanged ();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        add_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                /*일정 입력 화면으로 전환*/
                Intent intent = new Intent(getApplicationContext (), EnterSchedule.class);
                intent.putExtra ("st_date", clicked_day);
                startActivity (intent);
            }
        });

        hw_btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                /*과제 입력 화면으로 전환*/
                Intent intent = new Intent(getApplicationContext (), EnterHomework.class);
                //intent.putExtra ("st_date", clicked_day);
                startActivity (intent);
            }
        });

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext (), ModifySchedule.class);
                //Toast.makeText (getApplicationContext (), fileList.get (i).toString (), Toast.LENGTH_SHORT).show ();
                intent.putExtra ("st_date", clicked_day);
                intent.putExtra ("title", fileList.get(i).toString ());
                startActivity(intent);
            }
        });

        listView2.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent2 = new Intent(getApplicationContext (), ModifyHomework.class);
                //Toast.makeText (getApplicationContext (), fileList.get (i).toString (), Toast.LENGTH_SHORT).show ();
                intent2.putExtra ("date", clicked_day);
                intent2.putExtra ("name", fileList2.get(i).toString ());
                startActivity(intent2);
            }
        });

    }


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(" ");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays,CalendarActivity.this));
        }
    }
}