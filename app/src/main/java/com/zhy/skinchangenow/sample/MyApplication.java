package com.zhy.skinchangenow.sample;

import android.app.Application;

import com.zhy.changeskin.SkinManager;
import com.zhy.skinchangenow.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


/**
 * Created by zhy on 15/9/22.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        SkinManager.getInstance().init(this);



        final File themeFile = new File(getCacheDir(), "skin1");
        if (!themeFile.exists()) {
            InputStream is = getResources().openRawResource(R.raw.skin1);
            try {
                FileOutputStream fos = new FileOutputStream(themeFile, false);

                byte[] buf = new byte[1024];
                int len;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
            } catch (Exception e) {
                // null
            }
        }
    }
}
