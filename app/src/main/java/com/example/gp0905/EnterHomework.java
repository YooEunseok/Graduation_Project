package com.example.gp0905;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EnterHomework extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Object ccc = null;
    public static String newHwKey;
    private Context context;
    private FirebaseFunctions mFunctions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_homework);

        final EditText name = (EditText) findViewById(R.id.et_name);
        final EditText subject = (EditText) findViewById(R.id.et_subject);
        final TextView due_date = (TextView) findViewById(R.id.due_date);
        final TextView due_time = (TextView) findViewById(R.id.due_time);

        Button back = (Button) findViewById(R.id.btn_back);
        Button ok = (Button) findViewById(R.id.btn_ok);

        mAuth = FirebaseAuth.getInstance();
        mFunctions = FirebaseFunctions.getInstance();
        this.context = this;

        Calendar now = Calendar.getInstance();
        due_date.setText(now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH));
        due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, listener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }

            private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    due_date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                }
            };
        });

        due_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, listener, 23, 59, false);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    due_time.setText(hourOfDay + ":" + minute);
                }
            };
        });

        /*ok 버튼 클락 -> 입력한 정보 DB에 들어감*/
        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String db_name = name.getText().toString();
                String db_subject = subject.getText().toString();
                String db_date = due_date.getText().toString();
                String db_time = due_time.getText().toString();

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("User_list");

                newHwKey = myRef.child(ResultActivity.uid).child("Homework").child(db_date).push().getKey();

                myRef.child(ResultActivity.uid).child("Homework").child(newHwKey).child("name").setValue(db_name);
                myRef.child(ResultActivity.uid).child("Homework").child(newHwKey).child("subject").setValue(db_subject);
                myRef.child(ResultActivity.uid).child("Homework").child(newHwKey).child("due_date").setValue(db_date);
                myRef.child(ResultActivity.uid).child("Homework").child(newHwKey).child("due_time").setValue(db_time);
                //이거나중에 child를 db_date 말고 db_subject로 만드는게 나을듯
                //과목은 스피너로 하고

                String msg = db_name;

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

                Toast.makeText(getApplicationContext(), "OK button clicked", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Back button clicked", Toast.LENGTH_SHORT).show();
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
        data.put("flag", 3);

        return mFunctions
                .getHttpsCallable(functionName)
                .call(data)
                .continueWith(task -> {
                    Object result = task.getResult().getData();
                    return result;
                });
    }
}