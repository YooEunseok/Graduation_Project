package com.example.gp0905;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnterFixedSchedule extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String newScheduleKey;
    private FirebaseFunctions mFunctions;
    Object ccc = null;

    private Context context;

    ArrayAdapter arrayAdapter;
    ArrayList arrayList = new ArrayList<>();

    Spinner spinner;
    private TextView startTv;
    private TextView endTv;

    String dayOfWeek;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_fixed_schedule);
        mFunctions = FirebaseFunctions.getInstance();

        this.context = this;

        final EditText et_title = (EditText) findViewById(R.id.title);
        final Button bt_back = (Button) findViewById(R.id.back_bt);
        final Button bt_ok = (Button) findViewById(R.id.ok_bt);
        startTv = findViewById(R.id.start_time);
        endTv = findViewById(R.id.end_time);
        spinner = (Spinner) findViewById(R.id.spinner);

        mAuth = FirebaseAuth.getInstance();
        ResultActivity.uid = mAuth.getCurrentUser().getUid();

        arrayList = new ArrayList<>();
        arrayList.add("월요일");
        arrayList.add("화요일");
        arrayList.add("수요일");
        arrayList.add("목요일");
        arrayList.add("금요일");
        arrayList.add("토요일");
        arrayList.add("일요일");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), arrayList.get(i) + "이 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();
                dayOfWeek = (String) arrayList.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, listener, 10, 00, false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startTv.setText(hourOfDay + ":" + minute);
                }
            };
        });
        endTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, listener, 13, 30, false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endTv.setText(hourOfDay + ":" + minute);
                }
            };
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String title = et_title.getText().toString();
                String st_time = startTv.getText().toString();
                String end_time = endTv.getText().toString();

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("User_list");

                newScheduleKey = myRef.child(ResultActivity.uid).child("FixedSchedule").child(dayOfWeek).push().getKey();
                myRef.child(ResultActivity.uid).child("FixedSchedule").child(dayOfWeek).child(newScheduleKey).child("title").setValue(title);
                myRef.child(ResultActivity.uid).child("FixedSchedule").child(dayOfWeek).child(newScheduleKey).child("start_time").setValue(st_time);
                myRef.child(ResultActivity.uid).child("FixedSchedule").child(dayOfWeek).child(newScheduleKey).child("end_time").setValue(end_time);
                myRef.child(ResultActivity.uid).child("FixedSchedule").child(dayOfWeek).child(newScheduleKey).child("day_of_week").setValue(dayOfWeek);

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

                finish();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Task<Object> SendMessageToFirebaseFunctions(String functionName, String question) {
        final Map<String, Object> data = new HashMap<>();
        data.put("question", question);
        data.put("push", true);
        data.put("ccc", ccc);
        data.put("flag", 1);

        return mFunctions
                .getHttpsCallable(functionName)
                .call(data)
                .continueWith(task -> {
                    Object result = task.getResult().getData();
                    return result;
                });
    }
}
