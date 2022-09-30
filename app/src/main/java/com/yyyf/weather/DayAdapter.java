package com.yyyf.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class DayAdapter extends BaseAdapter {
    private LinkedList<Day> days;
    private Context context;

    public DayAdapter(LinkedList<Day> list,Context context){
        this.context = context;
        this.days = list;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        //--------------获取item里面的view-----------------------//
        TextView day = convertView.findViewById(R.id.item_day);
        TextView date = convertView.findViewById(R.id.item_date);
        TextView wind = convertView.findViewById(R.id.item_wind);
        TextView windspeed = convertView.findViewById(R.id.item_windspeed);

        TextView weather = convertView.findViewById(R.id.item_tv_weather);
        ImageView img_weather = convertView.findViewById(R.id.item_img_weather);
        TextView tem_max = convertView.findViewById(R.id.item_tem_max);
        TextView tem_min = convertView.findViewById(R.id.item_tem_min);
        //-----------------设置view的内容-----------------------//
        day.setText(days.get(position).day);
        date.setText(days.get(position).date);
        wind.setText(days.get(position).wind);
        windspeed.setText(days.get(position).windspeed);

        img_weather.setImageResource(days.get(position).getImg_weather());
        weather.setText(days.get(position).weather);
        tem_max.setText(days.get(position).tem_max);
        tem_min.setText(days.get(position).tem_min);

        return convertView;
    }
}
