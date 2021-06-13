package com.example.gp0905;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class scheduleSearch3 {
    static String uid = ResultActivity.uid;
    private static scheduleSearch3 instance = null;
    private static MainActivity activity = null;
    static MyCallback mmm;
    static int finish = 0;

    static int flag;
    static int flag2 = 0;

    static int contain = 0;
    final static String[] contains = new String[10];
    final static String[] containsDate = new String[10];
    final static String[] containsTitle = new String[10];
    final static String[] containsDate2 = new String[10];
    final static String[] containsTime = new String[10];
    final static String[] containsTime2 = new String[10];

    final static String[] maxKey = new String[1];
    final static String[] maxDate = new String[1];
    static double max_distance = 0;

    public static scheduleSearch3 getInstance() {
        if (instance == null) {
            instance = new scheduleSearch3();
        }
        return instance;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public static void scheduleSearch(MyCallback mcb, FirebaseDatabase mDatabase, DatabaseReference databaseRef
            , String[] date, String str, int n) {
        contain=0;
        finish = 0;
        mmm = mcb;

        max_distance =0;
        maxKey[0] =null;
        //break;
        maxDate[0] =null;

        Log.d("scheduleSearch", "n: " + n);

        for (int i = 0; i < n; i++) {
            Log.d("scheduleSearch", "date[i]: " + date[i]);
        }

        final double[] maxValue = {0.0};

        for (int i = 0; i < n; i++) {
            try {
                int finalI = i;
                int finalI1 = i;
                int finalI2 = i;
                databaseRef.child(uid).child("Schedule").child(date[i]).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("scheduleSearch", "date[i]: " + date[finalI1]);

                        //contain = 0;

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {

                            String obj = objSnapshot.getKey();

                            String title = objSnapshot.child("title").getValue(String.class);
                            String st_date = objSnapshot.child("start_date").getValue(String.class);
                            String ed_date = "";
                            if (objSnapshot.child("end_date").getValue(String.class) != null)
                                ed_date = objSnapshot.child("end_date").getValue(String.class);
                            String st_time = "";
                            String ed_time = "";
                            flag = 0;//시간할당일정인지아닌지

                            if (objSnapshot.child("start_time").getValue(String.class) != null && objSnapshot.child("end_time").getValue(String.class) != null) {
                                st_time = objSnapshot.child("start_time").getValue(String.class);
                                Log.d("scheduleSearch", "start time: " + st_time);
                                ed_time = objSnapshot.child("end_time").getValue(String.class);
                                Log.d("scheduleSearch", "end time: " + ed_time);
                                flag = 1;
                            }

                            double distance = similarity(str, title); //매개변수랑 일정명 유사도 계산

                            if (title.contains(str)) {
                                contains[contain] = obj;
                                containsDate[contain] = st_date;
                                containsDate2[contain] = ed_date;
                                containsTitle[contain] = title;
                                containsTime[contain] = st_time;
                                containsTime2[contain] = ed_time;
                                contain++;
                                Log.d("pppp", "contain: " + title);
                                Log.d("pppp", "contain: " + contain);
                            }

                            Log.d("distance", String.valueOf(distance));
                            if (distance >= maxValue[0]) {
                                Log.d("mmmm", "졸려졸려" + distance);
                                maxValue[0] = distance;
                                max_distance = distance;
                                maxKey[0] = obj;
                                //break;
                                maxDate[0] = date[finalI];
                                Log.d("캔디1", "맥스키: " + maxKey[0]);
                                Log.d("캔디1", "맥스데이트: " + maxDate[0]);

                            }
                        }
                        Log.d("캔디2", "맥스키: " + maxKey[0]);
                        Log.d("캔디2", "맥스데이트: " + maxDate[0]);
                        Log.d("캔디2", "맥스디스턴스: " +max_distance);

                        if (max_distance == 0) {
                            //Log.d("캔디", "유사도 0: ");
                            //- 유사도가 0일경우 -> 다시말하라고함
                            //챗봇으로 다시 말해달라고 말함
                            //쿼리를 날려줄지 아니면 자체적으로 말풍선을 띄울지
                            activity.showTextView("해당하는 일정을 찾을 수 없습니다. 다시한번 말씀해 주시겠어요?", 10002);
                            finish = 1;
                        }

                        if (contain == 1) {//포함되는 일정이 1개 -> 그걸 말해줌
                            //Log.d("캔디", "contain1: " + contains[0]);
                        }

                        if (contain >= 2) {//포함되는 일정이 2개이상 -> 리스트로 선택하게함
                            activity.ttt = 5;
                            Log.d("캔디", "contain2: " + contains[0]);
                            Log.d("캔디", "contain2: " + contains[1]);
                            activity.mDatas.clear();
                            for (int i = 0; i < contain; i++) {
                                Log.d("캔디", "title: " + containsTitle[i]);
                                activity.addData(containsTitle[i]);////////////////////////
                            }
                            activity.listViewCheck = 1;
                            //activity.showList();
                            Log.d("캔디", "스케쥴1");
                            if (finish == 0) {
                                activity.showTextView("다음 일정들 중 어느 일정인가요?", 10002);
                            }
                            Log.d("캔디", "스케쥴2");
                            activity.listViewCheck = 0;
                            activity.finish = 1;
                            finish = 1;

                        }

                        if (finish == 0 && finalI2 == n - 1) {
                            store(mcb, databaseRef);
                            //activity.showTextView("일정을 성공적으로 수정했습니다.", 10002);
                            finish = 1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } catch (Exception e) {
                Log.d("scheduleSearch", "Date doesn't exist.");
            }
        }
    }

    public static void store(scheduleSearch3.MyCallback mcb, DatabaseReference databaseRef) {
        databaseRef.child(uid).child("Schedule").child(maxDate[0]).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    String obj = objSnapshot.getKey();
                    Log.d("마카롱", "obj: " + obj);

                    if (obj.equals(maxKey[0])) {
                        Log.d("마카롱", "obj: " + obj);
                        //if(f[0]==0) {
                        String title = objSnapshot.child("title").getValue(String.class);
                        String st_date = objSnapshot.child("start_date").getValue(String.class);

                        String ed_date = "";
                        if (objSnapshot.child("end_date").getValue(String.class) != null)
                            ed_date = objSnapshot.child("end_date").getValue(String.class);
                        String st_time = "";
                        String ed_time = "";
                        flag = 0;//시간할당일정인지아닌ㄴ지

                        if (objSnapshot.child("start_time").getValue(String.class) != null && objSnapshot.child("end_time").getValue(String.class) != null) {
                            st_time = objSnapshot.child("start_time").getValue(String.class);
                            Log.d("scheduleSearch", "start time: " + st_time);
                            ed_time = objSnapshot.child("end_time").getValue(String.class);
                            Log.d("scheduleSearch", "end time: " + ed_time);
                            flag = 1;
                        }
                        mcb.onCallback(maxKey[0], title, st_date,0);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        flag2 = 1;
    }

    public interface MyCallback {
        void onCallback(String value,String title, String st_date,int flag2);
        //void onCallback(String value);
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