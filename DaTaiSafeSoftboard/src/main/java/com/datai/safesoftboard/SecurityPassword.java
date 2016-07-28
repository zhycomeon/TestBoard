package com.datai.safesoftboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datai.safesoftboard.Utils.DensityUtils;
import com.datai.safesoftboard.interfaces.IInputComplete;

/**
 * Created by Administrator on 2016/7/19.
 */
public class SecurityPassword extends LinearLayout {
    private static final int DEFAULT_PASSWORDLENGTH = 6;
    private static final int DEFAULT_TEXTSIZE = 32;
    private static final String DEFAULT_TRANSFORMATION = "●";
    private static final int DEFAULT_LINECOLOR = 0xaa888888;
    private static final int DEFAULT_BORDERCOLOR = 0xfffd0000;

    private int mTextSize = DEFAULT_TEXTSIZE;
    private int mPasswordLength = DEFAULT_PASSWORDLENGTH;
    private int mLineWidth;
    private ColorStateList mTextColor;
    private int mPasswordType;
    private Drawable mLineDrawable;
    private Drawable mBorderDrawable;
    private int mLineColor = DEFAULT_LINECOLOR;
    private int mGridColor = DEFAULT_BORDERCOLOR;

    private StringBuilder mContentStr;

    private IInputComplete completeListener = null;

    private TextView[] mViewArr;

    public SecurityPassword(Context context) {
        this(context, null);
    }

    public SecurityPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public SecurityPassword(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SecurityPassword(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PasswordView, defStyleAttr, 0);

        mTextColor = ta.getColorStateList(R.styleable.PasswordView_pvTextColor);
        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
        }

        int textSize = ta.getDimensionPixelSize(R.styleable.PasswordView_pvTextSize, -1);
        if (textSize != -1) {
            mTextSize = (int) DensityUtils.px2sp(textSize);
        }

        mLineWidth = (int) ta.getDimension(R.styleable.PasswordView_pvLineWidth, DensityUtils.dp2px(1));

        mLineColor = ta.getColor(R.styleable.PasswordView_pvLineColor, DEFAULT_LINECOLOR);
        mGridColor = ta.getColor(R.styleable.PasswordView_pvBorderColor, DEFAULT_BORDERCOLOR);

        mPasswordType = 0;

        mLineDrawable = new ColorDrawable(mLineColor);
        mPasswordLength = ta.getInt(R.styleable.PasswordView_pvPasswordLength, DEFAULT_PASSWORDLENGTH);

        ta.recycle();
//        mBorderDrawable = generateBackgroundDrawable();
//        setBackgroundDrawable(mBorderDrawable);
        mContentStr = new StringBuilder();
        mViewArr = new TextView[mPasswordLength];
        int index = 0;
        LayoutInflater inflater = LayoutInflater.from(context);
        while (index < mPasswordLength) {

            TextView textView = (TextView) inflater.inflate(R.layout.textview, null);
            setCustomAttr(textView);

            LayoutParams textViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
            textView.setGravity(Gravity.CENTER);
            addView(textView, textViewParams);

            mViewArr[index] = textView;

            if ((index + 1) != mPasswordLength) {
                View dividerView = inflater.inflate(R.layout.divider, null);
                LayoutParams dividerParams = new LayoutParams(mLineWidth, LayoutParams.MATCH_PARENT);
                dividerView.setBackgroundDrawable(mLineDrawable);
                addView(dividerView, dividerParams);
            }
            index++;
        }
//        setBackgroundResource(R.drawable.input_area);
    }

    private GradientDrawable generateBackgroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(mGridColor);
        drawable.setStroke(mLineWidth, mLineColor);
        return drawable;
    }

    private void setCustomAttr(TextView view) {
        if (mTextColor != null)
            view.setTextColor(mTextColor);
//        view.setTextColor(0x000000);
        view.setTextSize(mTextSize);

        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (mPasswordType) {

            case 1:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case 2:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case 3:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }
        view.setInputType(inputType);
        // view.setTransformationMethod(mTransformationMethod);
    }


    public String getPasswordContent() {
        if (mContentStr != null) {
            return mContentStr.toString();
        }
        return "";
    }

    public void appendValue(String value) {
        if (mContentStr != null && mContentStr.length() < mPasswordLength) {
            mViewArr[mContentStr.length()].setText(value);
            mContentStr.append(value);

            if (mContentStr.length() == mPasswordLength) {
                if (completeListener != null) {
                    completeListener.completeNotification();
                }
            }
        }

        invalidate();
    }

    public void deleteLastValue() {
        if (mContentStr != null && mContentStr.length() > 0) {
            mViewArr[mContentStr.length() - 1].setText("");
            mContentStr.delete(mContentStr.length() - 1, mContentStr.length());

        }
        invalidate();
    }

    public void setCompleteListener(IInputComplete listener) {
        completeListener = listener;
    }

}
