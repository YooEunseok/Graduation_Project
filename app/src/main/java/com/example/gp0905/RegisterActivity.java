package com.example.gp0905;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;

    //지금 구글로그인을 사용중인데 혹시 나중에 자체 로그인 기능을 사용하게 될 경우 활용할 수 있을 것 같아서 남겨둠 지금은 따로 구현하지 않았고 틀만 만들어 놓은 상태
    //이후 사용 안하는 것으로 확실히 결정나면 삭제
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.majorSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.major, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}

