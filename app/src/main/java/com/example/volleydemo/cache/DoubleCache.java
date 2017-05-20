package com.example.volleydemo.cache;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

public class DoubleCache implements ImageLoader.ImageCache {
    private MemoryCache memoryCache;
    private DiskCache diskCache;
    public DoubleCache(){
        memoryCache = new MemoryCache();
        diskCache = new DiskCache();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = memoryCache.getBitmap(url);
        if (bitmap == null){
            bitmap = diskCache.getBitmap(url);
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        memoryCache.putBitmap(url, bitmap);
        diskCache.putBitmap(url,bitmap);
    }
}
