package com.zhy.changeskin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by zhy on 15/9/22.
 */
public class ResourceManager {
    private static final String DEFTYPE_DRAWABLE = "drawable";
    private static final String DEFTYPE_COLOR = "color";
    private Resources mResources;
    private String mPluginPackageName;
    private String mSuffix;

    private HashMap<String, Integer> mResIdxs = new HashMap<>();

    public ResourceManager(Resources res, String pluginPackageName, String suffix) {
        mResources = res;
        mPluginPackageName = pluginPackageName;

        if (suffix == null) {
            suffix = "";
        }
        mSuffix = suffix;
    }

    public Drawable getDrawableByName(String name) {
        name = appendSuffix(name);
        String sName = "d_" + name;

        try {
            int id;
            if(mResIdxs.containsKey(sName)){
                id = mResIdxs.get(sName);
            } else {
                id = mResources.getIdentifier(name, DEFTYPE_DRAWABLE, mPluginPackageName);
                if (id == 0) {
                    id = mResources.getIdentifier(name, DEFTYPE_COLOR, mPluginPackageName);
                }
                mResIdxs.put(sName, id);
            }

            return id == 0 ? null : mResources.getDrawable(id);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    public int getColor(String name) {
        try {
            name = appendSuffix(name);

            String sName = "c_" + name;
            int id;
            if(mResIdxs.containsKey(sName)){
                id = mResIdxs.get(sName);
            } else {
                id = mResources.getIdentifier(name, DEFTYPE_COLOR, mPluginPackageName);
                mResIdxs.put(sName, id);
            }

            return id == 0 ? -1 : mResources.getColor(id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ColorStateList getColorStateList(String name) {
        try {
            name = appendSuffix(name);

            String sName = "csl_" + name;
            int id;
            if(mResIdxs.containsKey(sName)){
                id = mResIdxs.get(sName);
            } else {
                id = mResources.getIdentifier(name, DEFTYPE_COLOR, mPluginPackageName);
                mResIdxs.put(sName, id);
            }

            return id == 0 ? null : mResources.getColorStateList(id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String appendSuffix(String name) {
        if (!TextUtils.isEmpty(mSuffix))
            return name + "_" + mSuffix;
        return name;
    }

}
