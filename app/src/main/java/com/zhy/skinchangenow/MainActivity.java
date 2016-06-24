package com.zhy.skinchangenow;

import java.io.File;

import com.nineoldandroids.view.ViewHelper;
import com.zhy.changeskin.SkinManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41531475
 *
 * @author zhy
 */
public class MainActivity extends com.zhy.changeskin.base.BaseSkinActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    // private String mSkinPkgPath = Environment.getExternalStorageDirectory() +
    // File.separator + "night_plugin.apk";
    private String[] mDatas = new String[] { "Activity", "Service", "Activity", "Service", "Activity", "Service",
            "Activity", "Service" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();

    }

    private void initEvents() {

        mListView = (ListView) findViewById(R.id.id_listview);
        mListView.setAdapter(new ArrayAdapter<String>(this, -1, mDatas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false);
                }

                TextView tv = (TextView) convertView.findViewById(R.id.id_tv_title);
                tv.setText(getItem(position));


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SkinActivity.class));
                    }
                });

                return convertView;
            }
        });

        mDrawerLayout.setDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")) {

                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.id_left_menu_container);
        if (fragment == null) {
            fm.beginTransaction().add(R.id.id_left_menu_container, new MenuLeftFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.id_action_plugin_skinchange:
            final File themeFile = new File(getCacheDir(), "skin1");
            SkinManager.getInstance().changeSkin(themeFile.getPath(),
                    new com.zhy.changeskin.callback.ISkinChangingCallback() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(MainActivity.this, "换肤失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            Toast.makeText(MainActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
                        }
                    });
            break;
        case R.id.id_action_remove_any_skin:
            com.zhy.changeskin.SkinManager.getInstance().removeAnySkin();
            break;
        case R.id.id_action_test_res:

        }

        return super.onOptionsItemSelected(item);
    }

}
