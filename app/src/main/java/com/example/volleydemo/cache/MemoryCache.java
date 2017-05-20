package com.example.volleydemo.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class MemoryCache implements ImageLoader.ImageCache {
    private LruCache<String,Bitmap> imageLruCache;

    public MemoryCache(){
        initCache();
    }

    private void initCache() {
        //获取当前运行的最大内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize  = maxMemory/4;//取四分之一作为图片缓存
        imageLruCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return imageLruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        imageLruCache.put(url,bitmap);
    }
}
