package com.zhy.skinchangenow;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zhy.changeskin.base.BaseSkinActivity;
import com.zhy.changeskin.utils.L;

/**
 * caiyi.com.testapkbuild
 *
 * @author CJL
 * @since 2016-06-24
 */
public class SkinActivity extends BaseSkinActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long st = System.currentTimeMillis();


        setContentView(R.layout.activity_skin);
        long et1 = System.currentTimeMillis();

        L.e("setContentView cost Time -> " + (et1 - st) + "ms");


        GridView gv = (GridView) findViewById(R.id.grid);
        gv.setAdapter(new MAdapter(this));

    }

    static class MAdapter extends BaseAdapter{

        private Context context;

        public MAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.text);
            tv.setText("" + position);
            return convertView;
        }
    }
}
