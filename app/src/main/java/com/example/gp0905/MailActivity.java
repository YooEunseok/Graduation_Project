package com.example.gp0905;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import static com.example.gp0905.LoginActivity.email;
import static com.example.gp0905.LoginActivity.name;

public class MailActivity extends AppCompatActivity {

    private EditText title = null;
    private EditText address = null;
    private EditText message1 = null;
    private EditText message2 = null;
    private EditText message3 = null;
    private String message = null;
    private Button button = null;
    private String password = null;
    private Button simple = null;
    private Button read_mail= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        title = (EditText) findViewById(R.id.title_mail); // 보내는 메일 제목
        address = (EditText) findViewById(R.id.u_mail); //받는 사람의 이메일
        message1 = (EditText) findViewById(R.id.message1); //본문 내용 머리말
        message2 = (EditText) findViewById(R.id.message2); //본문 내용 본문
        message3 = (EditText) findViewById(R.id.message3); //본문 내용 맺음말
        button = (Button) findViewById(R.id.button); // 전송 버튼
        simple = (Button) findViewById(R.id.simple); //간편 작성 기능 사용
        read_mail = (Button) findViewById(R.id.read_mail);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //전송버튼을 누르면
                final LinearLayout linear = (LinearLayout) View.inflate(MailActivity.this, R.layout.dialog_password, null);

                new AlertDialog.Builder(MailActivity.this)
                        .setView(linear)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText pwd = (EditText) linear.findViewById(R.id.passwordEditText);//비밀번호 입력받기
                                password = pwd.getText().toString();//String으로 변환

                                Log.d("TAG", "->" + password);
                                message = message1.getText().toString() + message2.getText().toString() + message3.getText().toString(); //머리말, 본문, 맺음말 모두 하나로 합침
                                SettingMail(email, password);//보내는 이메일 주소와 비밀번호를 넘겨준다
                                dialog.dismiss();//팝업 창 종료
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();//팝업창 종료
                            }
                        })
                        .show();

            }
        });

        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //간편 메일 버튼을 누르면
                final LinearLayout linear = (LinearLayout) View.inflate(MailActivity.this, R.layout.mail_information, null);

                new AlertDialog.Builder(MailActivity.this)
                        .setView(linear)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText prof = (EditText) linear.findViewById(R.id.professor); //교수님 이름
                                EditText clasn = (EditText) linear.findViewById(R.id.class_name); // 수업 이름
                                EditText dtls = (EditText) linear.findViewById(R.id.details); // 문의 내용

                                //EditText에서 받아온 내용들을 String으로 변환
                                String professor = prof.getText().toString();
                                String class_name = clasn.getText().toString();
                                String details = dtls.getText().toString();

                                WriteMail(professor, class_name, details); //EditText에 받은 내용+ 메일 탬플릿 입력

                                dialog.dismiss(); //팝업창 종료
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();//팝업창 종료
                            }
                        })
                        .show();//팝업창 띄우기

            }
        });

        read_mail.setOnClickListener((new View.OnClickListener(){

            @Override
            public void onClick(View v) { //메일 조회 액티비티 실행
                Intent intent = new Intent(getApplicationContext(), ReadMailActivity.class); //인텐트 화면 전환
                startActivity(intent);

            }
        }));

    }


    public void WriteMail(String professor, String class_name, String details) // 메일 전송을 위해 필요한 정보들을 세팅
    {
        ((EditText) findViewById(R.id.title_mail)).setText("["+class_name+"] 문의사항이 있습니다."); //제목 설정
        //((EditText) findViewById(R.id.u_mail)).setText(professor); //임시로 교수님 이름을 메일 주소에 추가하도록 해놨는데 나중에 메일연동으로 바꾸면 됨
        ((EditText) findViewById(R.id.message1)).setText("안녕하세요. "+professor+" 교수님 "+class_name+"을 듣고 있는 "+name+"입니다.\n"); //머리말
        //머리말에서의 name은 구글계정의 이름을 가져오는 방식 만약 구글 계정의 이름이 실명이 아니라 별명 등을 사용한 경우엔 직접 수정해야 함
        ((EditText) findViewById(R.id.message2)).setText("다름이 아니라 "+details+ "에 관해 문의 드리고 싶어 이렇게 메일을 보내게 되었습니다.\n"); //본문
        ((EditText) findViewById(R.id.message3)).setText("바쁘신 와중에 메일을 확인해 주셔서 감사합니다\n편하실 때 메일 확인 후 회신해주시면 감사하겠습니다.");//맺음말
    }

    //메일 전송자의 메일주소, 비밀번호를 받아 메일을 전송할 때 사용
    public void SettingMail(String email, String pwd){ //이메일은 파이어베이스 로그인을 통해 받아오고 비밀번호는 입력을 통해 받아옴
        try {
            GMailSender gMailSender = new GMailSender(email, pwd);

            //GMailSender.sendMail(제목, 본문내용, 받는사람);
            gMailSender.sendMail(title.getText().toString(), message, address.getText().toString());
            Toast.makeText(this.getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {//발송 이메일 주소가 잘못되었을 때
            Toast.makeText(this.getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {// 1. 보안설정이 잘못되었거나 2. 비밀번호 등 계정이 틀렸거나 3. 인터넷 네트워크가 끊겨 있거나
            Toast.makeText(this.getApplicationContext(), "인터넷 또는 구글 보안설정을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}