package com.yyyf.weather;

import okhttp3.OkHttpClient;
import okhttp3.Request;
//资料来源 https://www.jianshu.com/p/d006bc55bca9

//--------------网络请求工具类-----------------//
public class HttpUtil {
    //异步请求方法
    public static void sendRequestWithOkhttp(String address,okhttp3.Callback callback)
    {
        OkHttpClient client=new OkHttpClient();                         //创建OkhttpClient对象
        Request request=new Request.Builder().url(address).build();     //通过builde()创建Request对象，并且传入url
        client.newCall(request).enqueue(callback);                      //这里创建的是匿名Call对象，将请求放入，enqueue()是异步请求方式。
    }

//    //同步请求方法
//    public static String sendRequestWithOkhttp(String address)
//    {
//        OkHttpClient client=new OkHttpClient();
//        Request request = new Request.Builder().url(url) .build();
//        Response response= client.newCall(request).execute();
//        String message=response.body().string();
//        return message;
//    }
}
