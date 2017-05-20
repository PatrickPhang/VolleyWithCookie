package com.example.volleydemo;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public abstract class VolleyRequest {
    protected abstract void onMyResponse(String s);
    protected abstract void onMyErrorResponse(VolleyError volleyError);

    public Response.Listener getVolleyListener(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                onMyResponse(s);
            }
        };
        return listener;
    }

    public Response.ErrorListener getVolleyErrorListener(){
        Response.ErrorListener listener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onMyErrorResponse(volleyError);
            }
        };
        return listener;
    }
}
