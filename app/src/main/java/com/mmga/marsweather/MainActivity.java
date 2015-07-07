package com.mmga.marsweather;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

//    private static final int _EXPAND = 1000;
//    private static final int _SHRINK = 1001;
    private static  int _EXPAND = 1;
    private TextView mTextDegree, mTextWeather, mTextError;
    private LinearLayout mLinearLayout;
    MarsWeather helper = MarsWeather.getInstance();
    final static String RECENT_API_ENDPOINT = "http://marsweather.ingenology.com/v1/latest/?format=json";
    final static String IMG_API_ENDPOINT = "http://cn.bing.com/HPImageArchive.aspx?format=js&n=1";
    private ImageView mImageVIew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextDegree = (TextView) findViewById(R.id.degrees);
        mTextWeather = (TextView) findViewById(R.id.weather);
        mTextError = (TextView) findViewById(R.id.error);
        mImageVIew = (ImageView) findViewById(R.id.main_img);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        mImageVIew.setOnClickListener(new View.OnClickListener() {
            DisplayMetrics metrics = new DisplayMetrics();
            int height = metrics.heightPixels;

            @Override
            public void onClick(View view) {

                if (_EXPAND == 1) {
                    mImageVIew.setClickable(false);
                    expandImg();
                    _EXPAND = 0;
                } else {
                    mImageVIew.setClickable(false);
                    shrink();
                    _EXPAND = 1;
                }
            }
        });

        loadBingImage();
        loadWeatherData();
    }

    private void loadWeatherData() {
        CustomJsonRequest request = new CustomJsonRequest(Request.Method.GET, RECENT_API_ENDPOINT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String minTemp, maxTemp, atmo;
                    int avgTemp;

                    response = response.getJSONObject("report");
                    Log.d(">>>>>>>>", "2");

                    minTemp = response.getString("min_temp");
                    minTemp = minTemp.substring(0, minTemp.indexOf("."));
                    maxTemp = response.getString("max_temp");
                    maxTemp = maxTemp.substring(0, maxTemp.indexOf("."));

                    avgTemp = (Integer.parseInt(minTemp) + Integer.parseInt(maxTemp)) / 2;

                    atmo = response.getString("atmo_opacity");
                    String temp = "" + minTemp + " ~ " + maxTemp;

                    Log.d(">>>>>>>>", "3");
                    Log.d(">>>>>>>", atmo);
                    Log.d(">>>>>>>", temp);


                    mTextDegree.setText(temp);
                    mTextWeather.setText(atmo);

                } catch (Exception e) {
                    Log.e(">>>>>>>>>>", e.getMessage(), e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtError(error);
            }
        });
        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
        Log.d(">>>>>>>>", "4");
    }


    private void txtError(Exception e) {
        mTextError.setVisibility(View.VISIBLE);
        e.printStackTrace();
    }

    private void loadBingImage() {
        CustomJsonRequest request = new CustomJsonRequest(Request.Method.GET,
                IMG_API_ENDPOINT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("images");
                    JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
                    String urlbase = jsonObject.getString("urlbase");
                    String imageUrl = "http://cn.bing.com" + urlbase + "_720x1280.jpg";
                    Log.d(">>>>>>>>", imageUrl);

                    loadImage(imageUrl);
                } catch (JSONException e) {
                    imageError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setPriority(Request.Priority.LOW);
        helper.add(request);
    }

    private void loadImage(String imageUrl) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                mImageVIew.setImageBitmap(bitmap);
                Log.d(">>>>>>>>", "6");
            }
        },
                720,
                1280,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageError(error);
                Toast.makeText(MainActivity.this, "读取错误", Toast.LENGTH_SHORT).show();
                Log.d(">>>>>>>>", "8");
            }
        });
        helper.add(imageRequest);
    }

    int mainColor = Color.parseColor("#FF5722");

    private void imageError(Exception e) {
        mImageVIew.setBackgroundColor(mainColor);
        e.printStackTrace();
    }


    private void expandImg() {
        mImageVIew.measure(View.MeasureSpec.makeMeasureSpec(mLinearLayout.getMeasuredWidth(), View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int end = mImageVIew.getMeasuredHeight();


        Animator animator = LayoutAnimator.ofHeight(mImageVIew, 0, end);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mImageVIew.setClickable(true);
            }

        });
    }

    private void shrink() {
        mImageVIew.measure(View.MeasureSpec.makeMeasureSpec(mLinearLayout.getMeasuredWidth(), View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int end = mImageVIew.getMeasuredHeight();

        Animator animator = LayoutAnimator.ofHeight(mImageVIew, end, 0);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mImageVIew.setClickable(true);
            }
        });

    }



}
