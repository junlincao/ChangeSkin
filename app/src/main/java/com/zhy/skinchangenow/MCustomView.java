package com.zhy.skinchangenow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.zhy.changeskin.ResourceManager;
import com.zhy.changeskin.base.ISkinable;

/**
 * com.zhy.skinchangenow
 *
 * @author CJL
 * @since 2016-06-27
 */
public class MCustomView extends TextView implements ISkinable {

    private int mPreColor;
    private Drawable mBgD;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MCustomView(Context context) {
        this(context, null);
    }

    public MCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGravity(Gravity.CENTER);
        mPreColor = ContextCompat.getColor(context, R.color.skin_textColor);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MCustomView);
        mBgD = a.getDrawable(R.styleable.MCustomView_MCV_background);
        a.recycle();

        if(mBgD == null){
            mBgD = ContextCompat.getDrawable(context, R.drawable.skin_mainbg);
        }
    }

    @Override
    public void applySkin(ResourceManager rm) {
        mPreColor = rm.getColor("skin_textColor");
        mBgD = rm.getDrawableByName("skin_mainbg");
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBgD.setBounds(0, 0, 200, 200);
        mBgD.draw(canvas);

        mPaint.setColor(mPreColor);
        canvas.drawRect(0, 0, 100, 100, mPaint);
    }
}
