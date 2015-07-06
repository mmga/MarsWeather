package com.mmga.marsweather;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MarsWeather extends Application {

    public static final String TAG = MarsWeather.class.getName() ;
    private RequestQueue mRequestQueue;
    private static MarsWeather mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized MarsWeather getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancel(){
        mRequestQueue.cancelAll(TAG);
    }


}
