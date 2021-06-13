package com.example.gp0905;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Team_schedule extends AppCompatActivity {


    int i;
    int m;
    int num;
    int D1;
    int last_check=0;

    int d=0;
    int DDD=0;
    int dd0_index;

    int success_check1;
    int s_h_index;
    int s_m_index;

    int success_check2;
    int e_h_index;
    int e_m_index;

    int[] success_check3 = new int[3];
    int user_s_h_index;
    int user_s_m_index;
    int user_e_h_index;
    int user_e_m_index;
    public int duration_arr[][][] = new int[100][24][6];

    int save;
    int M;
    /*배열 사이즈 이후에 늘려야함! 테스팅을 위해 작게 잡아놨음*/
    String[] duration = new String[10];
    String[] members = new String[10];
    String[][] schedules = new String[15][6];
    String[][] copy_schedules = new String[15][6];


    String[] schedules_s_date = new String[15];
    String[] schedules_s_hour = new String[15];
    String[] schedules_s_min = new String[15];
    String[] schedules_e_date = new String[15];
    String[] schedules_e_hour = new String[15];
    String[] schedules_e_min = new String[15];

    int schedule_count;
    int sc;

    String[] S_date = new String[15];
    String[] E_date = new String[15];
    String[] T_time = new String[15];
    String[] S_time = new String[15];
    String[] E_time = new String[15];

    int date_index;

    // Edit text에서 가져온 값들
    final String[] s_date = new String[1];
    final String[] e_date = new String[1];
    final String[] t_time = new String[1];
    final String[] s_time = new String[1];
    final String[] e_time = new String[1];

    List<String> result = new ArrayList<String>();
    List<String> result2 = new ArrayList<String>();
    String copyResult[];
    String result_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_team_schedule);

        final FirebaseDatabase database;
        final DatabaseReference myRef;

        final EditText s_duration = (EditText)findViewById (R.id.s_duration);
        final EditText e_duration = (EditText)findViewById (R.id.e_duration);
        final EditText s_Time = (EditText)findViewById (R.id.s_time);
        final EditText e_Time = (EditText)findViewById (R.id.e_time);
        final EditText total_time = (EditText)findViewById (R.id.total_time);
        Button btn = (Button)findViewById (R.id.btn);
        Button block_btn = (Button)findViewById (R.id.block_btn);
        final Button result_btn = (Button)findViewById (R.id.result_btn);

        /*Team.java에서 과목명과 멤버수 받아옴*/
        Intent intent = getIntent ();
        String subject = intent.getExtras ().getString ("subject");
        final int m_count = intent.getExtras ().getInt ("count");
        Log.d("Test Count", String.valueOf (m_count));
        //int C = count;




        //final String[] members = new String[100];
        final String[] uid = new String[1];
        database = FirebaseDatabase.getInstance ();
        myRef = database.getReference ("User_list");



        members[0] = ResultActivity.uid;
        Toast.makeText (getApplicationContext (), members[0], Toast.LENGTH_SHORT).show ();

        myRef.child(ResultActivity.uid).child("Team").addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot : snapshot.getChildren ()) {
                    for (i=1; i<= m_count; i++) {
                        members[i] = objSnapshot.child ("member"+ i).getValue ().toString ();
                        //Toast.makeText (getApplicationContext (),"member" + i + " "+members[i],Toast.LENGTH_SHORT).show ();
                        Log.d("Test member: ", members[i]);
                        //c++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                s_date[0] = String.valueOf (s_duration.getText ());
                e_date[0] = String.valueOf (e_duration.getText ());
                t_time[0] = String.valueOf (total_time.getText ());
                s_time[0] = String.valueOf (s_Time.getText ());
                e_time[0] = String.valueOf (e_Time.getText ());
                num=0;

//                s_date[0] = "2020 11 29";
//                e_date[0] = "2020 11 30";
//                t_time[0] = "3:00";
//                s_time[0] = "19:00";
//                e_time[0] = "18:00";

                SimpleDateFormat beforeFormat = new SimpleDateFormat ("yyyy MM dd");
                //SimpleDateFormat afterFormat = new SimpleDateFormat ("yyyy mm dd");
                Date tempDate = null;
                Date tempDate2 = null;

                try {
                    tempDate = beforeFormat.parse (s_date[0]);
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                try {
                    tempDate2 = beforeFormat.parse (e_date[0]);
                } catch (ParseException e) {
                    e.printStackTrace ();
                }

                Calendar c1 = Calendar.getInstance ();
                Calendar c2 = Calendar.getInstance ();

                c1.setTime (tempDate);
                c2.setTime (tempDate2);
                int i = 0;
                D1=0;
                while (c1.compareTo (c2) != 1) {  // 기간 내 날짜들 구하기
                    //Toast.makeText (getApplicationContext (), c1.getTime (), Toast.LENGTH_SHORT).show ();
                    duration[i] = beforeFormat.format (c1.getTime ());
                    //Toast.makeText (getApplicationContext (), duration[i], Toast.LENGTH_SHORT).show ();
                    Log.d("Test duration ~" , duration[i]);
                    c1.add (Calendar.DATE, 1);
                    i++;
                    D1++;
                }

                /*일정 가져오기*/
//                int m = 0;
                m=0;
                //int end=0;
                schedule_count=0;
                //final String [][] copySchedules = new String[50][6];
                while (members[m] != null) {
                    d = 0;
                    Toast.makeText (getApplicationContext (), members[m], Toast.LENGTH_SHORT).show ();
                    while (duration[d] != null) {
                        String D = duration[d];
                        Log.d ("duration", D);
                        myRef.child (members[m]).child ("Schedule").child (D).addListenerForSingleValueEvent (new ValueEventListener () {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot objSnapshot : snapshot.getChildren ()) {
                                    if (objSnapshot.exists ()) {

                                        String obj = objSnapshot.getKey ();
                                        S_date[schedule_count] = objSnapshot.child ("start_date").getValue (String.class);
                                        S_time[schedule_count] = objSnapshot.child ("start_time").getValue (String.class);
                                        E_date[schedule_count]= objSnapshot.child ("end_date").getValue (String.class);
                                        E_time[schedule_count] = objSnapshot.child ("end_time").getValue (String.class);

                                        String[] S_TIME = S_time[schedule_count].split (":");
                                        schedules[schedule_count][0] = S_date[schedule_count];
                                        schedules[schedule_count][1] = S_TIME[0];
                                        schedules[schedule_count][2] = S_TIME[1];


                                        String[] E_TIME = E_time[schedule_count].split (":");
                                        schedules[schedule_count][3] = E_date[schedule_count];
                                        schedules[schedule_count][4] = E_TIME[0];
                                        schedules[schedule_count][5] = E_TIME[1];

                                        fill_schedule (schedules, schedule_count, m, m_count, t_time[0]);
                                        //Toast.makeText (getApplicationContext (), schedule_count + " s_count " + schedules[schedule_count][0], Toast.LENGTH_SHORT).show ();
                                        Log.d("Test s_count", schedules[schedule_count][0]);
                                        schedule_count++;


                                    } else {
                                        Log.d("Test s_count", "DD");
                                    }

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        d++;
                    }
                    m++;

                }
                if (s_date[0] != null){
                    Log.d("Test length", String.valueOf (schedule_count));
                    block_time();
                    before_start ();

                }
                result_btn.setEnabled(true);
            }


        });

        result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                copyResult = new String[result2.size()];
                for (int cR=0; cR<copyResult.length; cR++){
                    copyResult[cR] = result2.get(cR);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Team_schedule.this);

                builder.setTitle("추천 시간");
                builder.setItems(copyResult, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(Team_schedule.this, copyResult[id], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });



    }

    public void block_time() {
        int duration_len = 0;
        while (duration[duration_len] != null) {
            duration_len++;
        }
        Log.d ("Test duration0", String.valueOf (duration_len));

        for (int dl = 0; dl<duration_len; dl++) {
            // 0:00 ~ 9:00 block
            for(int dl_h0 =0; dl_h0<9; dl_h0++) {
                for(int dl_m0=0; dl_m0<6; dl_m0++) {
                    duration_arr[dl][dl_h0][dl_m0] = 1;
                }
            }

            // 18:00 ~ 23:59 block
            for(int dl_h1 =18; dl_h1<24; dl_h1++) {
                for(int dl_m1=0; dl_m1<6; dl_m1++) {
                    duration_arr[dl][dl_h1][dl_m1] = 1;
                }
            }
        }
        Log.d("Test block", Arrays.deepToString (duration_arr));

    }
    public void before_start(){
 /*
                0시부터 23시까지로 우선 고정
                분은 0~9, 10~19, 20~29, 30~39, 40~49, 50~59로 나눔
                기본 어레이 0으로 초기화
                사용 가능한 시간 - 0 , 사용 불가 시간 -1
                */
        // 사용자 지정 시간 바깥의 시간들에 대해 1로 변경

        String[] ss = s_time[0].split (":");
        Toast.makeText (getApplicationContext (), ss[0], Toast.LENGTH_SHORT).show ();
        // start 시간 인덱스
        s_h_index = Integer.parseInt (ss[0]);
        Toast.makeText (getApplicationContext (), "s_h " + s_h_index, Toast.LENGTH_SHORT).show ();

        // start 분 인덱스
        String ss1 = ss[1].substring (0,1);
        s_m_index = Integer.parseInt (ss1);
        success_check1 = 1;
        Toast.makeText (getApplicationContext (), "ss1 " + s_m_index, Toast.LENGTH_SHORT).show ();

        // 팀플 기간 이전 처리
        // 예를 들어 10시 50분인 경우 - 9시 59분까지 1로 표현
        if(success_check1 == 1) {
            for (int s0=0; s0 <s_h_index; s0++) { // 시간
                for (int s1=0; s1<6; s1++) { // 분
                    duration_arr[0][s0][s1] = 1;
                }
            }

            // 나머지 10시 0~50분에 대해 1로 표현
            for (int s2=0; s2 <= s_m_index; s2++) { // 시간
                duration_arr[0][s_h_index][s2] = 1;
            }
            Log.d("Test before", Arrays.deepToString (duration_arr));

            after_end();
        }
    }

    public void after_end(){
        String[] ee = e_time[0].split (":");

        // end 시간 인덱스
        e_h_index = Integer.parseInt (ee[0]);

        Log.d("Test e_h", ee[0]);
        // end 분 인덱스
        String ee1 = ee[1].substring (0,1);
        s_m_index = Integer.parseInt (ee[1]);
        success_check2 = 2;

        Log.d("Test ee1", ee1);


        Log.d("Test D1 ", String.valueOf (D1));
        if(success_check2 == 2) {
            // 팀플 기간 이후 처리
            // 예를 들어 10시 50분인 경우 - 11시부터 모두 1로 표현
            for (int e0=e_h_index+1; e0 <24; e0++) { // 시간
                for (int e1=0; e1<6; e1++) { // 분
                    duration_arr[D1-1][e0][e1] = 1;
                }
            }

            // 나머지 10시 50~59분에 대해 1로 표현
            for (int e1=e_m_index; e1 <6; e1++) { // 시간
                duration_arr[D1-1][e_h_index][e1] = 1;
            }

            Log.d("Test after", Arrays.deepToString (duration_arr));
            Log.d("Test schedule", String.valueOf (schedule_count));

        }
    }

    public void fill_schedule(String[][] Schedules, int Schedule_count, int member, int count, String t_time){

        num++;
        int S1 = 0;
        while(Schedules[S1][0] != null) { // 기간 2
            S1++;
        }
        Log.d ("Test2", String.valueOf (Schedules[0][0]));

        int du_len =0;
        while(duration[du_len] != null) { // 기간 2
            du_len++;
        }

        for (int du =0; du<du_len; du++) {
            if(duration[du].equals (Schedules[Schedule_count][0])){
                date_index = du;
                Log.d("Test ss ", String.valueOf (Schedule_count));
                Log.d("Test ddate ", duration[du]);
                break;
            }
        }
        Log.d("Test DATE INDEX ", String.valueOf (date_index));

        //start 시간 인덱스
        user_s_h_index = Integer.parseInt (Schedules[Schedule_count][1]);
        Log.d("Test user_s_h ", String.valueOf (user_s_h_index));
        // start 분 인덱스
        String user_ss1 = Schedules[Schedule_count][2].substring (0,1);
        user_s_m_index = Integer.parseInt (user_ss1);
        Log.d("Test user_s_m ", String.valueOf (user_s_m_index));

        //end 시간 인덱스
        user_e_h_index = Integer.parseInt (Schedules[Schedule_count][4]);
        Log.d("Test user_e_h ", String.valueOf (user_e_h_index));
        // end 분 인덱스
        String user_ee1 = Schedules[Schedule_count][5].substring (0,1);
        user_e_m_index = Integer.parseInt (user_ee1);
        Log.d("Test user_e_m ", String.valueOf (user_e_m_index));

        /*예를 들어 13:20 ~ 15:50 분까지 스케쥴이 이미 있는 경우
         * 우선 14:00 ~ 14:59 까지 1로 변경
         * 그리고 13:20 ~ 1로 변경
         * 마지막으로 ~15:50 1로 변경 */
        for (int h= user_s_h_index+1;  h<user_e_h_index; h++) {
            for (int m=0; m<6; m++) {
                duration_arr[date_index][h][m] = 1;
                success_check3[0] = 3;
            }
        }

        for (int m=user_s_m_index; m<6; m++) {
            duration_arr[date_index][user_s_h_index][m] = 1;
            success_check3[1] = 3;
        }

        for(int em = 0; em<user_e_m_index; em++) {
            duration_arr[date_index][user_e_h_index][em] = 1;
            success_check3[2] = 3;
        }

        Log.d("Test fill1", Arrays.deepToString (duration_arr));

        Log.d ("Test m", String.valueOf (member));
        Log.d ("Test c", String.valueOf (count));

        Log.d ("Test SC0", String.valueOf (sc));
        Log.d ("Test SC1", String.valueOf (schedule_count));

        if(success_check3[0] == 3 && success_check3[1] == 3 && success_check3[2] == 3) {
            try {
                Log.d ("Test extract", "IN");
                extract_time (t_time);
            } catch (ParseException e) {
                Log.d ("Test extract", "NOT IN");
                e.printStackTrace ();
            }
        }

    }
    public void extract_time(String t_time) throws ParseException {

        String base = "00:00";
        String user = t_time;

        SimpleDateFormat dateFormat = new SimpleDateFormat ("HH:mm");

        Date b_time = dateFormat.parse (base);
        Date u_time = dateFormat.parse (t_time);
        Log.d ("Test u_time", t_time);

        long diff = u_time.getTime () - b_time.getTime ();
        long min = diff / 60000; // 차이 계산 (분 단위로)

        int e = (int) (min / 10); // 배열의 0이 연달아 몇개 존재해야 하는지 알기 위해
        Log.d ("Test E ", String.valueOf (e));
        int duration_len = 0;
        while (duration[duration_len] != null) {
            duration_len++;
        }
        Log.d ("Test duration", String.valueOf (duration_len));

        int arr[][] = new int[5][24 * 6];
        for (int h = 0; h < duration_len; h++) { // 각 날짜에 대한 값들이 일차원으로 표현됨
            for (int i = 0; i < 24; i++) {
                for (int j = 0; j < 6; j++) {
                    arr[h][i * 6 + j] = duration_arr[h][i][j];
                }
            }
        }

        int[] arr_day = new int[1]; // 날짜
        int[] arr_loc = new int[1]; //
        int[] check = new int[1];
        int c=0;
        int sum=0;
        int aa;
        int bb;
        loopOut:
        for (aa = 0; aa < duration_len; aa++) {
            check[0] = 0;
            for (bb = 0; bb < 144-e; bb++) {
                if (arr[aa][bb] == 0) {
                    arr_day[0] = aa;
                    arr_loc[0] = bb;
                    Log.d ("Test suma: ", String.valueOf (aa));
                    Log.d ("Test sumb: ", String.valueOf (bb));
                    sum = bb+e;
                    Log.d ("Test sum: ", String.valueOf (sum));
                    for (c = bb; c < sum; c++) {
                        if (arr[aa][c] == 0) {
                            check[0]++;
                            if(check[0] == e)
                                break loopOut;
                        }
                        else{
                            check[0] = 0;
                            bb = c;
                            break;
                        }
                    }
                }
            }
        }


        Log.d ("Test check result ", String.valueOf (check[0]));
        if(check[0]  == e){
            Log.d ("Test ", String.valueOf (arr_loc[0]));
            String day = duration[arr_day[0]];
            String start_h = String.valueOf (arr_loc[0] / 6); // 시간
            String start_m = String.valueOf (arr_loc[0]%6 * 10); // 분
            Log.d("Test h", start_h );
            Log.d("Test m", start_m );

            String new_start_h = "0";
            String new_start_m = "0";

            if(start_h.length () == 1) {
                new_start_h = new_start_h.concat (start_h);
            } else {
                new_start_h = start_h;
            }

            if(start_m.length () == 1) {
                new_start_m = new_start_m.concat (start_m);
            }else {
                new_start_m = start_m;
            }
            Log.d("Test new_h", new_start_h );
            Log.d("Test new_m", new_start_m );


            Log.d("Test day", day + new_start_h  + new_start_m);
            String strDate0 = day.concat (new_start_h);
            String strDate1 = strDate0.concat (new_start_m);

            SimpleDateFormat date = new SimpleDateFormat ("yyyy MM ddHHmm");
            Date formatDate = date.parse (strDate1);

            SimpleDateFormat newDate = new SimpleDateFormat ("yyyy MM dd HH:mm");
            String a_start_time = newDate.format (formatDate); // 시작 타임
            Log.d("Test changed ", a_start_time );

            Calendar cal = Calendar.getInstance ();

            Date regDate = newDate.parse (a_start_time);
            cal.setTime (regDate);

            cal.add(Calendar.MINUTE, e*10);
            String a_end_time = newDate.format (cal.getTime ()); // 종료 타임


            result_date = a_start_time.concat(" ~ ");
            result_date = result_date.concat(a_end_time);

            if(!result2.contains(result_date)){
                result2.add(result_date);
            }

            last_check++;

            Log.d("Test time1", a_start_time);
            Log.d("Test time2", a_end_time);

            Log.d("Test last_check", String.valueOf (last_check));

        } else {
            Log.d("Test Result", "Cannot search");
        }
    }
}