package com.example.gp0905;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

public class teamSearch {

    static String uid = ResultActivity.uid;
    private static teamSearch instance = null;
    private static MainActivity activity = null;
    static MyCallback mmm;
    static int finish = 0;

    static int flag;
    static int flag2 = 0;

    static int contain = 0;
    final static String[] contains = new String[10];
    final static String[] containsSubject = new String[10];
    final static String[][] containsMembers = new String[10][10];
    final static int[] containsMemberCount = new int[10];

    final static String[] maxKey = new String[1];
    final static String[] maxDate = new String[1];
    static double max_distance = 0;

    public static teamSearch getInstance() {
        if (instance == null) {
            instance = new teamSearch();
        }
        return instance;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public static void teamSearch(teamSearch.MyCallback mcb, FirebaseDatabase mDatabase, DatabaseReference databaseRef
            , String[] date, String str, int n) { //str==subject

        contain = 0;
        finish = 0;
        mmm = mcb;

        max_distance = 0;
        maxKey[0] = null;
        maxDate[0] = null;

        Log.d("teamSearch", "n: " + n);
        for (int i = 0; i < n; i++) {
            Log.d("teamSearch", "date[i]: " + date[i]);
        }
        // 일정짜줄때 넘겨줄 애

        final double[] maxValue = {0.0};

        databaseRef.child(uid).child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    String obj = objSnapshot.getKey();
                    Log.d("teamSearch", obj);
                    String subject = objSnapshot.child("subject").getValue(String.class);
                    Log.d("teamSearch", subject);
                    int c = objSnapshot.child("count").getValue(int.class);
                    Log.d("teamSearch", c + "");
                    String member;
                    for (int i = 1; i <= c; i++) {
                        member = objSnapshot.child("member" + i).getValue(String.class);
                        Log.d("teamSearch", "member" + i + ": " + member);
                    }

                    double distance = similarity(str, subject); //매개변수랑 일정명 유사도 계산

                    if (subject.contains(str)) {
                        contains[contain] = obj;
                        containsSubject[contain] = subject;
                        for (int i = 0; i < c; i++) {
                            member = objSnapshot.child("member" + c).getValue(String.class);
                            containsMembers[contain][i] = member;
                        }
                        containsMemberCount[contain] = c;
                        contain++;
                        Log.d("pppp", "contain: " + subject);
                        Log.d("pppp", "contain: " + contain);
                    }

                    Log.d("distance", String.valueOf(distance));
                    if (distance >= maxValue[0]) {
                        maxValue[0] = distance;
                        max_distance = distance;
                        maxKey[0] = obj;
                        Log.d("캔디1", "맥스키: " + maxKey[0]);
                    }
                }
                Log.d("캔디2", "맥스키: " + maxKey[0]);
                Log.d("캔디2", "맥스디스턴스: " + max_distance);

                if (max_distance == 0) {
                    activity.showTextView("해당하는 과목의 팀플을 찾을 수 없습니다. 다시한번 말씀해 주시겠어요?", 10002);
                    finish = 1;
                }

                if (contain == 1) {//포함되는 일정이 1개 -> 그걸 말해줌
                    Log.d("캔디", "contain1: " + contains[0]);
                }

                if (contain >= 2) {//포함되는 일정이 2개이상 -> 리스트로 선택하게함
                    activity.ttt = 1;
                    Log.d("캔디", "contain2: " + contains[0]);
                    Log.d("캔디", "contain2: " + contains[1]);
                    activity.mDatas.clear();
                    for (int i = 0; i < contain; i++) {
                        Log.d("캔디", "subject: " + containsSubject[i]);
                        activity.addData(containsSubject[i]);////////////////////////
                    }
                    activity.listViewCheck = 1;
                    if (finish == 0) {
                        activity.showTextView("다음 중 어느 과목의 팀플인가요?", 10002);
                    }
                    activity.listViewCheck = 0;
                    activity.finish = 1;
                    finish = 1;

                }

                if (finish == 0) {
                    store(mcb, databaseRef);
                    finish = 1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public interface MyCallback {
        void onCallback(String value, String subject, String[] member, int count);
    }

    public static void store(teamSearch.MyCallback mcb, DatabaseReference databaseRef) {
        databaseRef.child(uid).child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("teamSearch","store!");

                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    String obj = objSnapshot.getKey();
                    Log.d("teamSearch", obj);
                    if (obj.equals(maxKey[0])) {
                        String subject = objSnapshot.child("subject").getValue(String.class);
                        Log.d("teamSearch", subject);
                        int c = objSnapshot.child("count").getValue(int.class);
                        Log.d("teamSearch", c + "");
                        String[] member=new String[10];
                        for (int i = 1; i <= c; i++) {
                            member[i] = objSnapshot.child("member" + i).getValue(String.class);
                            Log.d("teamSearch", "member" + i + ": " + member[i]);
                        }
                        mcb.onCallback(maxKey[0], subject, member, c);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
