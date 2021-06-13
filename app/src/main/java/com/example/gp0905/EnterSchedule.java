package com.example.gp0905;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EnterSchedule extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String newScheduleKey;
    private FirebaseFunctions mFunctions;
    Object ccc = null;
    int c;
    private Context context;

    private TextView startTv;
    private TextView endTv;
    private TextView startTv1;
    private TextView endTv1;
    private Schedule schedule;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_enter_schedule);

        mFunctions = FirebaseFunctions.getInstance();
        this.context = this;

        final EditText et_title = (EditText) findViewById (R.id.title);

        final Button bt_back = (Button) findViewById (R.id.back_bt);
        final Button bt_ok = (Button) findViewById (R.id.ok_bt);
        startTv = findViewById(R.id.start_time);
        endTv = findViewById(R.id.end_time);
        startTv1 = findViewById(R.id.start_date);
        endTv1 = findViewById(R.id.end_date);
        schedule = new Schedule();
        schedule.setStartTime(new Time(10,0));
        schedule.setEndTime(new Time(13,30));

        mAuth = FirebaseAuth.getInstance();
        //ResultActivity.uid  = mAuth.getCurrentUser ().getUid ();
        Log.d("왜왜왜",ResultActivity.uid);
        ResultActivity.uid="4YCp1lYaHRhznJTkH5mxYdPCt7z2";
        Intent intent = getIntent ();

        final String[] date = {intent.getExtras ().getString ("st_date")};
//        String d = date[0] + " 9 : 00";

        Calendar now = Calendar.getInstance();
        startTv1.setText(now.get(Calendar.YEAR)+"-"+ now.get(Calendar.MONTH)+"-"+now.get(Calendar.DAY_OF_MONTH));
        endTv1.setText(now.get(Calendar.YEAR)+"-"+ now.get(Calendar.MONTH)+"-"+now.get(Calendar.DAY_OF_MONTH));

        startTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context,listener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }

            private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view,int year, int monthOfYear, int dayOfMonth) {
                    startTv1.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                }
            };
        });
        endTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context,listener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }

            private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view,int year, int monthOfYear, int dayOfMonth) {
                    endTv1.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                }
            };
        });

        RadioGroup radioGroup = (RadioGroup) findViewById (R.id.radioGroup);
        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener,schedule.getStartTime().getHour(), schedule.getStartTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startTv.setText(hourOfDay + ":" + minute);
                    schedule.getStartTime().setHour(hourOfDay);
                    schedule.getStartTime().setMinute(minute);
                }
            };
        });
        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context,listener,schedule.getEndTime().getHour(), schedule.getEndTime().getMinute(), false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endTv.setText(hourOfDay + ":" + minute);
                    schedule.getEndTime().setHour(hourOfDay);
                    schedule.getEndTime().setMinute(minute);
                }
            };
        });

        radioGroup.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {


            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton select = (RadioButton) findViewById (i);

                if (i == R.id.rg_btn1) {// 시간
                    startTv.setVisibility (View.VISIBLE);
                    endTv.setVisibility (View.VISIBLE);

                    Toast.makeText (getApplicationContext (), "Clicked btn1", Toast.LENGTH_SHORT).show ();
                    date[0] = intent.getExtras ().getString ("st_date");

                    Toast.makeText (getApplicationContext (), date[0], Toast.LENGTH_SHORT).show ();


                    c=0;

                }
                else if (i == R.id.rg_btn2) {// 하루종일
                    startTv.setVisibility (View.GONE);
                    endTv.setVisibility (View.GONE);

                    Toast.makeText (getApplicationContext (), "Clicked btn2", Toast.LENGTH_SHORT).show ();
                    final String date = intent.getExtras ().getString ("st_date");

                    c=1;

                }
            }


        });

        bt_back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText (getApplicationContext (), "Back button clicked", Toast.LENGTH_SHORT).show ();
                finish ();
            }
        });

        final Map<String, Object> Calendar = new HashMap<> ();

        bt_ok.setOnClickListener (new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String title = et_title.getText ().toString ();
                String st_date = startTv1.getText ().toString ();
                String end_date = endTv1.getText ().toString ();
                String st_time = startTv.getText ().toString ();
                String end_time = endTv.getText ().toString ();


                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("User_list");

                newScheduleKey = myRef.child(ResultActivity.uid).child ("Schedule").child(st_date).push ().getKey ();
                myRef.child(ResultActivity.uid).child ("Schedule").child(st_date).child (newScheduleKey).child("title").setValue(title);
                myRef.child(ResultActivity.uid).child ("Schedule").child(st_date).child (newScheduleKey).child("start_date").setValue(st_date);
                myRef.child(ResultActivity.uid).child ("Schedule").child(st_date).child (newScheduleKey).child("end_date").setValue(end_date);

                if(c==0) {
                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date).child (newScheduleKey).child ("start_time").setValue (st_time);
                    myRef.child (ResultActivity.uid).child ("Schedule").child (st_date).child (newScheduleKey).child ("end_time").setValue (end_time);
                }


                Toast.makeText (getApplicationContext (), "OK button clicked", Toast.LENGTH_SHORT).show ();

                String msg = title;

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

                finish ();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Task<Object> SendMessageToFirebaseFunctions(String functionName, String question) {
        // Create the arguments to the callable function.
        final Map<String, Object> data = new HashMap<>();
        data.put("question", question);
        data.put("push", true);
        data.put("ccc", ccc);
        data.put("flag", 1);

        return mFunctions
                .getHttpsCallable(functionName)
                .call(data)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.

                    Object result = task.getResult().getData();

                    //Gson gson = new Gson();
                    //String objJson = gson.toJson(result);

                    //JSONObject json = null;
                    //json = new JSONObject(objJson);
                    //JSONObject json1 = (JSONObject) json.getJSONArray("outputContexts").get(0);


                    // String response = (String) json.get("fulfillmentText");
                    //ccc = json.get("outputContexts");

                    //String intent = (String) ((JSONObject) json.get("intent")).get("displayName");

                    //if (response != null) {
                    //process aiResponse here
                    //} else {
                    //}


                    return result;
                });
    }
}

