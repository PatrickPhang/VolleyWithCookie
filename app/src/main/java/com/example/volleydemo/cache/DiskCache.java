package com.example.volleydemo.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.example.volleydemo.utils.MD5Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DiskCache implements ImageLoader.ImageCache {
    private String cacheDir = Environment.getExternalStorageDirectory().toString()+"/noco_image_cache/";

    public DiskCache(){
    }

    @Override
    public Bitmap getBitmap(String url) {
        return BitmapFactory.decodeFile(cacheDir+ MD5Util.MD5(url)+".jpeg");
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.i("px", "开始保存");
        File f = new File(cacheDir);
        if (!f.exists()){
            f.mkdir();
            Log.i("px", "创建路径成功");
        }
        Log.i("px", "已经存在路径");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir+MD5Util.MD5(url)+".jpeg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
                Log.i("px", "已经保存");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("px", "保存失败");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
