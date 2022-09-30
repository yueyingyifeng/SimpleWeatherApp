package com.yyyf.weather;

import android.util.Log;

public class Day {
    String
    day,            //明天，后天，4天后，5天后，6天后，7天后
    date,           //日期 yyyy-mm-dd
    wind,           //风向 北风
    windspeed,      //风速 3-4级
    tem_max,        //最高温度 34
    tem_min,        //最低温度 22
    weather,        //天气
    img_weather;    //天气图标
    Day(int day,String date, String wind,String windspeed, String tem_max, String tem_min,String weather,String img_weather){
                switch (day){
                    case 1:
                        this.day = "明天";
                        break;
                    case 2:
                        this.day = "后天";
                        break;
                    case 3:
                        this.day = "4天后";
                        break;
                    case 4:
                        this.day = "5天后";
                        break;
                    case 5:
                        this.day = "6天后";
                        break;
                    case 6:
                        this.day = "7天后";
                        break;
                }
                this.date = date;
                this.wind = wind;
                this.windspeed = windspeed;
                this.tem_max = tem_max;
                this.tem_min = tem_min;
                this.weather = weather;
                this.img_weather = img_weather;
    }
    //通过字符串来选择正确的图片
    public int getImg_weather() {
        //这拼音是他们图片的名字设定的，和本开发者无关
        switch (img_weather){
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
                Log.e("Page", "getBackground: 获取到错误的资源:"+weather);
                return -1;
        }
    }
}
