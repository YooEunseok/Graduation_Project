package com.example.gp0905;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ScheduleListAdapter extends BaseAdapter {

    private Context context;
    private List<Schedule1> scheduleList;

    //메인화면 게시위해 정보를 리스트에 저장
    public ScheduleListAdapter(Context context, List<Schedule1> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }


    @Override
    public int getCount() {
        return scheduleList.size();
    } //게시해야 하는 일정의 갯수

    @Override
    public Object getItem(int i) {
        return scheduleList.get(i);
    } //일정 정보를 받는다

    @Override
    public long getItemId(int i) {
        return i;
    } //일정의 순번

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.schedule,null);
        TextView titleText = (TextView) v.findViewById(R.id.TitleText);
        TextView locationText = (TextView) v.findViewById(R.id.LocationText);
        TextView dateText = (TextView) v.findViewById(R.id.dateText);

        //Schedulelist에 각 정보들 저장
        titleText.setText(scheduleList.get(i).getTitle());
        locationText.setText(scheduleList.get(i).getLocation());
        dateText.setText(scheduleList.get(i).getDate());

        v.setTag(scheduleList.get(i).getTitle());
        return v;
    }

}
