package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class assignmentSearch {

    static MyCallback mmm;
    static int finish = 0;
    private static assignmentSearch instance = null;
    private static MainActivity activity = null;

    private static int count;

    static String uid = ResultActivity.uid;

    static int flag;
    static int flag2 = 0;
    static int contain = 0;
    final static String[] contains = new String[10];
    final static String[] containsSubject = new String[10];
    final static String[] containsName = new String[10];
    final static String[] containsDate = new String[10];
    final static String[] containsTime = new String[10];

    final static String[] max_subject_key = new String[1];
    final static String[] max_assignment = new String[1];
    final static String[] max_subject = new String[1];


    static double max_subject_distance = 0;
    final static String[] max_name_key = new String[1];
    static double max_name_distance = 0;

    public static assignmentSearch getInstance() {
        if (instance == null) {
            instance = new assignmentSearch();
        }
        return instance;
    }

    public void setActivity(MainActivity activity) {

        Log.d("assignmentSearch", "check");
        this.activity = activity;
        Log.d("assignmentSearch", "check");

    }

    public static void assignmentSearch(MyCallback mcb, FirebaseDatabase mDatabase, DatabaseReference databaseRef
            , String assignment, String lecture, int n) {
        mmm = mcb;

        final double[] maxValue = {0.0};
        final String[] targetSubject = new String[1];

        if (n == 1) {
            //과목 찾아주는거
            try {
                databaseRef.child(uid).child("Homework").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        contain = 0;

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            String obj = objSnapshot.getKey();

                            String name = objSnapshot.child("name").getValue(String.class);
                            Log.d("assignmentSearch", "name: " + name);
                            String subject = objSnapshot.child("subject").getValue(String.class);
                            Log.d("assignmentSearch", "subject: " + subject);

                            //먼저 과목 매개변수랑 과목명을 문자열 유사도 계산
                            double distance = similarity(lecture, subject);

                            Log.d("assignmentSearch", "distance: " + distance);
                            if (distance >= maxValue[0]) {
                                maxValue[0] = distance;
                                max_subject_distance = distance;
                                max_subject_key[0] = obj;
                                Log.d("assignmentSearch", "max_subject_distance: " + max_subject_distance);
                                Log.d("assignmentSearch", "max_subject_key[0]: " + max_subject_key[0]);
                                targetSubject[0] = subject;
                            }
                            count++;
                        }

                        if (max_subject_distance == 0.0) {
                            //Log.d("캔디", "유사도 0: ");
                            //- 유사도가 0일경우 -> 다시말하라고함
                            //챗봇으로 다시 말해달라고 말함
                            //쿼리를 날려줄지 아니면 자체적으로 말풍선을 띄울지
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } catch (Exception e) {
                Log.d("assignmentSearch", "Key doesn't exist.");
            }


            //과제명 찾아주는거
            maxValue[0] = 0.0;
            try {
                databaseRef.child(uid).child("Homework").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        contain = 0;

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {

                            String obj = objSnapshot.getKey();

                            if (targetSubject[0].equals(objSnapshot.child("subject").getValue(String.class))) {

                                String name = objSnapshot.child("name").getValue(String.class);
                                Log.d("assignmentSearch", "name: " + name);
                                String subject = objSnapshot.child("subject").getValue(String.class);
                                Log.d("assignmentSearch", "subject: " + subject);
                                String date = objSnapshot.child("due_date").getValue(String.class);
                                String time = objSnapshot.child("due_time").getValue(String.class);

                                //과제명 매개변수랑 과제명을 문자열 유사도 계산
                                double distance = similarity(assignment, name);

                                if (name.contains(assignment)) {
                                    contains[contain] = obj;
                                    containsName[contain] = name;
                                    containsSubject[contain] = subject;
                                    containsDate[contain] = date;
                                    containsTime[contain] = time;
                                    contain++;
                                    Log.d("assignmentSearch", "contain: " + contain);
                                }

                                Log.d("distance", String.valueOf(distance));
                                if (distance >= maxValue[0]) {
                                    maxValue[0] = distance;
                                    max_name_distance = distance;
                                    max_name_key[0] = obj;
                                    max_assignment[0]=name;
                                    max_subject[0]=subject;
                                    Log.d("Hellokitty", "max_name_key[0]: " + max_name_key[0]);
                                    Log.d("Hellokitty", "max_assignment[0]: " + max_assignment[0]);
                                    Log.d("Hellokitty", " max_subject[0]: " +  max_subject[0]);
                                    Log.d("Hellokitty", "max_name_distance: " + max_name_distance);
                                    Log.d("Hellokitty", "max_name_key[0]: " + max_subject_key[0]);
                                    targetSubject[0] = subject;
                                }
                                count++;
                            }

                        }

                        if (max_name_distance == 0.0) {
                            //Log.d("캔디", "유사도 0: ");
                            //- 유사도가 0일경우 -> 다시말하라고함
                            //챗봇으로 다시 말해달라고 말함
                            //쿼리를 날려줄지 아니면 자체적으로 말풍선을 띄울지
                        }

                        if (contain == 1) {//포함되는 과제명이 1개 -> 그걸 말해줌
                            Log.d("Hellokitty", "contain1: " + contains[0]);

finish=0;
                        }
                        if (contain >= 2) {//포함되는 과제명이 2개이상 -> 리스트로 선택하게함


                            Log.d("assignmentSearch", "contain2: " + contains[0]);
                            Log.d("assignmentSearch", "contain2: " + contains[1]);
                            activity.mDatas.clear();
                            for (int i = 0; i < contain; i++) {
                                Log.d("assignmentSearch", "name: " + containsName[i]);
                                activity.addData("[" + containsSubject[i] + "] " + containsName[i] +
                                        "  (" + containsDate[i] + "  " + containsTime[i] + ")");
                            }
                            activity.listViewCheck = 1;
                            activity.ttt = 2;
                            activity.showTextView("다음 과제들 중 어느 과제인가요?", 10002);
                            activity.listViewCheck = 0;
                            activity.finish = 1;
                            finish = 1;
                        }
                        if (finish == 0) {
                            Log.d("Hellokitty","store(mcb, databaseRef);");
                            store(mcb, databaseRef);
                            //activity.showTextView("과제 완료를 성공적으로 저장했습니다.", 10002);

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
        if (n == 2) {
            //과목 찾아주는거

            try {
                databaseRef.child(uid).child("Homework").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        contain = 0;

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            String obj = objSnapshot.getKey();

                            String name = objSnapshot.child("name").getValue(String.class);
                            Log.d("assignmentSearch", "name: " + name);
                            String subject = objSnapshot.child("subject").getValue(String.class);
                            Log.d("assignmentSearch", "subject: " + subject);

                            //먼저 과목 매개변수랑 과목명을 문자열 유사도 계산
                            double distance = similarity(lecture, subject);

                            Log.d("assignmentSearch", "distance: " + distance);
                            if (distance >= maxValue[0]) {
                                maxValue[0] = distance;
                                max_subject_distance = distance;
                                max_subject_key[0] = obj;
                                max_assignment[0]=name;
                                max_subject[0]=subject;
                                Log.d("assignmentSearch", "max_assignment[0]: " + max_assignment[0]);
                                Log.d("assignmentSearch", " max_subject[0]: " +  max_subject[0]);
                                Log.d("assignmentSearch", "max_subject_distance: " + max_subject_distance);
                                Log.d("assignmentSearch", "max_subject_key[0]: " + max_subject_key[0]);
                                targetSubject[0] = subject;
                            }
                            count++;
                        }

                        if (max_subject_distance == 0.0) {
                            //Log.d("캔디", "유사도 0: ");
                            //- 유사도가 0일경우 -> 다시말하라고함
                            //챗봇으로 다시 말해달라고 말함
                            //쿼리를 날려줄지 아니면 자체적으로 말풍선을 띄울지
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } catch (Exception e) {
                Log.d("assignmentSearch", "Key doesn't exist.");
            }


            //과제명 찾아주는거
            maxValue[0] = 0.0;
            try {
                databaseRef.child(uid).child("Homework").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        contain = 0;

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {

                            String obj = objSnapshot.getKey();

                            if (targetSubject[0].equals(objSnapshot.child("subject").getValue(String.class))) {

                                String name = objSnapshot.child("name").getValue(String.class);
                                Log.d("assignmentSearch", "name: " + name);
                                String subject = objSnapshot.child("subject").getValue(String.class);
                                Log.d("assignmentSearch", "subject: " + subject);
                                String date = objSnapshot.child("due_date").getValue(String.class);
                                String time = objSnapshot.child("due_time").getValue(String.class);

                                contains[contain] = obj;
                                containsName[contain] = name;
                                containsSubject[contain] = subject;
                                containsDate[contain] = date;
                                containsTime[contain] = time;
                                contain++;
                                Log.d("assignmentSearch", "contain: " + contain);

                                count++;
                            }
                        }

                        activity.mDatas.clear();
                        for (int i = 0; i < contain; i++) {
                            Log.d("assignmentSearch", "name: " + containsName[i]);
                            activity.addData("[" + containsSubject[i] + "] " + containsName[i] +
                                    "  (" + containsDate[i] + "  " + containsTime[i] + ")");
                        }
                        activity.listViewCheck = 1;
                        activity.ttt = 2;
                        activity.showTextView("다음 과제들 중 어느 과제인가요?", 10002);
                        activity.listViewCheck = 0;
                        activity.finish = 1;
                        finish = 1;

                        if (finish == 0) {
                            store(mcb, databaseRef);
                            //activity.showTextView("과제 완료를 성공적으로 저장했습니다.", 10002);

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

        if (n == 3) {
            maxValue[0] = 0.0;
            maxValue[0] = 0.0;
            try {
                databaseRef.child(uid).child("Homework").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        contain = 0;

                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {

                            String obj = objSnapshot.getKey();


                            String name = objSnapshot.child("name").getValue(String.class);
                            Log.d("assignmentSearch", "name: " + name);
                            String subject = objSnapshot.child("subject").getValue(String.class);
                            Log.d("assignmentSearch", "subject: " + subject);
                            String date = objSnapshot.child("due_date").getValue(String.class);
                            String time = objSnapshot.child("due_time").getValue(String.class);

                            //과제명 매개변수랑 과제명을 문자열 유사도 계산
                            double distance = similarity(assignment, name);

                            if (name.contains(assignment)) {
                                contains[contain] = obj;
                                containsName[contain] = name;
                                containsSubject[contain] = subject;
                                containsDate[contain] = date;
                                containsTime[contain] = time;
                                contain++;
                                Log.d("assignmentSearch", "contain: " + contain);
                            }

                            Log.d("distance", String.valueOf(distance));
                            if (distance >= maxValue[0]) {
                                maxValue[0] = distance;
                                max_name_distance = distance;
                                max_name_key[0] = obj;
                                max_assignment[0]=name;
                                max_subject[0]=subject;
                                Log.d("assignmentSearch", "max_name_distance: " + max_name_distance);
                                Log.d("assignmentSearch", "max_name_key[0]: " + max_name_key[0]);
                                Log.d("assignmentSearch", "max_assignment[0]: " + max_assignment[0]);
                                Log.d("assignmentSearch", " max_subject[0]: " +  max_subject[0]);
                                targetSubject[0] = subject;
                            }
                            count++;
                        }


                        if (max_name_distance == 0.0) {
                            //Log.d("캔디", "유사도 0: ");
                            //- 유사도가 0일경우 -> 다시말하라고함
                            //챗봇으로 다시 말해달라고 말함
                            //쿼리를 날려줄지 아니면 자체적으로 말풍선을 띄울지
                        }

                        if (contain == 1) {//포함되는 과제명이 1개 -> 그걸 말해줌
                            Log.d("assignmentSearch", "contain1: " + contains[0]);

                        }
                        if (contain >= 2) {//포함되는 과제명이 2개이상 -> 리스트로 선택하게함


                            Log.d("assignmentSearch", "contain2: " + contains[0]);
                            Log.d("assignmentSearch", "contain2: " + contains[1]);
                            activity.mDatas.clear();
                            for (int i = 0; i < contain; i++) {
                                Log.d("assignmentSearch", "name: " + containsName[i]);
                                activity.addData("[" + containsSubject[i] + "] " + containsName[i] +
                                        "  (" + containsDate[i] + "  " + containsTime[i] + ")");
                            }
                            activity.listViewCheck = 1;
                            activity.ttt = 2;


                            activity.showTextView("다음 과제들 중 어느 과제인가요?", 10002);
                            activity.listViewCheck = 0;
                            activity.finish = 1;
                            finish = 1;
                        }
                        if (finish == 0) {
                            store(mcb, databaseRef);
                            //activity.showTextView("과제 완료를 성공적으로 저장했습니다.", 10002);

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

    public interface MyCallback {
        void onCallback(String value,String assignment,String subject ,int flag);
    }

    public static void store(MyCallback mcb, DatabaseReference databaseRef) {
        databaseRef.child(uid).child("Homework").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    String obj = objSnapshot.getKey();

                    if (obj.equals(max_name_key[0])) {
                        Log.d("hellokitty",max_name_key[0]);
                        Log.d("hellokitty",max_assignment[0]);
                        Log.d("hellokitty",max_subject[0]);
                       // Log.d("hellokitty",max_assignment[0]);

                        mcb.onCallback(max_name_key[0],max_assignment[0],max_subject[0], flag);
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