package com.zhy.changeskin.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.zhy.changeskin.SkinManager;
import com.zhy.changeskin.attr.SkinAttr;
import com.zhy.changeskin.attr.SkinAttrSupport;
import com.zhy.changeskin.attr.SkinAttrType;
import com.zhy.changeskin.attr.SkinView;
import com.zhy.changeskin.callback.ISkinChangedListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on 15/9/22.
 */
public class BaseSkinActivity extends AppCompatActivity implements ISkinChangedListener, LayoutInflaterFactory {
    static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();
    private final Object[] mConstructorArgs = new Object[2];
    private static Method sCreateViewMethod;
    static final Class<?>[] sCreateViewSignature = new Class[]{View.class, String.class, Context.class, AttributeSet.class};

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        AppCompatDelegate delegate = getDelegate();
        View view = null;
        try {
            if (sCreateViewMethod == null) {
                sCreateViewMethod = delegate.getClass().getMethod("createView", sCreateViewSignature);
            }
            Object object = sCreateViewMethod.invoke(delegate, parent, name, context, attrs);
            view = (View) object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttrs(attrs, context);

        if (view != null && view instanceof ISkinable) {
            skinAttrList.add(new SkinAttr(SkinAttrType.CUSTOM, null));
            return view;
        } else if (skinAttrList.isEmpty()) {
            return view;
        } else if (view == null) {
            view = createViewFromTag(context, name, attrs);

            if (view instanceof ISkinable) {
                skinAttrList.add(new SkinAttr(SkinAttrType.CUSTOM, null));
            }
        }
        injectSkin(view, skinAttrList);
        return view;
    }

    private void injectSkin(View view, List<SkinAttr> skinAttrList) {
        //do some skin inject
        if (skinAttrList.size() != 0) {
            List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
            if (skinViews == null) { // 此库下明显LinkedList更好
                skinViews = new LinkedList<>();
            }
            SkinManager.getInstance().addSkinView(this, skinViews);
            skinViews.add(new SkinView(view, skinAttrList));

            if (SkinManager.getInstance().needChangeSkin()) {
                // 放到队列后面处理，不然处理ListView等会不断触发刷新，直接触发ANR
                mHandler.removeCallbacks(mDelayApplyRun);
                mHandler.post(mDelayApplyRun);
            }
        }
    }


    private Runnable mDelayApplyRun = new Runnable() {
        @Override
        public void run() {
            if (SkinManager.getInstance().needChangeSkin()) {
                SkinManager.getInstance().apply(BaseSkinActivity.this);
            }
        }
    };


    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                // try the android.widget prefix first...
                return createView(context, name, "android.widget.");
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().addChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().removeChangedListener(this);
    }


    @Override
    public void onSkinChanged() {
        SkinManager.getInstance().apply(this);
    }
}
