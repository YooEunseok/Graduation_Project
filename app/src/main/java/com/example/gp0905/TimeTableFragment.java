package com.example.gp0905;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    int s,e;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;
    static boolean calledAlready = false;
    private int count;


    //FireStore 연결
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    final String TAG = "[TAG]";
    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    //TimeTableFragment 실행
    public static TimeTableFragment newInstance(String param1, String param2) {
        TimeTableFragment fragment = new TimeTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    //각 요일 시간별로 저장할 수 있도록 배열 선언
    private AutoResizeTextView sunday[] = new AutoResizeTextView[15];
    private AutoResizeTextView monday[] = new AutoResizeTextView[15];
    private AutoResizeTextView tuesday[] = new AutoResizeTextView[15];
    private AutoResizeTextView wednesday[] = new AutoResizeTextView[15];
    private AutoResizeTextView thursday[] = new AutoResizeTextView[15];
    private AutoResizeTextView friday[] = new AutoResizeTextView[15];
    private AutoResizeTextView saturday[] = new AutoResizeTextView[15];
    private Scheduling scheduling = new Scheduling();


    @Override // fragement가 생성될 때 실행
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        //배열과 인덱스 매칭 (모든 요일)
        sunday[0] = (AutoResizeTextView) getView().findViewById(R.id.sunday0);
        sunday[1] = (AutoResizeTextView) getView().findViewById(R.id.sunday1);
        sunday[2] = (AutoResizeTextView) getView().findViewById(R.id.sunday2);
        sunday[3] = (AutoResizeTextView) getView().findViewById(R.id.sunday3);
        sunday[4] = (AutoResizeTextView) getView().findViewById(R.id.sunday4);
        sunday[5] = (AutoResizeTextView) getView().findViewById(R.id.sunday5);
        sunday[6] = (AutoResizeTextView) getView().findViewById(R.id.sunday6);
        sunday[7] = (AutoResizeTextView) getView().findViewById(R.id.sunday7);
        sunday[8] = (AutoResizeTextView) getView().findViewById(R.id.sunday8);
        sunday[9] = (AutoResizeTextView) getView().findViewById(R.id.sunday9);
        sunday[10] = (AutoResizeTextView) getView().findViewById(R.id.sunday10);
        sunday[11] = (AutoResizeTextView) getView().findViewById(R.id.sunday11);
        sunday[12] = (AutoResizeTextView) getView().findViewById(R.id.sunday12);
        sunday[13] = (AutoResizeTextView) getView().findViewById(R.id.sunday13);
        sunday[14] = (AutoResizeTextView) getView().findViewById(R.id.sunday14);

        monday[0] = (AutoResizeTextView) getView().findViewById(R.id.monday0);
        monday[1] = (AutoResizeTextView) getView().findViewById(R.id.monday1);
        monday[2] = (AutoResizeTextView) getView().findViewById(R.id.monday2);
        monday[3] = (AutoResizeTextView) getView().findViewById(R.id.monday3);
        monday[4] = (AutoResizeTextView) getView().findViewById(R.id.monday4);
        monday[5] = (AutoResizeTextView) getView().findViewById(R.id.monday5);
        monday[6] = (AutoResizeTextView) getView().findViewById(R.id.monday6);
        monday[7] = (AutoResizeTextView) getView().findViewById(R.id.monday7);
        monday[8] = (AutoResizeTextView) getView().findViewById(R.id.monday8);
        monday[9] = (AutoResizeTextView) getView().findViewById(R.id.monday9);
        monday[10] = (AutoResizeTextView) getView().findViewById(R.id.monday10);
        monday[11] = (AutoResizeTextView) getView().findViewById(R.id.monday11);
        monday[12] = (AutoResizeTextView) getView().findViewById(R.id.monday12);
        monday[13] = (AutoResizeTextView) getView().findViewById(R.id.monday13);
        monday[14] = (AutoResizeTextView) getView().findViewById(R.id.monday14);

        tuesday[0] = (AutoResizeTextView) getView().findViewById(R.id.tuesday0);
        tuesday[1] = (AutoResizeTextView) getView().findViewById(R.id.tuesday1);
        tuesday[2] = (AutoResizeTextView) getView().findViewById(R.id.tuesday2);
        tuesday[3] = (AutoResizeTextView) getView().findViewById(R.id.tuesday3);
        tuesday[4] = (AutoResizeTextView) getView().findViewById(R.id.tuesday4);
        tuesday[5] = (AutoResizeTextView) getView().findViewById(R.id.tuesday5);
        tuesday[6] = (AutoResizeTextView) getView().findViewById(R.id.tuesday6);
        tuesday[7] = (AutoResizeTextView) getView().findViewById(R.id.tuesday7);
        tuesday[8] = (AutoResizeTextView) getView().findViewById(R.id.tuesday8);
        tuesday[9] = (AutoResizeTextView) getView().findViewById(R.id.tuesday9);
        tuesday[10] = (AutoResizeTextView) getView().findViewById(R.id.tuesday10);
        tuesday[11] = (AutoResizeTextView) getView().findViewById(R.id.tuesday11);
        tuesday[12] = (AutoResizeTextView) getView().findViewById(R.id.tuesday12);
        tuesday[13] = (AutoResizeTextView) getView().findViewById(R.id.tuesday13);
        tuesday[14] = (AutoResizeTextView) getView().findViewById(R.id.tuesday14);

        wednesday[0] = (AutoResizeTextView) getView().findViewById(R.id.wednesday0);
        wednesday[1] = (AutoResizeTextView) getView().findViewById(R.id.wednesday1);
        wednesday[2] = (AutoResizeTextView) getView().findViewById(R.id.wednesday2);
        wednesday[3] = (AutoResizeTextView) getView().findViewById(R.id.wednesday3);
        wednesday[4] = (AutoResizeTextView) getView().findViewById(R.id.wednesday4);
        wednesday[5] = (AutoResizeTextView) getView().findViewById(R.id.wednesday5);
        wednesday[6] = (AutoResizeTextView) getView().findViewById(R.id.wednesday6);
        wednesday[7] = (AutoResizeTextView) getView().findViewById(R.id.wednesday7);
        wednesday[8] = (AutoResizeTextView) getView().findViewById(R.id.wednesday8);
        wednesday[9] = (AutoResizeTextView) getView().findViewById(R.id.wednesday9);
        wednesday[10] = (AutoResizeTextView) getView().findViewById(R.id.wednesday10);
        wednesday[11] = (AutoResizeTextView) getView().findViewById(R.id.wednesday11);
        wednesday[12] = (AutoResizeTextView) getView().findViewById(R.id.wednesday12);
        wednesday[13] = (AutoResizeTextView) getView().findViewById(R.id.wednesday13);
        wednesday[14] = (AutoResizeTextView) getView().findViewById(R.id.wednesday14);

        thursday[0] = (AutoResizeTextView) getView().findViewById(R.id.thursday0);
        thursday[1] = (AutoResizeTextView) getView().findViewById(R.id.thursday1);
        thursday[2] = (AutoResizeTextView) getView().findViewById(R.id.thursday2);
        thursday[3] = (AutoResizeTextView) getView().findViewById(R.id.thursday3);
        thursday[4] = (AutoResizeTextView) getView().findViewById(R.id.thursday4);
        thursday[5] = (AutoResizeTextView) getView().findViewById(R.id.thursday5);
        thursday[6] = (AutoResizeTextView) getView().findViewById(R.id.thursday6);
        thursday[7] = (AutoResizeTextView) getView().findViewById(R.id.thursday7);
        thursday[8] = (AutoResizeTextView) getView().findViewById(R.id.thursday8);
        thursday[9] = (AutoResizeTextView) getView().findViewById(R.id.thursday9);
        thursday[10] = (AutoResizeTextView) getView().findViewById(R.id.thursday10);
        thursday[11] = (AutoResizeTextView) getView().findViewById(R.id.thursday11);
        thursday[12] = (AutoResizeTextView) getView().findViewById(R.id.thursday12);
        thursday[13] = (AutoResizeTextView) getView().findViewById(R.id.thursday13);
        thursday[14] = (AutoResizeTextView) getView().findViewById(R.id.thursday14);

        friday[0] = (AutoResizeTextView) getView().findViewById(R.id.friday0);
        friday[1] = (AutoResizeTextView) getView().findViewById(R.id.friday1);
        friday[2] = (AutoResizeTextView) getView().findViewById(R.id.friday2);
        friday[3] = (AutoResizeTextView) getView().findViewById(R.id.friday3);
        friday[4] = (AutoResizeTextView) getView().findViewById(R.id.friday4);
        friday[5] = (AutoResizeTextView) getView().findViewById(R.id.friday5);
        friday[6] = (AutoResizeTextView) getView().findViewById(R.id.friday6);
        friday[7] = (AutoResizeTextView) getView().findViewById(R.id.friday7);
        friday[8] = (AutoResizeTextView) getView().findViewById(R.id.friday8);
        friday[9] = (AutoResizeTextView) getView().findViewById(R.id.friday9);
        friday[10] = (AutoResizeTextView) getView().findViewById(R.id.friday10);
        friday[11] = (AutoResizeTextView) getView().findViewById(R.id.friday11);
        friday[12] = (AutoResizeTextView) getView().findViewById(R.id.friday12);
        friday[13] = (AutoResizeTextView) getView().findViewById(R.id.friday13);
        friday[14] = (AutoResizeTextView) getView().findViewById(R.id.friday14);

        saturday[0] = (AutoResizeTextView) getView().findViewById(R.id.saturday0);
        saturday[1] = (AutoResizeTextView) getView().findViewById(R.id.saturday1);
        saturday[2] = (AutoResizeTextView) getView().findViewById(R.id.saturday2);
        saturday[3] = (AutoResizeTextView) getView().findViewById(R.id.saturday3);
        saturday[4] = (AutoResizeTextView) getView().findViewById(R.id.saturday4);
        saturday[5] = (AutoResizeTextView) getView().findViewById(R.id.saturday5);
        saturday[6] = (AutoResizeTextView) getView().findViewById(R.id.saturday6);
        saturday[7] = (AutoResizeTextView) getView().findViewById(R.id.saturday7);
        saturday[8] = (AutoResizeTextView) getView().findViewById(R.id.saturday8);
        saturday[9] = (AutoResizeTextView) getView().findViewById(R.id.saturday9);
        saturday[10] = (AutoResizeTextView) getView().findViewById(R.id.saturday10);
        saturday[11] = (AutoResizeTextView) getView().findViewById(R.id.saturday11);
        saturday[12] = (AutoResizeTextView) getView().findViewById(R.id.saturday12);
        saturday[13] = (AutoResizeTextView) getView().findViewById(R.id.saturday13);
        saturday[14] = (AutoResizeTextView) getView().findViewById(R.id.saturday14);

        ReadData(); //FireStore에서 데이터 읽어오고 시간표 세팅

    }

    public void ReadData(){

        database = FirebaseDatabase.getInstance ();
        databaseRef = database.getReference ("User_list");

        final String uid_ = ResultActivity.uid;
        String Schedule_ = EnterSchedule.newScheduleKey;

        final String[] sKey = new String[10]; // Schedule Key
        databaseRef.child (uid_).child ("Schedule").child ("2020-11-09").addValueEventListener (new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             //   adapter.clear ();
                count=0;
                for (DataSnapshot objSnapshot : snapshot.getChildren ()){
                    String obj = objSnapshot.getKey ();
                    //Toast.makeText (getApplicationContext (), obj, Toast.LENGTH_SHORT).show ();
                    sKey[count] = obj;

                    String title = objSnapshot.child("title").getValue(String.class);
                    String st_date = objSnapshot.child("start_date").getValue(String.class);
                    String ed_date = " ";
                    if (objSnapshot.child("end_date").getValue(String.class) != null)
                        ed_date = objSnapshot.child("end_date").getValue(String.class);
                    String st_time = " ";
                    String ed_time = " ";
                    int flag = 0;//시간할당일정인지아닌ㄴ지

                    if (objSnapshot.child("start_time").getValue(String.class) != null && objSnapshot.child("end_time").getValue(String.class) != null) {
                        st_time = objSnapshot.child("start_time").getValue(String.class);
                        Log.d("scheduleSearch", "start time: " + st_time);
                        ed_time = objSnapshot.child("end_time").getValue(String.class);
                        Log.d("scheduleSearch", "end time: " + ed_time);
                        flag = 1;
                    }

                    if (flag==1){
                        s=9;
                        e=10;

                        scheduling.addSchedule("월",s, e,title); //일정 내용 요일에 저장
                        scheduling.setting(sunday, monday, tuesday, wednesday, thursday, friday, saturday, getContext()); // 시간표 세팅
                    }

                    count++;
                    //fileList.add(title);
                }
                //adapter.notifyDataSetChanged ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData()); //로그로 읽어온 데이터 나열
                                s= Integer.parseInt(String.valueOf(Math.round(document.getDouble("start_time")))); //시간 Double -> Int 변환
                                e= Integer.parseInt(String.valueOf(Math.round(document.getDouble("end_time")))); //시간 Double -> Int 변환

                                scheduling.addSchedule(document.getString("dateOfDay"),s, e,document.getString("title")); //일정 내용 요일에 저장
                                scheduling.setting(sunday, monday, tuesday, wednesday, thursday, friday, saturday, getContext()); // 시간표 세팅
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException()); //실패한 경우 로그로 알려줌
                        }
                    }
                });

 */

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_table, container, false);
    }
}