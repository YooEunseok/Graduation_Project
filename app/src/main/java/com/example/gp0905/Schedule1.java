package com.example.gp0905;

public class Schedule1 {
    String title;
    String location;
    String date;

    //메인화면에 게시하기 위해 정보를 저장해서 모든 정보를 저장하지 않았음 너무 많은 정보를 저장해서 보여주면 가독성이 떨어지기 때문에
    public Schedule1(String title, String location, String date) {
        this.title = title; // 일정 내용
        this.location = location; // 일정 장소
        this.date = date; // 일정 날짜
    }

    public String getTitle() {
        return title;
    } //일정의 내용을 반환

    public void setSchedule(String schedule) {
        this.title = schedule;
    } //별도로 사용하는 함수는 아님

    public String getLocation() {
        return location;
    } // 일정의 장소를 반환

    public void setDetails(String details) {
        this.location = details;
    }

    public String getDate() {
        return date;
    } //일정의 날짜 반환

    public void setDate(String date) {
        this.date = date;
    }
}
