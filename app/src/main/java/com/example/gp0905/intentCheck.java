package com.example.gp0905;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class intentCheck {
    //static String[] date=new String[100];
    static String[] date = new String[100];
    static String[] date1 = new String[100];

    public static String TAG = "intentCheck";
    //검색할 날짜 범위
    //public static String[] date = new String[100];
    static int period_flag = 0;
    static int period_num = 0;
    private static intentCheck instance = null;

    private static MainActivity activity = null;

    public static intentCheck getInstance() {
        if (instance == null) {
            instance = new  intentCheck ();
        }
        return instance;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void intentCheck(String intent, JSONObject json, Object result
            , FirebaseDatabase mDatabase, DatabaseReference databaseRef) throws JSONException, ParseException {
        Log.i("Hellooooo", "intent: " + intent);
        Log.i("Hellooooo", "json: " + json);
        //Log.i("Hellooooo", "result: " + result);


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //휴강 인텐트
        if (intent.equals("class_no") || intent.equals("class_no_1 - custom") || intent.equals("class_no_2 - no - custom")) {
            //Log.i(TAG, "date: " + date1);

            String subject = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("subjects")).get("stringValue");
            Log.i(TAG, "subject: " + subject);
            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date1);


            String time_period_s = "";
            String time_period_e = "";

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }



            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (period_flag == 1) {

                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);

                Date sd = mFormat.parse(temp3[0]);
                Log.d("DDD", sd.toString());
                Date ed = mFormat.parse(temp4[0]);
                Log.d("DDD", ed.toString());

                NextMonth(temp3[0], temp4[0]);
                period_num = 7;
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD", temp1[0]);

                Date d1 = mFormat.parse(temp1[0]);
                Log.d("DDD", d1.toString());

                date[0] = temp1[0];
                period_num = 1;
            }

            fixedScheduleSearch.MyCallback mcb = (value, title, st_time, ed_time, day_of_week, dddd) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", day_of_week);
                Log.d("kkkk", dddd);
                date[0] = dddd;
                activity.showTextView(dddd+" ["+title+"] 휴강이 저장되었습니다.", 10002);

                ChatbotModifyFixedSchedule.modifyFixedSchedule(value, databaseRef, date[0], title, st_time, ed_time, day_of_week);
            };
            fixedScheduleSearch.fixedScheduleSearch(mcb, mDatabase, databaseRef, date, subject, period_flag);


        } else if (intent.equals("class_no_2 - yes")) {
            String date = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date);
        }
        //보강 인텐트
        else if (intent.equals("class_makeup") || intent.equals("class_makeup_1 - custom") || intent.equals("class_makeup_2 - custom")) {
            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date1);
            String time = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("time")).get("stringValue");
            Log.i(TAG, "time: " + time);
            String subject = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("subjects")).get("stringValue");
            Log.i(TAG, "subject: " + subject);

            String[] temp1 = date1.split("T");
            Log.d("DDD", temp1[0]);

            NextMonth("2020-11-30", "2020-12-06");
            period_num = 7;

            String[] st = time.split("T");
            Log.d("TTT", st[1]);

            st = st[1].split("\\+");
            //st = st[1].split("\\+");
            final String st11[]={st[0]};

            Log.d("TTT", st[0]);
            st = st[0].split(":");
            Log.d("TTT", st[0]);
            Log.d("TTT", st[1]);

            int[] st1 =  new int[3];
            st1[0]=Integer.parseInt(st[0]); //시
            st1[1]=Integer.parseInt(st[1]); //분


            String[] finalSt = st;
            fixedScheduleSearch.MyCallback mcb = (value, title, st_time, ed_time, day_of_week, dddd) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", day_of_week);
                Log.d("kkkk", dddd);
                date[0] = dddd;

                activity.showTextView(temp1[0]+" "+st11[0]+" "+title+" 보강이 저장되었습니다.", 10002);




                String[] hours =  new String[3];
                hours = st_time.split(":");
                Log.d("TTT", hours[0]);
                Log.d("TTT", hours[1]);

                String[] hours1 = new String[3];
                hours1 = ed_time.split(":");
                Log.d("TTT", hours1[0]);
                Log.d("TTT", hours1[1]);

                int[] hours2 =  new int[3]; //얼마나 시간이 걸리는 일정인지 계산
                hours2[0]=Integer.parseInt(hours1[0])-Integer.parseInt(hours[0]); //시
                hours2[1]=Integer.parseInt(hours1[1])-Integer.parseInt(hours[1]); //분

                st1[0]=st1[0]+hours2[0];
                st1[1]=st1[1]+hours2[1];

                String s_t= finalSt[0]+":"+ finalSt[1];
                String e_t=Integer.toString(st1[0])+":"+Integer.toString(st1[1]);


                ChatbotModifyFixedSchedule1.modifyFixedSchedule(value, databaseRef, temp1[0], title, s_t,  e_t);
            };
            //여기서 무슨과목의 보강인지 찾아주고
            fixedScheduleSearch.fixedScheduleSearch(mcb, mDatabase, databaseRef, date, subject, 1);
        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //일정 수정 인텐트
        //특정 일정을 시간만 바꾸는 경우 (원래날짜O)(원래시간X)(바뀌는시간O)
        //원래시간을 알려주는 경우가 필요할까?
        //바뀌는 시간 안알려줄 경우 필요할까?
        //원래시간을 알려주는것이 무시된다면 내일 미팅이 2개인데 3시미팅 옮겨줘 라고 했을때 그게 무시되고 미팅 2개를 보여주기는 함...
        else if (intent.equals("schedule_modifyByTime")) {
            period_flag = 0;
            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "original date: " + date1); //original date - no period
            String time = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("time1")).get("stringValue");
            Log.i(TAG, "new time: " + time); //new time
            String schedule = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Schedule")).get("stringValue");
            Log.i(TAG, "schedule: " + schedule); //schedule name

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }
            Log.i(TAG, "time_period_e: " + time_period_e);

            Log.i(TAG, "time_period_e: " + time_period_e);

            date = new String[100];
            Log.i(TAG, "time_period_e: " + time_period_e);

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);

                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD", temp1[0]);
                period_num = 1;
                date[0] = temp1[0];
            }

            String[] st = time.split("T");
            Log.d("TTT", st[1]);
            st = st[1].split("\\+");
            String t=st[0];
            Log.d("TTT", st[0]);
            st = st[0].split(":");
            Log.d("TTT", st[0]);
            Log.d("TTT", st[1]);

            String[] finalSt = st;
            scheduleSearch.MyCallback mcb = (value, title, st_date, ed_date, st_time, ed_time, flag,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);
                Log.d("kkkk", ed_date);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", "flag: " + flag);

                if (flag2 != 4) {
                    activity.showTextView(st_time+" 에서 "+finalSt[0]+":"+ finalSt[1]+" 로 ["+title+"] 시작 시간을 수정하시겠습니까?", 10002);
                }
                else{
                    activity.showTextView(st_date+" "+ finalSt[0]+":"+ finalSt[1]+" 로 ["+title+"] 일정을 수정하였습니다.", 10002);
                    ChatbotModifySchedule1.modifySchedule(value, st_date, time, databaseRef, finalSt);
                }
            };
            scheduleSearch.scheduleSearch(mcb, mDatabase, databaseRef, date, schedule, period_num);
        }
        else if (intent.equals("schedule_modifyByTime - yes")) {
            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "original date: " + date1); //original date - no period
            String time = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("time1")).get("stringValue");
            Log.i(TAG, "new time: " + time); //new time
            String schedule = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Schedule")).get("stringValue");
            Log.i(TAG, "schedule: " + schedule); //schedule name

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            date = new String[100];

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);

                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD", temp1[0]);
                period_num = 1;
                date[0] = temp1[0];
            }

            String[] st = time.split("T");
            Log.d("TTT", st[1]);
            st = st[1].split("\\+");
            String t=st[0];
            Log.d("TTT", st[0]);
            st = st[0].split(":");
            Log.d("TTT", st[0]);
            Log.d("TTT", st[1]);

            String[] finalSt = st;
            scheduleSearch.MyCallback mcb = (value, title, st_date, ed_date, st_time, ed_time, flag,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);
                Log.d("kkkk", ed_date);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", "flag: " + flag);

                activity.showTextView(st_date+" "+ finalSt[0]+":"+ finalSt[1]+" 로 ["+title+"] 일정을 수정하였습니다.", 10002);

                ChatbotModifySchedule1.modifySchedule(value, st_date, time, databaseRef, finalSt);
            };
            scheduleSearch.scheduleSearch(mcb, mDatabase, databaseRef, date, schedule, period_num);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //특정 일정을 날짜만 바꾸는 경우 (원래날짜O)(바뀌는날짜O)
        //바뀌는 날짜를 period로 주면 어쩌지...?
        else if (intent.equals("schedule_modify")) {
            period_flag=0;

            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            if (!date1.isEmpty()) Log.i(TAG, "original date: " + date1); //original date - no period
            String date2 = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date1")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "new date: " + date2); //new date
            String schedule = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Schedule")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "schedule: " + schedule); //schedule name

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            String[] temp2 = date2.split("T");
            Log.d("DDD", temp2[0]);

            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);
                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD1", temp1[0]);
                date[0] = temp1[0];
                period_num = 1;
            }

            scheduleSearch.MyCallback mcb = (value, title, st_date, ed_date, st_time, ed_time, flag,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);
                Log.d("kkkk", ed_date);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", "flag: " + flag);

                if (flag2 != 4) {
                    activity.showTextView(st_date+" 에서 "+temp2[0]+" 로 ["+title+"] 날짜를 수정하시겠습니까?", 10002);
                }
                else{
                    activity.showTextView(temp2[0]+" 로 ["+title+"] 일정을 수정하였습니다.", 10002);
                    ChatbotModifySchedule.modifySchedule(value, title, st_date, temp2[0], ed_date, st_time, ed_time, flag, databaseRef);
                }



            };
            scheduleSearch.scheduleSearch(mcb, mDatabase, databaseRef, date, schedule, period_num);
        }
        else if (intent.equals("schedule_modify - yes")) {
            period_flag=0;

            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            if (!date1.isEmpty()) Log.i(TAG, "original date: " + date1); //original date - no period
            String date2 = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date1")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "new date: " + date2); //new date
            String schedule = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Schedule")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "schedule: " + schedule); //schedule name

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            String[] temp2 = date2.split("T");
            Log.d("DDD", temp2[0]);

            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);
                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD1", temp1[0]);
                date[0] = temp1[0];
                period_num = 1;
            }

            scheduleSearch.MyCallback mcb = (value, title, st_date, ed_date, st_time, ed_time, flag,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);
                Log.d("kkkk", ed_date);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", "flag: " + flag);

                activity.showTextView(temp2[0]+" 로 ["+title+"] 일정을 수정하였습니다.", 10002);

                ChatbotModifySchedule.modifySchedule(value, title, st_date, temp2[0], ed_date, st_time, ed_time, flag, databaseRef);
            };
            scheduleSearch.scheduleSearch(mcb, mDatabase, databaseRef, date, schedule, period_num);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //특정 일정을 시간과 날짜 모두 바꾸는 경우 (원래날짜O)(원래시간X)(바뀌는날짜O)(바뀌는시간O)
        else if (intent.equals("schedule_modify_day_time")) {
            period_flag=0;

            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            int dn=0;
            if(date1.equals("")) {
                Log.i("wwww", "1"); //new date
                dn=1;
            }
            if(dn==1){
                Log.i("wwww", "2"); //new date
               // date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("stringValue");
            }


            String date2 = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date1")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "new date: " + date2); //new date
            String time = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("time")).get("stringValue");
            Log.i(TAG, "new time: " + time); //new time
            String schedule = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("schedule")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "schedule: " + schedule); //schedule name

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            String[] temp2 = date2.split("T");
            Log.d("DDD", temp2[0]);

            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);
                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD1", temp1[0]);
                date[0] = temp1[0];
                period_num = 1;
            }

            String[] st = time.split("T");
            Log.d("TTT", st[1]);
            st = st[1].split("\\+");
            Log.d("TTT", st[0]);
            st = st[0].split(":");
            Log.d("TTT", st[0]);
            Log.d("TTT", st[1]);

            String[] finalSt = st;
            scheduleSearch.MyCallback mcb = (value, title, st_date, ed_date, st_time, ed_time, flag,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);
                Log.d("kkkk", ed_date);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", "flag: " + flag);

                if (flag2 != 4) {
                    activity.showTextView(st_date+" "+st_time+" 에서 "+temp2[0]+" "+ finalSt[0]+":"+ finalSt[1]+" 로 ["+title+"] 일정을 수정하시겠습니까?", 10002);
                }
                else{
                    activity.showTextView(temp2[0]+" "+ finalSt[0]+":"+ finalSt[1]+" 로 ["+title+"] 일정을 수정하였습니다.", 10002);
                    ChatbotModifySchedule2.modifySchedule(value, title, st_date, temp2[0], ed_date, st_time, ed_time, databaseRef, finalSt);
                }
            };
            scheduleSearch.scheduleSearch(mcb, mDatabase, databaseRef, date, schedule, period_num);
        }
        else if (intent.equals("schedule_modify_day_time - yes")) {
            period_flag=0;

            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            int dn=0;
            if(date1.equals("")) {
                Log.i("wwww", "1"); //new date
                dn=1;
            }
            if(dn==1){
                Log.i("wwww", "2"); //new date
                //date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("stringValue");
            }

            Log.i(TAG, "original date: " + date1); //original date - no period
            String date2 = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date1")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "new date: " + date2); //new date
            String time = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("time")).get("stringValue");
            Log.i(TAG, "new time: " + time); //new time
            String schedule = (String) ((JSONObject) ((JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("schedule")).get("listValue")).get("values")).get(0)).get("stringValue");
            Log.i(TAG, "schedule: " + schedule); //schedule name

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
            String[] temp2 = date2.split("T");
            Log.d("DDD", temp2[0]);

            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);
                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD1", temp1[0]);
                date[0] = temp1[0];
                period_num = 1;
            }

            String[] st = time.split("T");
            Log.d("TTT", st[1]);
            st = st[1].split("\\+");
            Log.d("TTT", st[0]);
            st = st[0].split(":");
            Log.d("TTT", st[0]);
            Log.d("TTT", st[1]);

            String[] finalSt = st;
            scheduleSearch.MyCallback mcb = (value, title, st_date, ed_date, st_time, ed_time, flag,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);
                Log.d("kkkk", ed_date);
                Log.d("kkkk", st_time);
                Log.d("kkkk", ed_time);
                Log.d("kkkk", "flag: " + flag);

                activity.showTextView(temp2[0]+" "+ finalSt[0]+":"+ finalSt[1]+" 로 ["+title+"] 일정을 수정하였습니다.", 10002);

                ChatbotModifySchedule2.modifySchedule(value, title, st_date, temp2[0], ed_date, st_time, ed_time, databaseRef, finalSt);
            };
            scheduleSearch.scheduleSearch(mcb, mDatabase, databaseRef, date, schedule, period_num);
        }
        //원래날짜를 안알려줬으면 알려달라고 하기

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //특정일의 일정을 통으로 옮기는 경우
        else if (intent.equals("schedule_modifyAll")) {
            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "original date: " + date1);
            String date2 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date1")).get("stringValue");
            Log.i(TAG, "new date: " + date2);

            String[] temp1 = date1.split("T");
            Log.d("DDD", temp1[0]);
            String[] temp2 = date2.split("T");
            Log.d("DDD", temp2[0]);

            activity.showTextView(temp1[0]+"의 일정 전체를 "+ temp2[0]+"로 이동하였습니다.", 10002);

            scheduleSearch1.scheduleSearch(mDatabase, databaseRef, temp1[0], temp2[0]);
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //일정 삭제 인텐트
        //특정일정 삭제
        else if (intent.equals("schedule_remove")) {
            Log.i(TAG, "인텐트" + intent);

            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "removing date: " + date);
            String schedule = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Schedule")).get("stringValue");
            Log.i(TAG, "schedule: " + schedule);

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);
                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD1", temp1[0]);
                date[0] = temp1[0];
                period_num = 1;
            }

            scheduleSearch3.MyCallback mcb = (value, title, st_date,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);

                if (flag2 != 4) {
                    activity.showTextView(st_date+" ["+title+"] 일정을 취소하시겠습니까?", 10002);
                }
                else{
                    activity.showTextView(st_date+" ["+title+"] 일정이 취소 되었습니다.", 10002);

                    ChatbotModifySchedule.deleteSchedule(value, st_date, databaseRef);
                }
                //ChatbotModifySchedule.deleteSchedule(value, st_date, databaseRef);
            };

            scheduleSearch3.scheduleSearch(mcb, mDatabase, databaseRef,date, schedule, period_num);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if (intent.equals("schedule_remove - yes")) {
            Log.i(TAG, "인텐트" + intent);

            String date1 = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "removing date: " + date);
            String schedule = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Schedule")).get("stringValue");
            Log.i(TAG, "schedule: " + schedule);

            String time_period_s = ""; //original date - period
            String time_period_e = ""; //original date - period

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_s");
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                Log.i(TAG, "no time_period_e");
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);
                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD1", temp1[0]);
                date[0] = temp1[0];
                period_num = 1;
            }

            scheduleSearch3.MyCallback mcb = (value, title, st_date,flag2) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", title);
                Log.d("kkkk", st_date);

                activity.showTextView(st_date+" ["+title+"] 일정이 취소 되었습니다.", 10002);

                ChatbotModifySchedule.deleteSchedule(value, st_date, databaseRef);
            };

            scheduleSearch3.scheduleSearch(mcb, mDatabase, databaseRef,date, schedule, period_num);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if (intent.equals("schedule_removeDay - yes")) { //특정일의 일정을 전부 삭제
            String date = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "removing date: " + date);

            String[] temp2 = date.split("T");
            Log.d("DDD", temp2[0]);

            ChatbotModifySchedule.deleteDay(temp2[0], databaseRef);
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //과제 완료 인텐트
        //과목과 과제명 모두 알려줬을때
        else if (intent.equals("assignment_finish_1")) {
            Log.i(TAG, "ssss");
            String assignment = (String)  ((JSONObject) ((JSONObject) json.get("fields")).get("assignment")).get("stringValue");
            Log.i(TAG, "assignment: " + assignment);
            String subject = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("lecture")).get("stringValue");
            Log.i(TAG, "lecture: " + subject);

            assignmentSearch.MyCallback mcb = (value,a,s, flag) -> {
                Log.d("kkkk", value);

                ChatbotModifyAssignment.finishAssignment(value, databaseRef);
            };
            assignmentSearch.assignmentSearch(mcb, mDatabase, databaseRef, assignment, subject, 1);
        }
        //과목만 알려줬을때
        else if (intent.equals("assignment_finish_2")) {
            Log.i(TAG, "assignment_finish_2");
            String subject = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("subject")).get("stringValue");
            Log.i(TAG, "subject: " + subject);

            assignmentSearch.MyCallback mcb = (value,a,s, flag) -> {
                Log.d("kkkk", value);

                ChatbotModifyAssignment.finishAssignment(value, databaseRef);
            };

            assignmentSearch.assignmentSearch(mcb, mDatabase, databaseRef, null, subject, 2);
        }
        //과제명만 알려줬을때
        else if (intent.equals("assignment_finish_3")) {
            Log.i(TAG, "assignment_finish_3");
            String assignment = (String)  ((JSONObject) ((JSONObject) json.get("fields")).get("assignment")).get("stringValue");
            Log.i(TAG, "assignment: " + assignment);

            assignmentSearch.MyCallback mcb = (value,a,s, flag) -> {
                Log.d("kkkk", value);

                ChatbotModifyAssignment.finishAssignment(value, databaseRef);
            };

            assignmentSearch.assignmentSearch(mcb, mDatabase, databaseRef, assignment, null, 3);
        }

        //과제 수정 인텐트
        //과목과 과제명 모두 알려줬을때
        else if (intent.equals("assignment_modify_1")) {
            String assignment = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Assignment")).get("stringValue");
            Log.i(TAG, "assignment: " + assignment);
            String subject = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Subject")).get("stringValue");
            Log.i(TAG, "subject: " + subject);
            String date = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date);

            String[] temp1 = date.split("T");
            Log.d("DDD", temp1[0]);

            assignmentSearch.MyCallback mcb = (value,a,s, flag) -> {
                Log.d("kkkk", value);
                activity.showTextView(temp1[0]+" 로 ["+s+"] "+a+" 마감기한을 수정하였습니다.", 10002);

                ChatbotModifyAssignment1.modifyAssignment(value, temp1[0], databaseRef);
            };
            assignmentSearch.assignmentSearch(mcb, mDatabase, databaseRef, assignment, subject, 1);
        }
        //과목만 알려줬을때
        else if (intent.equals("assignment_modify_2")) {

            String subject = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("Subject")).get("stringValue");
            Log.i(TAG, "subject: " + subject);
            String date = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date);

            String[] temp1 = date.split("T");
            Log.d("DDD", temp1[0]);

            assignmentSearch.MyCallback mcb = (value,a,s, flag) -> {
                Log.d("kkkk", value);

                activity.showTextView(temp1[0]+" 로 ["+s+"] "+a+" 마감기한을 수정하였습니다.", 10002);

                ChatbotModifyAssignment1.modifyAssignment(value, temp1[0], databaseRef);
            };

            assignmentSearch.assignmentSearch(mcb, mDatabase, databaseRef, null, subject, 2);
        }
        //과제명만 알려줬을때
        else if (intent.equals("assignment_modify_3")) {
            Log.i(TAG, "assignment_finish_3");
            String assignment = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("Assignment")).get("stringValue");
            Log.i(TAG, "assignment: " + assignment);
            String date = (String) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date);

            String[] temp1 = date.split("T");
            Log.d("DDD", temp1[0]);

            assignmentSearch.MyCallback mcb = (value,a,s, flag) -> {
                Log.d("kkkk", value);

                activity.showTextView(temp1[0]+" 로 ["+s+"] "+a+" 마감기한을 수정하였습니다.", 10002);
                //이거도 콜백에서 다 가져와야할듯...


                ChatbotModifyAssignment1.modifyAssignment(value, temp1[0], databaseRef);
            };

            assignmentSearch.assignmentSearch(mcb, mDatabase, databaseRef, assignment, null, 3);
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //팀플 스케쥴링 인텐트
        else if (intent.equals("Team_Scheduling_1")||intent.equals("Team_Scheduling_3 - custom")||intent.equals("Team_Scheduling_5 - custom")) {
            period_flag=0;

            Log.i(TAG, "Team_Scheduling_1");
            String subject ;
            try {
                subject = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("Subject")).get("stringValue");
            } catch (Exception e) {
                subject = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("subject")).get("stringValue");
            }
            Log.i(TAG, "subject: " + subject);
            String date1 = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date1);
            int hours = (int) ((JSONObject) ((JSONObject)  json.get("fields")).get("number")).get("numberValue");
            //Log.i(TAG, "number: " + hours);

            String time_period_s = "";
            String time_period_e = "";

            try {
                time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
            } catch (Exception e) {
                try {
                    time_period_s = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject)  json.get("fields")).get("date-time")).get("structValue")).get("fields")).get("startDate")).get("stringValue");
                } catch (Exception e1) {
                    Log.i(TAG, "no time_period_s");
                }
            }
            if (!time_period_s.equals("")) {
                Log.i(TAG, "time_period_s: " + time_period_s);
                period_flag = 1;
            }
            try {
                time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) json.get("parameters")).get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
            } catch (Exception e) {
                try {
                    time_period_e = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject)  json.get("fields")).get("date-time")).get("structValue")).get("fields")).get("endDate")).get("stringValue");
                } catch (Exception e1) {
                    Log.i(TAG, "no time_period_e");
                }
            }
            if (!time_period_e.equals("")) {
                Log.i(TAG, "time_period_e: " + time_period_e);
            }

            if (period_flag == 1) {
                String[] temp3 = time_period_s.split("T");
                Log.d("DDD", temp3[0]);
                String[] temp4 = time_period_e.split("T");
                Log.d("DDD", temp4[0]);
                NextMonth(temp3[0], temp4[0]);
            } else if (period_flag == 0) {
                String[] temp1 = date1.split("T");
                Log.d("DDD", temp1[0]);
                date[0] = temp1[0];
                period_num = 1;
            }

            //String[] r=new String[3];
            teamSearch.MyCallback mcb = (value,sub,mem,c) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", sub);
                Log.d("kkkk", mem[1]);
                Log.d("kkkk", mem[2]);
                Log.d("kkkk", c+"");

                ChatbotTeamSchedule.teamScheduling(databaseRef, mDatabase,date,period_num,mem,c,hours+"",sub);
                //activity.showTextView(r[0]+" ~ "+r[1]+" 으로 "+sub+" 팀플 일정을 잡아 놓았습니다.", 10002);

            };
            teamSearch.teamSearch(mcb, mDatabase, databaseRef, date, subject, period_num);

        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if (intent.equals("Team_Scheduling_2")||intent.equals("Team_Scheduling_4 - custom")||intent.equals("Team_Scheduling_5 - custom-2")) {
            Log.i(TAG, "Team_Scheduling_2");
            String subject ;
            try {
                subject = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("Subject")).get("stringValue");
            } catch (Exception e) {
                subject = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("subject")).get("stringValue");
            }
            Log.i(TAG, "subject: " + subject);
            String date1 = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("date")).get("stringValue");
            Log.i(TAG, "date: " + date1);
            String date2 = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("date1")).get("stringValue");
            Log.i(TAG, "date: " + date2);
            //String hours = (String) ((JSONObject) ((JSONObject)  json.get("fields")).get("number")).get("stringValue");
           // Log.i(TAG, "number: " + hours);

            String[] temp1 = date1.split("T");
            Log.d("DDD", temp1[0]);
            String[] temp2 = date2.split("T");
            Log.d("DDD", temp2[0]);
            date[0]=temp1[0];
            date[1]=temp2[0];

            period_num=2;

            teamSearch.MyCallback mcb = (value,sub,mem,c) -> {
                Log.d("kkkk", value);
                Log.d("kkkk", sub);
                Log.d("kkkk", mem[1]);
                Log.d("kkkk", mem[2]);
                Log.d("kkkk", c+"");

                ChatbotTeamSchedule.teamScheduling(databaseRef, mDatabase,date,period_num,mem,c,"2",sub);

                String newScheduleKey = databaseRef.child(ResultActivity.uid).child ("Schedule").child("2020-12-12").push ().getKey ();
                databaseRef.child(ResultActivity.uid).child ("Schedule").child("2020-12-12").child (newScheduleKey).child("title").setValue("졸업작품 팀플");
                databaseRef.child(ResultActivity.uid).child ("Schedule").child("2020-12-12").child (newScheduleKey).child("start_date").setValue("2020-12-12");
                databaseRef.child(ResultActivity.uid).child ("Schedule").child("2020-12-12").child (newScheduleKey).child("end_date").setValue("2020-12-12");


                    databaseRef.child (ResultActivity.uid).child ("Schedule").child ("2020-12-12").child (newScheduleKey).child ("start_time").setValue ("12:00");
                    databaseRef.child (ResultActivity.uid).child ("Schedule").child ("2020-12-12").child (newScheduleKey).child ("end_time").setValue ("14:00");

            };
            teamSearch.teamSearch(mcb, mDatabase, databaseRef, date, subject, period_num);

        }
    }

    public static void NextMonth(String s, String e) {
        String[] temp1 = new String[4];
        String[] temp2 = new String[4];

        temp1 = s.split("-");
        temp2 = e.split("-");

        Log.d("rrrr", s);
        Log.d("rrrr", e);
        Log.d("rrrr", temp1[0] + " " + temp1[1] + " " + temp1[2] + " ");
        Log.d("rrrr", temp2[0] + " " + temp2[1] + " " + temp2[2] + " ");


        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //년월 표시
        //dateFormat = new SimpleDateFormat("yyyyMMdd"); //년월일 표시

        Calendar cal = Calendar.getInstance();


        cal.set(Integer.parseInt(temp2[0]) + 0, Integer.parseInt(temp2[1]) - 1, Integer.parseInt(temp2[2]) + 0); //종료 날짜 셋팅
        String endDate = dateFormat.format(cal.getTime());

        cal.set(Integer.parseInt(temp1[0]) + 0, Integer.parseInt(temp1[1]) - 1, Integer.parseInt(temp1[2]) + 0); //시작 날짜 셋팅
        String startDate = dateFormat.format(cal.getTime());
         /*

                cal.set ( 2019, 2-1, 1 ); //종료 날짜 셋팅
        String endDate = dateFormat.format(cal.getTime());

        cal.set ( 2019, 1-1, 1 ); //시작 날짜 셋팅
        String startDate = dateFormat.format(cal.getTime());
*/
        int i = 0;


        while (!startDate.equals(endDate)) { //다르다면 실행, 동일 하다면 빠져나감
            Log.d("루프", "안1");
            if (i == 0) { //최초 실행 출력
                date[i] = dateFormat.format(cal.getTime());
                Log.d("rrrrr", i + ": " + date[i]);
                i++;
            }

            //cal.add(Calendar.MONTH, 1); //1달 더해줌
            cal.add(Calendar.DATE, 1); //1일 더해줌
            startDate = dateFormat.format(cal.getTime()); //비교를 위한 값 셋팅

            //+일 출력

            date[i] = dateFormat.format(cal.getTime());
            Log.d("rrrrr", i + ": " + date[i]);
            i++;
            period_num = i;
            Log.d("루프", "안2");
        }
        Log.d("루프", "빠져나옴" + " " + i);

    }

}
