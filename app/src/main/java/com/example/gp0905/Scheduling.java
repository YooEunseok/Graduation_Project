package com.example.gp0905;

import android.content.Context;


public class Scheduling {

    //각 요일별로 시간나누고 저장할 수 있도록 배열 선언 (오전8시 ~ 오후 10시까지)
    private String sunday[] = new String[15];
    private String monday[] = new String[15];
    private String tuesday[] = new String[15];
    private String wednesday[] = new String[15];
    private String thursday[] = new String[15];
    private String friday[] = new String[15];
    private String saturday[] = new String[15];

    public Scheduling() { //요일별 일정 저장할 수 있도록 선언
        for(int i=0;i<15;i++)
        {
            sunday[i]="";
            monday[i]="";
            tuesday[i]="";
            wednesday[i]="";
            thursday[i]="";
            friday[i]="";
            saturday[i]="";
        }
    }

    // 일정 요일별로 분류하고 저장
    public void addSchedule(String dateOfday, int s, int e, String title)
    {

        //요일별 일정 분류해서 해당 시간에 저장
        if(dateOfday.equals("일"))
        {
            for(int i=s-8;i<e-8;i++) {
                sunday[i] = title;
            }
        }
        else if(dateOfday.equals("월")) {
            for (int i = s - 8; i <e - 8; i++) {
                monday[i] = title;
            }
        }
        else if(dateOfday.equals("화"))
        {
            for(int i=s-8;i<e-8;i++) {
                tuesday[i] = title;
            }
        }
        else if(dateOfday.equals("수")) {
            for (int i = s - 8; i <= e - 8; i++) {
                wednesday[i] = title;
            }
        }
        else if(dateOfday.equals("목"))
        {
            for(int i=s-8;i<e-8;i++) {
                thursday[i] = title;
            }
        }
        else if(dateOfday.equals("금"))
        {
            for(int i=s-8;i<e-8;i++) {
                friday[i] = title;
            }
        }
        else if(dateOfday.equals("토"))
        {
            for(int i=s-8;i<e-8;i++) {
                saturday[i] = title;
            }
        }
    }

    //시간표 구성하는 함수
    public void setting(AutoResizeTextView[] sunday, AutoResizeTextView[] monday, AutoResizeTextView[] tuesday, AutoResizeTextView[] wednesday, AutoResizeTextView[] thursday,
                        AutoResizeTextView[] friday, AutoResizeTextView[] saturday, Context context)
    {
        int maxLength=0; // 가장 긴 스트링의 길이 저장
        String maxString=""; // 가장 긴 스트링 저장

        for(int i=0;i<15;i++){ //가장 긴 스트링, 길이 찾기
            if(this.sunday[i].length()>maxLength)
            {
                maxLength = this.sunday[i].length();
                maxString = this.sunday[i];
            }
            if(this.monday[i].length()>maxLength)
            {
                maxLength = this.monday[i].length();
                maxString = this.monday[i];
            }
            if(this.tuesday[i].length()>maxLength)
            {
                maxLength = this.tuesday[i].length();
                maxString = this.tuesday[i];
            }
            if(this.wednesday[i].length()>maxLength)
            {
                maxLength = this.wednesday[i].length();
                maxString = this.wednesday[i];
            }
            if(this.thursday[i].length()>maxLength)
            {
                maxLength = this.thursday[i].length();
                maxString = this.thursday[i];
            }
            if(this.friday[i].length()>maxLength)
            {
                maxLength = this.friday[i].length();
                maxString = this.friday[i];
            }
            if(this.saturday[i].length()>maxLength)
            {
                maxLength = this.saturday[i].length();
                maxString = this.saturday[i];
            }
        }

        for(int i = 0; i < 15; i++){
            if(!this.sunday[i].equals("")) //일정이 등록된 경우
            {
                sunday[i].setText(this.sunday[i]);
                sunday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else// 일정이 등록되지 않은 경우
            {//글자를 대충 채워넣어 크기를 맞춘다
                sunday[i].setText(maxString);
            }
            if(!this.monday[i].equals(""))
            {
                monday[i].setText(this.monday[i]);
                monday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else// 일정이 등록되지 않은 경우
            {//글자를 대충 채워넣어 크기를 맞춘다
                monday[i].setText(maxString);
            }
            if(!this.tuesday[i].equals(""))
            {
                tuesday[i].setText(this.tuesday[i]);
                tuesday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else// 일정이 등록되지 않은 경우
            {//글자를 대충 채워넣어 크기를 맞춘다
                tuesday[i].setText(maxString);
            }
            if(!this.wednesday[i].equals(""))
            {
                wednesday[i].setText(this.wednesday[i]);
                wednesday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else// 일정이 등록되지 않은 경우
            {//글자를 대충 채워넣어 크기를 맞춘다
                wednesday[i].setText(maxString);
            }
            if(!this.thursday[i].equals(""))
            {
                thursday[i].setText(this.thursday[i]);
                thursday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else// 일정이 등록되지 않은 경우
            {//글자를 대충 채워넣어 크기를 맞춘다
                thursday[i].setText(maxString);
            }
            if(!this.friday[i].equals(""))
            {
                friday[i].setText(this.friday[i]);
                friday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else// 일정이 등록되지 않은 경우
            {//글자를 대충 채워넣어 크기를 맞춘다
                friday[i].setText(maxString);
            }
            if(!this.saturday[i].equals(""))
            {
                saturday[i].setText(this.saturday[i]);
                saturday[i].setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else// 일정이 등록되지 않은 경우
            {//글자를 대충 채워넣어 크기를 맞춘다
                saturday[i].setText(maxString);
            }

            //글자 크기때문에 cell깨지는 것 방지하기 위해 크기 조정
            sunday[i].resizeText();
            monday[i].resizeText();
            tuesday[i].resizeText();
            wednesday[i].resizeText();
            thursday[i].resizeText();
            friday[i].resizeText();
            saturday[i].resizeText();

        }
    }


}
