package com.dlong.rep.dlsidebar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class DLSideBar extends View {
    private Context mContext;
    private int mChoose  = -1;
    private Paint mSideTextPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint mSideSelectTextPaint = new Paint(ANTI_ALIAS_FLAG);
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private DLTextDialog mTextDialog;

    /** sidebar的字符列表 **/
    private CharSequence[] mStringArray;
    /** sidebar的字符颜色 **/
    private int mSideTextColor;
    /** sidebar的字符选中时的颜色 **/
    private int mSideTextSelectColor;
    /** sidebar的字符大小 **/
    private float mSideTextSize;
    /** sidebar的背景颜色 **/
    private Drawable mSideBackground;
    /** 选中弹窗字符颜色 **/
    private int mDialogTextColor;
    /** 选中弹窗字符大小 **/
    private float mDialogTextSize;
    /** 选中弹窗字符背景颜色 **/
    private Drawable mDialogTextBackground;
    /** 选中弹窗字符背景宽度 **/
    private int mDialogTextBackgroundWidth;
    /** 选中弹窗字符背景高度 **/
    private int mDialogTextBackgroundHeight;

    /**
     * 自定义接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String str);
    }

    public DLSideBar(Context context) {
        super(context);
        mContext = context;
        initData(null);
    }

    public DLSideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData(attrs);
    }

    public DLSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData(attrs);
    }

    /**
     * 初始化数据
     * @param attrs
     */
    private void initData(@Nullable AttributeSet attrs) {
        // 加载默认资源
        final Resources res = getResources();
        final CharSequence[] defaultStringArray = res.getTextArray(R.array.dl_side_bar_def_list);
        final int defaultSideTextColor = res.getColor(R.color.default_side_text_color);
        final int defaultSideTextSelectColor = res.getColor(R.color.default_side_text_select_color);
        final float defaultSideTextSize = res.getDimension(R.dimen.default_side_text_size);
        final Drawable defaultSideBackground = res.getDrawable(R.drawable.default_side_background);
        final int defaultDialogTextColor = res.getColor(R.color.default_dialog_text_color);
        final float defaultDialogTextSize = res.getDimension(R.dimen.default_dialog_text_size);
        final Drawable defaultDialogTextBackground = res.getDrawable(R.drawable.default_dialog_text_background);
        final int defaultDialogTextBackgroundWidth = res.getDimensionPixelSize(R.dimen.default_dialog_text_background_width);
        final int defaultDialogTextBackgroundHeight = res.getDimensionPixelSize(R.dimen.default_dialog_text_background_height);
        // 读取配置信息
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.DLSideBar);
        mStringArray = a.getTextArray(R.styleable.DLSideBar_sideTextArray);
        if (null == mStringArray||mStringArray.length <= 0){
            mStringArray = defaultStringArray;
        }
        mSideTextColor = a.getColor(R.styleable.DLSideBar_sideTextColor, defaultSideTextColor);
        mSideTextSelectColor = a.getColor(R.styleable.DLSideBar_sideTextSelectColor, defaultSideTextSelectColor);
        mSideTextSize = a.getDimension(R.styleable.DLSideBar_sideTextSize, defaultSideTextSize);
        mSideBackground = a.getDrawable(R.styleable.DLSideBar_sideBackground);
        if (null == mSideBackground){
            mSideBackground = defaultSideBackground;
        }
        mDialogTextColor = a.getColor(R.styleable.DLSideBar_dialogTextColor, defaultDialogTextColor);
        mDialogTextSize = a.getDimension(R.styleable.DLSideBar_dialogTextSize, defaultDialogTextSize);
        mDialogTextBackground = a.getDrawable(R.styleable.DLSideBar_dialogTextBackground);
        if (null == mDialogTextBackground){
            mDialogTextBackground = defaultDialogTextBackground;
        }
        mDialogTextBackgroundWidth = a.getDimensionPixelSize(R.styleable.DLSideBar_dialogTextBackgroundWidth, defaultDialogTextBackgroundWidth);
        mDialogTextBackgroundHeight = a.getDimensionPixelSize(R.styleable.DLSideBar_dialogTextBackgroundHeight, defaultDialogTextBackgroundHeight);
        // 释放内存，回收资源
        a.recycle();
        mTextDialog = new DLTextDialog(mContext, mDialogTextBackgroundWidth, mDialogTextBackgroundHeight,
                mDialogTextColor, mDialogTextSize, mDialogTextBackground);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // get the height
        int height = getHeight();
        // get the width
        int width = getWidth();
        // get one letter height
        int singleHeight = height / mStringArray.length;

        mSideTextPaint.setColor(mSideTextColor);
        mSideTextPaint.setTextSize(mSideTextSize);
        mSideTextPaint.setTypeface(Typeface.DEFAULT);

        mSideSelectTextPaint.setColor(mSideTextSelectColor);
        mSideSelectTextPaint.setTextSize(mSideTextSize);
        mSideSelectTextPaint.setTypeface(Typeface.DEFAULT);
        mSideSelectTextPaint.setFakeBoldText(true);

        for (int i = 0; i < mStringArray.length; i++) {
            String text = mStringArray[i].toString();
            // if choosed
            if(i == mChoose) {
                float x = (float) width / 2 - mSideSelectTextPaint.measureText(text) / 2;
                float y = singleHeight * i + singleHeight;
                canvas.drawText(text, x, y, mSideSelectTextPaint);
            } else {
                float x = (float) width / 2 - mSideTextPaint.measureText(text) / 2;
                float y = singleHeight * i + singleHeight;
                canvas.drawText(text, x, y, mSideTextPaint);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        // get the Y
        final float y = event.getY();
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener changedListener = onTouchingLetterChangedListener;
        final int letterPos = (int)( y / getHeight() * mStringArray.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                mChoose = -1;
                invalidate();
                if (mTextDialog != null) mTextDialog.dismissD();
                break;

            default:
                setBackground(mSideBackground);
                if (oldChoose != letterPos) {
                    if (letterPos >= 0 && letterPos < mStringArray.length) {
                        if (changedListener != null)
                            changedListener.onTouchingLetterChanged(mStringArray[letterPos].toString());
                        if (mTextDialog != null) mTextDialog.showD(mStringArray[letterPos].toString());
                        mChoose = letterPos;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 设定回调接口
     * @param changedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener changedListener) {
        this.onTouchingLetterChangedListener = changedListener;
    }
}
