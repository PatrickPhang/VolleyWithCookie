package com.example.volleydemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView webContent;
    private ImageView imageView;

    private void assignViews() {
        webContent = (TextView) findViewById(R.id.web_content);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        // 请求测试
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        VolleyHelper.displayImageWithCache(requestQueue,"https://xxx", imageView, R.mipmap.default_picture, R.mipmap.error_picture, VolleyHelper.DOUBLE_CACHE);
        VolleyHelper.displayImage(requestQueue,"ImageLoader","http://xxx", imageView, R.mipmap.default_picture,R.mipmap.error_picture,0,0, Bitmap.Config.RGB_565);

        // 网络请求显示在TextView上
        VolleyHelper.getRequest(requestQueue, "GitHub", "https://github.com/", new VolleyRequest() {
            @Override
            public void onMyResponse(String s) {
                webContent.setText(s);
            }

            @Override
            public void onMyErrorResponse(VolleyError volleyError) {
                Log.i("error：", volleyError.toString());
            }
        });

        // 获取Cookie的带参数post请求测试
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "xxx");
        map.put("pwd", "xxx");

        VolleyHelper.postRequestGetCookie(requestQueue, "Login", "http://115.159.188.200:8001/do_login/", map, new VolleyRequest() {
            @Override
            protected void onMyResponse(String s) {
                Log.i("noco",""+s);
            }

            @Override
            protected void onMyErrorResponse(VolleyError volleyError) {

            }
        }, new CookieInterface() {
            @Override
            public void onResposeCookie(String cookie) {    //不能进行UI更新
                Log.i("noco cookie",""+cookie);
                Utils.saveSettingNote(MainActivity.this, "cookie", cookie);

                //不带Cookie的get请求
                VolleyHelper.getRequest(requestQueue, "haha1", "http://www.zipcodeapi.com/rest/OjACA1TyoIpGRkIBimy0wR4o2W7wMz0BTBdtD712QrIHLqi3NQ3jI8HK5PhRoish/info.json/12345/degrees", new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco1", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco1", "" + volleyError);
                    }
                });

                //带Cookie的get请求
                VolleyHelper.getRequestWithCookie(requestQueue, "haha2", "http://115.159.188.200:8001/getPic/", Utils.getSettingNote(MainActivity.this, "cookie"), new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco2", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco2", "" + volleyError);
                    }
                });

                //不Cookie的且不带参数的post请求
                VolleyHelper.postRequest(requestQueue, "haha3", "http://115.159.188.200:8001/do_login/", new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco3", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco3", "" + volleyError);
                    }
                });

                // 带Cookie且不带参数post请求
                VolleyHelper.postRequestWithCookie(requestQueue, "haha4", "http://115.28.49.92:8082/port/get_banner_port.ashx", Utils.getSettingNote(MainActivity.this, "cookie"), new VolleyRequest() {
                    @Override
                    protected void onMyResponse(String s) {
                        Log.i("noco4", "" + s);
                    }

                    @Override
                    protected void onMyErrorResponse(VolleyError volleyError) {
                        Log.i("noco4", "" + volleyError);
                    }
                });
            }
        });
    }
}
