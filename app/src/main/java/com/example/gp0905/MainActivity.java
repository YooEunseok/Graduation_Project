package com.example.gp0905;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.gp0905.scheduleSearch.contains;
import static com.example.gp0905.scheduleSearch.containsDate;
import static com.example.gp0905.scheduleSearch.containsDate2;
import static com.example.gp0905.scheduleSearch.containsTime;
import static com.example.gp0905.scheduleSearch.containsTime2;
import static com.example.gp0905.scheduleSearch.containsTitle;
import static com.example.gp0905.scheduleSearch.flag;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> mDatas = new ArrayList<String>();
    //ListView listview;
    ArrayAdapter adapter;
    private static final int USER = 10001;
    private static final int BOT = 10002;
    private int count;
    int listViewCheck;
    int ttt;
    static int finish=0;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    Button logout;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    Object ccc = null;

    public static String TAG = "MainActivity";
    private FirebaseFunctions mFunctions;
    private DatabaseReference databaseRef;


    private static LinearLayout chatLayout;
    private static EditText queryEditText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();//firebase auth 받기
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mFunctions = FirebaseFunctions.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        databaseRef = mDatabase.getReference("User_list");

        scheduleSearch.getInstance().setActivity(this);
        scheduleSearch3.getInstance().setActivity(this);

        fixedScheduleSearch.getInstance().setActivity(this);
        assignmentSearch.getInstance().setActivity(this);
        intentCheck.getInstance().setActivity(this);
        teamSearch.getInstance().setActivity(this);
        ChatbotTeamSchedule.getInstance().setActivity(this);


        final ScrollView scrollview = findViewById(R.id.chatScrollView);
        //채팅창 스크롤을 맨 아래로 가게 한다
        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        chatLayout = findViewById(R.id.chatLayout);

        ImageView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this::sendMessage);

        queryEditText = findViewById(R.id.queryEditText);
        queryEditText.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        sendMessage(sendBtn);
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMessage(View view) {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) { //사용자가 아무것도 입력하지 않았을 때
            Toast.makeText(MainActivity.this, "메세지를 입력하세요", Toast.LENGTH_LONG).show();
        } else { //입력했을 때
            showTextView(msg, USER);
            queryEditText.setText("");

            SendMessageToFirebaseFunctions("detectIntent", msg)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {

                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }
                        }

                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Task<Object> SendMessageToFirebaseFunctions(String functionName, String question) {
        // Create the arguments to the callable function.
        final Map<String, Object> data = new HashMap<>();
        data.put("question", question);
        data.put("push", true);
        data.put("ccc", ccc);
        data.put("flag", 0);

        return mFunctions
                .getHttpsCallable(functionName)
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.

                    Object result = task.getResult().getData();
                    Log.i(TAG, "Result: " + result);

                    Gson gson = new Gson();
                    String objJson = gson.toJson(result);

                    JSONObject json = null;
                    json = new JSONObject(objJson);
                    //JSONObject json1 = (JSONObject) json.getJSONArray("outputContexts").get(0);

                    String intent = (String) ((JSONObject) json.get("intent")).get("displayName");
                    Log.i("Hellooooo", "intent: " + intent);

                    JSONObject json1;
                    if (intent.equals("class_no_1 - custom")) {
                        json1 = (JSONObject) json.getJSONArray("outputContexts").get(3);
                        Log.i(TAG, String.valueOf(json1));
                    }
                    else if (intent.equals("assignment_finish_1")||intent.equals("assignment_finish_2")||intent.equals("assignment_finish_3")
                            ||intent.equals("Team_Scheduling_1")||intent.equals("Team_Scheduling_2")) {
                        json1 = json.getJSONObject("parameters");
                        Log.i(TAG, String.valueOf(json1));
                    }
                    else if (intent.equals("Team_Scheduling_3 - custom")||intent.equals("Team_Scheduling_4 - custom")
                            ||intent.equals("Team_Scheduling_5 - custom")||intent.equals("Team_Scheduling_5 - custom-2")) {
                        json1 = (JSONObject) json.getJSONArray("outputContexts").get(0);
                        json1 = json1.getJSONObject("parameters");
                        Log.i(TAG, String.valueOf(json1));
                    }
                    else {
                        json1 = (JSONObject) json.getJSONArray("outputContexts").get(0);
                        Log.i(TAG, String.valueOf(json1));
                    }

                    Log.i(TAG,"check1");

                    String response = (String) json.get("fulfillmentText");
                    Log.i(TAG,"check2");
                    ccc = json.get("outputContexts");
                    Log.i(TAG,"check3");







                    if (response != null) {
                        Log.i(TAG,"check4");

                        finish=0;
                        intentCheck.intentCheck(intent, json1, result, mDatabase, databaseRef);
                        Log.d("vvvv",finish+"");
                        Log.d("vvvv",intent);


                        if(intent.equals("schedule_modify - yes")||intent.equals("class_no") || intent.equals("class_no_1 - custom") || intent.equals("class_no_2 - no - custom")
                        ||intent.equals("assignment_finish_1")||intent.equals("assignment_finish_2")||intent.equals("assignment_finish_3")||
                                intent.equals("class_makeup") || intent.equals("class_makeup_1 - custom") || intent.equals("class_makeup_2 - custom")||
                                intent.equals("assignment_modify_1") || intent.equals("assignment_modify_2") || intent.equals("assignment_modify_3")||
                                intent.equals("schedule_modifyByTime")||intent.equals("schedule_modifyByTime - yes")||intent.equals("schedule_modify")||intent.equals("schedule_modify_day_time")||intent.equals("schedule_modify_day_time - yes")||intent.equals("schedule_remove")||
                                intent.equals("schedule_remove - yes")||  (intent.equals("Team_Scheduling_1")||intent.equals("Team_Scheduling_3 - custom")||intent.equals("Team_Scheduling_5 - custom-2")) ){

                            finish=1;
                        }



                        //process aiResponse here
                        if(finish==0){
                        showTextView(response, BOT);
                        }
                        Log.i("Hellooooo", "check: " + "ok");

                    } else {
                        showTextView("There was some communication issue. Please Try again!", BOT);
                    }


                    return result;
                });
    }

    public void addData(String title) {
        mDatas.add(title);
    }

    public void showList() {

        //listview.setVisibility(View.VISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showTextView(String message, int type) {
        FrameLayout layout;
        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                break;
            default:
                layout = getBotLayout();
                break;
        }

        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout);
        TextView tv = layout.findViewById(R.id.chatMsg);


        tv.setText(message);
        if (listViewCheck == 1) {
            ListView listview = layout.findViewById(R.id.listview);
            listview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    listview.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            Log.d("pppp", "메인액티비티2");
            Log.d("pppp", "메인액티비티3" + mDatas);
            listview.setVisibility(View.VISIBLE);
            adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, mDatas);
            listview.setAdapter(adapter);
            Log.d("pppp", "메인액티비티2");

            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, mDatas.get(position), Toast.LENGTH_SHORT).show();
                    //scheduleSearch.index=position;//선택된 인덱스 넘겨줌
                    Log.d("wwww", ""+ttt);
                    if(ttt==1){
                        scheduleSearch.mmm.onCallback(scheduleSearch.contains[position],scheduleSearch.containsTitle[position],
                                scheduleSearch.containsDate[position],scheduleSearch.containsDate2[position],
                                scheduleSearch.containsTime[position],scheduleSearch.containsTime2[position], flag,4);
                        ////////////이거 초기화 혹시 안됐는지 확인하기
                        //showTextView(scheduleSearch.containsTitle[position]+" "+scheduleSearch.containsDate[position]+" 성공적으로 수정했습니다.", 10002);
                    }
                    else if(ttt==5){
                        scheduleSearch3.mmm.onCallback(scheduleSearch3.contains[position],scheduleSearch3.containsTitle[position],
                                scheduleSearch3.containsDate[position],4);
                        ////////////이거 초기화 혹시 안됐는지 확인하기
                        //showTextView(scheduleSearch.containsTitle[position]+" "+scheduleSearch.containsDate[position]+" 성공적으로 수정했습니다.", 10002);
                    }
                    else if(ttt==6){
                        teamSearch.mmm.onCallback(teamSearch.contains[position],teamSearch.containsSubject[position],
                                teamSearch.containsMembers[position], teamSearch.containsMemberCount[position]);


                    }
                    else if(ttt==2){
                        assignmentSearch.mmm.onCallback(assignmentSearch.contains[position],assignmentSearch.containsName[position],
                                assignmentSearch.containsSubject[position],4);
                        showTextView("과제완료를 성공적으로 저장했습니다.", 10002);
                    }

                    else if(ttt==3){
                        Log.d("wwww", ""+fixedScheduleSearch.things[position]);
                        String selected_date="";
                        //여기서 포지션 과목이 무슨요일인지 알아서 ->알고있음
                        String[] dw=new String[8];
                        // 그 피리어드 안의 그 요일을 검색해줘야함
                        for (int k=0;k<7;k++){
                            String inputDate=fixedScheduleSearch.pppp[k];
                            for(int l=0;l<7;l++){
                                Log.d("pppp.toString()2", inputDate);
                            }

                            DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                            Date date_= null;
                            try {
                                date_ = dateFormat.parse(inputDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar calendar=Calendar.getInstance();
                            calendar.setTime(date_);

                            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                                case 2:
                                    dw[k] = "월요일";
                                    break;
                                case 3:
                                    dw[k] = "화요일";
                                    break;
                                case 4:
                                    dw[k] = "수요일";
                                    break;
                                case 5:
                                    dw[k] = "목요일";
                                    break;
                                case 6:
                                    dw[k] = "금요일";
                                    break;
                                case 7:
                                    dw[k] = "토요일";
                                    break;
                                case 1:
                                    dw[k] = "일요일";
                                    break;

                            }
                        }

                        for (int k=0;k<7;k++){
                            if(dw[k].equals(fixedScheduleSearch.thingsDay[position])){
                                Log.d("eeee", String.valueOf(k));
                                selected_date=fixedScheduleSearch.pppp[k];
                                Log.d("eeee",selected_date);
                            }
                        }

                        fixedScheduleSearch.mmm.onCallback(fixedScheduleSearch.things[position],
                                fixedScheduleSearch.thingsTitle[position],fixedScheduleSearch.thingsTime[position],
                                fixedScheduleSearch.thingsTime2[position],fixedScheduleSearch.thingsDay[position],selected_date);
                        showTextView("일정을 성공적으로 수정했습니다.", 10002);
                    }
                }
            };
            listview.setOnItemClickListener(listener);

        }
        layout.requestFocus();
        queryEditText.requestFocus(); // change focus back to edit text to continue typing
    }

    FrameLayout getUserLayout() { //사용자 메세지 레이아웃을 인플레이션
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    FrameLayout getBotLayout() { //챗봇 메세지 레이아웃을 인플레이션
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
    }


}