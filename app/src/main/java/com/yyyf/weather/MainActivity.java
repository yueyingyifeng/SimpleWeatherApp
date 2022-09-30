package com.yyyf.weather;
/*
天气API:
今日：https://www.tianqiapi.com/free/day?appid=76299581&appsecret=tEid9VeD
七日：https://www.tianqiapi.com/free/week?appid=76299581&appsecret=tEid9VeD
 */

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

enum weather_img{
    sunday,
    rainday,
    cloud
}

public class MainActivity extends Activity {
    //----------一些常量------------//
    final String TAG = "Page";
    final String C = "°C";
    //----------今天的view----------//
    private LinearLayout background;

    private TextView tv_cityName;
    private TextView tv_lastUpdate;

    private TextView tv_max_tem;
    private TextView tv_min_tem;

    private ImageView img_weather;
    private TextView tv_weather;

    private TextView tv_wind;
    private TextView tv_windspeed;
    //---------其他天的view---------//
    List<Day> other_daysData;
    ListView other_days;
    DayAdapter other_daysAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //------------去掉标题栏-------------//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //-----------------获取今天的View-----------------//
        background = findViewById(R.id.background);

        tv_cityName = findViewById(R.id.cityname);
        tv_lastUpdate = findViewById(R.id.last_updata);


        tv_max_tem = findViewById(R.id.max_tem);
        tv_min_tem = findViewById(R.id.min_tem);

        img_weather= findViewById(R.id.img_weather);
        tv_weather = findViewById(R.id.tv_weather);

        tv_wind = findViewById(R.id.wind);
        tv_windspeed = findViewById(R.id.windspeed);
        //----------------获取其他天的View----------------//
        other_days = findViewById(R.id.other_days);

        HttpUtil.sendRequestWithOkhttp("https://www.tianqiapi.com/free/week?appid=76299581&appsecret=tEid9VeD", new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.w(TAG, "onFailure: 获取失败");
                Toast.makeText(MainActivity.this,"数据获取失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "onResponse: 获取成功");
                parseJsonWithJsonObject(response);
            }
        });
    }

    private void parseJsonWithJsonObject(Response response) throws IOException {
        //--------------获取全部的json数据--------------//
        String responseData=response.body().string();   //解析response的body部分并转成String
        Log.i(TAG, "response " + response);
        Log.i(TAG, "responseData" + responseData);
        JSONObject jsonObjectOfWeather = JSON.parseObject(responseData);    //将String转成json对象

        //因为网络等是在子线程执行，而Android因为线程安全不允许在子线程修改View的数据，所以只能通过handle发生msg来修改
        // 可msg只能由一个。。。
        // 等等，我可以把json数据都发到msg方法里头然后在msg里面修改，不是只能发送一个嘛，我全发过去让它哪去解析json去，哼
        Message msg = new Message();
        msg.obj = jsonObjectOfWeather;
        handler.sendMessage(msg);
        //hhhhh
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            JSONObject Weather = (JSONObject)msg.obj;   //hhhh
            //--------------获取头部信息并设置---------------//
            setCity((String)Weather.get("city"));                   //获取所在城市（通过IP自动定位)
            setLastUpdate((String)Weather.get("update_time"));      //获取最后一次更新时间
            //----------------获取每日天气信息--------------//
            JSONArray jsonArray = Weather.getJSONArray("data");//因为API提供的七日天气保存在了JSON数组里面，故需要用JSONArray来获取这个七日天气
            JSONObject today = (JSONObject)jsonArray.get(0);        //通过索引来获取特定日期的，0就是今天，1就是明天等等。。。

            //-----------------获取今天的天气---------------//
            background.setBackgroundResource(getBackground(today.getString("wea_img")));    //设置背景
            tv_wind.setText( today.getString("win"));                                       //设置风向
            tv_windspeed.setText(today.getString("win_speed"));                             //设置风速
            tv_max_tem.setText(today.getString("tem_day") + C);                             //设置最高温度
            tv_min_tem.setText(today.getString("tem_night")+ C);                            //设置最低温度
            img_weather.setImageResource(getImg_weather( today.getString("wea_img")));      //设置天气图标
            tv_weather.setText(today.getString("wea"));                                     //设置天气
            //----------------获取其他天的天气--------------//
            other_daysData = new LinkedList<Day>();
            Log.i(TAG, "handleMessage: 加载每日天气");
            for(int n = 1;n<7;n++){
                Log.i(TAG, "handleMessage: 加载每日天气,第" + n + "个");
                JSONObject otherDay = (JSONObject)jsonArray.get(n);

                other_daysData.add(
                        new Day(n,                                                               //设置明天后天。。。
                                otherDay.getString("date"),                                 //设置日期
                                otherDay.getString("win"),                                  //设置风向
                                otherDay.getString("win_speed"),                            //设置风速
                                otherDay.getString("tem_day" )+ C,                 //设置最高温度
                                otherDay.getString("tem_night")+ C,                //设置最低温度
                                otherDay.getString("wea"),                                  //设置天气
                                otherDay.getString("wea_img")                               //设置天气图标
                                ));
                other_daysAdapter = new DayAdapter((LinkedList)other_daysData,MainActivity.this);//放进适配器
                other_days.setAdapter(other_daysAdapter);                                                 //将适配器应用到list
            }
            Log.i(TAG, "handleMessage: 加载每日天气成功");
        }

    };

    private void setCity(String city){
        tv_cityName.setText(city);
    }
    private void setLastUpdate(String date){
        tv_lastUpdate.setText(date);
    }
    public void Exit(View view){
        finish();
    }

    private int getImg_weather(String weather) {
        //这拼音是他们图片的名字设定的，和本开发者无关
        switch (weather){
            case "xue":
                return R.drawable.xue;
            case "lei":
                return R.drawable.lei;
            case "shachen":
                return R.drawable.shachen;
            case "wu":
                return R.drawable.wu;
            case "bingbao":
                return R.drawable.bingbao;
            case "yun":
                return R.drawable.yun;
            case "yu":
                return R.drawable.yu;
            case "yin":
                return R.drawable.yin;
            case "qing":
                return R.drawable.qing;
            default:
                return -1;
        }
    }

    private int getBackground(String weather){
        switch (weather){
            case "xue":
            case "lei":
            case "bingbao":
            case "yu":
                return R.drawable.background_rainday;
            case "shachen":
            case "wu":
            case "yun":
            case "yin":
                return R.drawable.background_cloud;
            case "qing":
                return R.drawable.background_sunday;
            default:
                Log.e(TAG, "getBackground: 获取到错误的资源:"+weather);
                return -1;
        }
    }
}