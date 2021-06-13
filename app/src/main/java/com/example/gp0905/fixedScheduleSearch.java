package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class fixedScheduleSearch {

    static int index = 0;
    static fixedScheduleSearch.MyCallback mmm;
    static int finish = 0;
    private static fixedScheduleSearch instance = null;
    private static MainActivity activity = null;

    private static int count;
    private static int contain;
    static String uid = ResultActivity.uid;

    static int flag;
    static int flag2 = 0;
    static int things_num = 0;
    final static String[] things = new String[10];
    final static String[] thingsTitle = new String[10];
    final static String[] thingsTime = new String[10];
    final static String[] thingsTime2 = new String[10];
    final static String[] thingsDay = new String[10];

    final static String[] maxKey = new String[1];
    final static String[] maxDate = new String[1];
    static double max_distance = 0;

    static String[] pppp = new String[8];

    public static fixedScheduleSearch getInstance() {
        if (instance == null) {
            instance = new fixedScheduleSearch();
        }
        return instance;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public static void fixedScheduleSearch(fixedScheduleSearch.MyCallback mcb, FirebaseDatabase mDatabase, DatabaseReference databaseRef
            , String[] date, String str, int periodFlag) throws ParseException { //특정일이나 기간 , 고정일정 제목 넘겨받기
        Log.d("ㅇㅇㅇㅇ", "넘어옴");
        contain = 0;
        finish = 0;
        mmm = mcb;
        final double[] maxValue = {0.0};
        int n = 0;

        pppp = date;
        if (periodFlag == 1) {
            for (int l = 0; l < 7; l++) {
                Log.d("pppp.toString()1", pppp[l]);
            }
        }
        if (periodFlag == 0) {

            Log.d("pppp.toString()1", pppp[0]);

        }


        String[] dayOfWeeks = new String[8];
        if (periodFlag == 1) {
            dayOfWeeks = new String[]{"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
            n = 7;
        } else if (periodFlag == 0) {
            String inputDate = date[0];
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date_ = dateFormat.parse(inputDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_);

            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case 2:
                    dayOfWeeks[0] = "월요일";
                    break;
                case 3:
                    dayOfWeeks[0] = "화요일";
                    break;
                case 4:
                    dayOfWeeks[0] = "수요일";
                    break;
                case 5:
                    dayOfWeeks[0] = "목요일";
                    break;
                case 6:
                    dayOfWeeks[0] = "금요일";
                    break;
                case 7:
                    dayOfWeeks[0] = "토요일";
                    break;
                case 1:
                    dayOfWeeks[0] = "일요일";
                    break;

            }

            Log.d("무슨요일", dayOfWeeks[0]);
            n = 1;
        }

        for (int i = 0; i < n; i++) {
            try {
                String[] finalDayOfWeeks = dayOfWeeks;
                int finalI = i;
                int finalI1 = i;
                int finalN = n;
                Log.d("요일", dayOfWeeks[i]);

                databaseRef.child(uid).child("FixedSchedule").child(dayOfWeeks[i]).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            int c = 0;
                            String obj = objSnapshot.getKey();

                            String title = objSnapshot.child("title").getValue(String.class);
                            String st_time = objSnapshot.child("start_time").getValue(String.class);
                            Log.d("scheduleSearch", "start time: " + st_time);
                            String ed_time = objSnapshot.child("end_time").getValue(String.class);
                            Log.d("scheduleSearch", "end time: " + ed_time);
                            String day_of_week = objSnapshot.child("day_of_week").getValue(String.class);
                            Log.d("scheduleSearch", "day_of_week: " + day_of_week);


                            double distance = similarity(str, title); //매개변수랑 일정명 유사도 계산

                            if (title.contains(str)) {
                                things[things_num] = obj;
                                thingsTitle[things_num] = title;
                                thingsTime[things_num] = st_time;
                                thingsTime2[things_num] = ed_time;
                                thingsDay[things_num] = day_of_week;
                                things_num++;
                                Log.d("pppp", "contain: " + title);
                                Log.d("pppp", "contain: " + things_num);
                                c = 1;
                            }

                            Log.d("distance", String.valueOf(distance));
                            if (distance >= maxValue[0] && c == 0) {
                                maxValue[0] = distance;
                                maxKey[0] = obj;
                                maxDate[0] = date[0];
                                Log.d("date[0]", date[0]);

                                thingsDay[0] = day_of_week;

                            }


                            count++;
                        }

                        if (max_distance == 0.0) {
                            //Log.d("캔디", "유사도 0: ");
                            //- 유사도가 0일경우 -> 다시말하라고함
                            //챗봇으로 다시 말해달라고 말함
                            //쿼리를 날려줄지 아니면 자체적으로 말풍선을 띄울지
                        }

                        if (things_num == 1) {//포함되는 일정이 1개 -> 그걸 말해줌
                            //Log.d("캔디", "contain1: " + contains[0]);

                        }
                        if (things_num >= 2 && finalI1 == finalN - 1) {//포함되는 일정이 2개이상 -> 리스트로 선택하게함
                            activity.ttt = 3;
                            activity.mDatas.clear();
                            for (int i1 = 0; i1 < things_num; i1++) {
                                Log.d("캔디", "title: " + thingsTitle[i1]);

                                activity.addData("[" + thingsDay[i1] + "] " + thingsTitle[i1] +
                                        "  (" + thingsTime[i1] + " - " + thingsTime2[i1] + ")");
                            }

                            activity.listViewCheck = 1;
                            Log.d("캔디", "스케쥴1");
                            if (finish == 0) {
                                activity.showTextView("다음 일정들 중 어느 일정인가요?", 10002);
                                things_num = 0;
                                finish = 1;
                            }
                            Log.d("캔디", "스케쥴2");
                            activity.listViewCheck = 0;
                            activity.finish = 1;



                        }
                        if (finish == 0 ) {
                            Log.d("캔디", "스케쥴222"+maxKey[0]);

                            store(mcb, databaseRef);
                            finish = 1;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            } catch (Exception e) {
            }
        }


    }

    public interface MyCallback {
        void onCallback(String value, String title, String st_time, String ed_time, String day_of_week, String dddd);
        //void onCallback(String value);
    }

    public static void store(fixedScheduleSearch.MyCallback mcb, DatabaseReference databaseRef) {
        Log.d("maxDate[0]", maxDate[0]);

        databaseRef.child(uid).child("FixedSchedule").child(thingsDay[0]).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = 0;
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    String obj = objSnapshot.getKey();

                    if (obj.equals(maxKey[0])) {
                        Log.d("캔디", "obj: " + obj);

                        String title = objSnapshot.child("title").getValue(String.class);
                        String st_time = objSnapshot.child("start_time").getValue(String.class);
                        Log.d("scheduleSearch", "start time: " + st_time);
                        String ed_time = objSnapshot.child("end_time").getValue(String.class);
                        Log.d("scheduleSearch", "end time: " + ed_time);
                        String day_of_week = objSnapshot.child("day_of_week").getValue(String.class);
                        Log.d("scheduleSearch", "day_of_week: " + day_of_week);
                        String dddd = maxDate[0];
                       // if (dddd != null) {
                       //     activity.showTextView(dddd+" "+title+" 보강이 저장되었습니다.", 10002);
                       // } else {
                          //
                       // }

                        mcb.onCallback(maxKey[0], title, st_time, ed_time, day_of_week, dddd);
                    }


                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        flag2 = 1;
    }

    private static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;

        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) return 1.0;
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int[] costs = new int[s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];

                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }

                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }

            if (i > 0) costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

}