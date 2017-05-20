package com.example.volleydemo;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.volleydemo.cache.DiskCache;
import com.example.volleydemo.cache.DoubleCache;
import com.example.volleydemo.cache.MemoryCache;

import java.util.HashMap;
import java.util.Map;

public class VolleyHelper {
    public static final int MEMORY_CACHE = 0;   //缓存在内存中
    public static final int DISK_CACHE = 1;     //缓存在sd卡中
    public static final int DOUBLE_CACHE = 2;   //内存和sd双缓存

    public static void getRequest(RequestQueue requestQueue, String requestTag, String url, VolleyRequest volleyRequest){//不带Cookie的Get方式请求网络数据
        removeRequest(requestQueue,requestTag);
        StringRequest request = new StringRequest(Request.Method.GET,url,volleyRequest.getVolleyListener(),volleyRequest.getVolleyErrorListener());
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public static void getRequestWithCookie(RequestQueue requestQueue,String requestTag,String url,final String cookie,VolleyRequest volleyRequest){//带Cookie的Get方式请求网络数据
        removeRequest(requestQueue,requestTag);
        StringRequest request = new StringRequest(Request.Method.GET,url,volleyRequest.getVolleyListener(),volleyRequest.getVolleyErrorListener()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public static void postRequest(RequestQueue requestQueue,String requestTag,String url,final Map<String, String> params,VolleyRequest volleyRequest){//不获取Cookie也不带Cookie但是带参数的Post网络请求
        removeRequest(requestQueue,requestTag);
        StringRequest request = new StringRequest(Request.Method.POST,url,volleyRequest.getVolleyListener(),volleyRequest.getVolleyErrorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public static void postRequest(RequestQueue requestQueue,String requestTag,String url,VolleyRequest volleyRequest){//不获取Cookie也不带Cookie和参数的Post网络请求
        removeRequest(requestQueue,requestTag);
        StringRequest request = new StringRequest(Request.Method.POST,url,volleyRequest.getVolleyListener(),volleyRequest.getVolleyErrorListener());
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public static void postRequestGetCookie(RequestQueue requestQueue,String requestTag,String url,final Map<String, String> params,VolleyRequest volleyRequest,final CookieInterface cookieInterface){//带参数的获取Cookie的Post网络请求
        removeRequest(requestQueue,requestTag);
        StringRequest request = new StringRequest(Request.Method.POST,url,volleyRequest.getVolleyListener(),volleyRequest.getVolleyErrorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                String cookies = responseHeaders.get("Set-Cookie");

                cookieInterface.onResposeCookie(cookies);//设置回调，这里面不能更新UI

                return super.parseNetworkResponse(response);
            }
        };
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public static void postRequestWithCookie(RequestQueue requestQueue,String requestTag,String url,final Map<String, String> params,final String cookie,VolleyRequest volleyRequest){//带Cookie而且有参数的Post网络请求
        removeRequest(requestQueue,requestTag);
        StringRequest request = new StringRequest(Request.Method.POST,url,volleyRequest.getVolleyListener(),volleyRequest.getVolleyErrorListener()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setTag(requestTag);
        requestQueue.add(request);
    }
    public static void postRequestWithCookie(RequestQueue requestQueue,String requestTag,String url,final String cookie,VolleyRequest volleyRequest){//带Cookie但是没有参数的Post网络请求
        removeRequest(requestQueue,requestTag);
        StringRequest request = new StringRequest(Request.Method.POST,url,volleyRequest.getVolleyListener(),volleyRequest.getVolleyErrorListener()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        request.setTag(requestTag);
        requestQueue.add(request);
    }

    public static void removeRequest(RequestQueue requestQueue,String requestTag){//移除多余的请求
        requestQueue.cancelAll(requestTag);
    }

    /*requestQueue：请求队列
    imageTag：请求标记
    url：请求地址
    imageView：显示对象
    defaultImageResId：加载时默认显示的图片id
    errorImageResId：加载失败时显示的图片id
    imageViewWidth：图片压缩宽度
    imageViewHeiget：图片压缩高度
    decodeConfig：图片存储模式（有以下四种模式）

    Bitmap.Config ARGB_4444：每个像素占四位，即A=4，R=4，G=4，B=4，那么一个像素点占4+4+4+4=16位
    Bitmap.Config ARGB_8888：每个像素占四位，即A=8，R=8，G=8，B=8，那么一个像素点占8+8+8+8=32位
    Bitmap.Config RGB_565：每个像素占四位，即R=5，G=6，B=5，没有透明度，那么一个像素点占5+6+5=16位
    Bitmap.Config ALPHA_8：每个像素占四位，只有透明度，没有颜色。*/
    public static void displayImage(RequestQueue requestQueue,String imageTag,String url, final ImageView imageView,int defaultImageResId, final int errorImageResId,int imageViewWidth,int imageViewHeiget,Bitmap.Config decodeConfig){//没有缓存的图片加载模式
        removeRequest(requestQueue, imageTag);

        //显示加载中的图片
        imageView.setImageResource(defaultImageResId);

        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        //设置加载成功的图片
                        imageView.setImageBitmap(bitmap);
                    }
                },
                imageViewWidth, imageViewHeiget, decodeConfig,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //设置加载失败的图片
                        imageView.setImageResource(errorImageResId);
                    }
                });

        request.setTag(imageTag);
        requestQueue.add(request);
    }

    /*requestQueue：请求队列
    url：请求地址
    imageView：显示对象
    defaultImageResId：加载时默认显示的图片id
    errorImageResId：加载失败时显示的图片id
    cacheMode：缓存模式（内存缓存，SD缓存，内存SD双缓存）*/
    public static void displayImageWithCache(RequestQueue requestQueue,String url,ImageView imageView,int defaultImageResId,int errorImageResId,int cacheMode){//可以设置缓存的图片加载方式
        ImageLoader imageLoader;
        switch (cacheMode){
            case MEMORY_CACHE:
                imageLoader = new ImageLoader(requestQueue, new MemoryCache());
                break;
            case DISK_CACHE:
                imageLoader = new ImageLoader(requestQueue, new DiskCache());
                break;
            case DOUBLE_CACHE:
                imageLoader = new ImageLoader(requestQueue, new DoubleCache());
                break;
            default:
                imageLoader = new ImageLoader(requestQueue, new MemoryCache());
                break;
        }
        ImageLoader.ImageListener listener = imageLoader.getImageListener(imageView,defaultImageResId,errorImageResId);
        //加载及缓存网络图片
        imageLoader.get(url,listener);
    }
}
