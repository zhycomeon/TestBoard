package com.datai.safesoftboard.view;


import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow.OnDismissListener;

import com.datai.safesoftboard.keyboard.BaseSoftKeyBoard;
import com.datai.safesoftboard.BusinessType;
import com.datai.safesoftboard.R;
import com.datai.safesoftboard.keyboard.SoftKeyBoardManager;
import com.datai.safesoftboard.Utils.ScreenUtils;
import com.datai.safesoftboard.interfaces.IBusinessProcess;
import com.datai.safesoftboard.interfaces.IBusinessTips;
import com.datai.safesoftboard.interfaces.IUpdateEncodeStr;

import java.lang.reflect.Field;


public class SafeEdit extends EditText implements OnDismissListener, IUpdateEncodeStr {
    /*
        0 代表输入密码的安全键盘；1 代表输入手数的键盘 ;2 代表输入价格的键盘;3 输入出入金键盘；4 输入出入金密码键盘
         */
    private final static int SOFTKEYBOARD_TYPE_PASSWORD = 0;
    private final static int SOFTKEYBOARD_TYPE_PACKAGE = 1;
    private final static int SOFTKEYBOARD_TYPE_PRICE = 2;
    private final static int SOFTKEYBOARD_TYPE_INOUTAMOUNT = 3;
    private final static int SOFTKEYBOARD_TYPE_INOUTPASSWORD = 4;

    private String mTwoDecimalFormat;
    protected BaseSoftKeyBoard softKeyBoard;
    protected ViewGroup mRootView;
    protected int mMarginTop = 0;
    protected StringBuilder mEncodeStr;
    protected StringBuilder inputStr;

    private float mMinUnit = 0f;
    private int mMaxLength;
    private boolean bOpenSystemInput = false;

    protected int mType = 0;

    private IBusinessProcess mBusinessListener = null;
    private IBusinessTips mBusinessTipsListener = null;

    public SafeEdit(Context context) {
        this(context, null);
        //initSafeEdit(context);
    }

    public SafeEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSafeEdit(context, attrs);
    }

    public SafeEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSafeEdit(context, attrs);
    }

    public void initSafeEdit(Context context, AttributeSet attrs) {
        //setOnFocusChangeListener(this);
        mEncodeStr = new StringBuilder();
        inputStr = new StringBuilder();
        mTwoDecimalFormat = context.getString(R.string.str_two_decimal_places);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextType);

        mType = ta.getInteger(R.styleable.EditTextType_inputType, 0);
        mMaxLength = getMaxLength();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_UP) {
            requestFocus();
            setFocusable(true);
            setSelection(length());
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);

            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (softKeyBoard == null) {
                        switch (mType) {
                            case SOFTKEYBOARD_TYPE_PASSWORD:
                                softKeyBoard = SoftKeyBoardManager.getSoftKeyBoard(getContext(), mType);//new SafeSoftKeyBoard(getContext());
                                mEncodeStr.delete(0, mEncodeStr.length());
                                setText("");
                                inputStr.delete(0, inputStr.length());

                                break;
                            case SOFTKEYBOARD_TYPE_PACKAGE:
                                softKeyBoard = SoftKeyBoardManager.getSoftKeyBoard(getContext(), mType);//new Spec1SoftKeyBoard(getContext());
                                inputStr.replace(0, inputStr.length(), getText().toString());
                                mEncodeStr.delete(0, mEncodeStr.length());
                                break;
                            case SOFTKEYBOARD_TYPE_PRICE:
                            case SOFTKEYBOARD_TYPE_INOUTAMOUNT:
                            case SOFTKEYBOARD_TYPE_INOUTPASSWORD:
                                softKeyBoard = SoftKeyBoardManager.getSoftKeyBoard(getContext(), mType);
                                inputStr.replace(0, inputStr.length(), getText().toString());
                                mEncodeStr.delete(0, mEncodeStr.length());
                                break;
                            default:
                                break;
                        }

                        updateBusinessTips();
                        if (softKeyBoard != null) {
                            softKeyBoard.setEdit(SafeEdit.this);
                            softKeyBoard.setOnDismissListener(SafeEdit.this);
                            softKeyBoard.show();
                        }

                        int boardHeight = getSoftKeyboardHeight();
//						int decorHeight = getDecorViewHeight();

                        //获取到程序显示的区域，包括标题栏，但不包括状态栏
//						Rect frame = new Rect();
//						((Activity)getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

                        //取到程序不包括标题栏的部分
                        //int contentTop = ((Activity)getContext()).findViewById(Window.ID_ANDROID_CONTENT).getTop();

                        int screenHeight = ScreenUtils.getScreenHeight(getContext());
                        int statusHeight = ScreenUtils.getStatusHeight(getContext());
                        int viewY = ScreenUtils.getYOnScreen(SafeEdit.this);
                        int titleHeight = ((Activity) getContext()).findViewById(Window.ID_ANDROID_CONTENT).getTop();
                        int editBottom = viewY + SafeEdit.this.getHeight();
                        if (screenHeight - editBottom < boardHeight) {
                            mMarginTop = boardHeight - screenHeight + editBottom;
                            showAnimator();
                        }
                    }
                }
            }, 500);

        }
        return true;
    }

    @Override
    public void onDismiss() {
        if (softKeyBoard != null) {
            softKeyBoard.recycle();
            softKeyBoard = null;
        }
        hideAnimator();
        requestFocus();
        setFocusable(true);

        if (bOpenSystemInput) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(SafeEdit.this, InputMethodManager.RESULT_SHOWN);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                    bOpenSystemInput = false;
                }
            }, 50);
        }
    }

    public void setSystemInputSwitch(boolean bSwitch) {
        bOpenSystemInput = bSwitch;
    }

    public int getSoftKeyboardHeight() {
        if (softKeyBoard != null) {
            return softKeyBoard.getSoftKeyboardHeight();
        }

        return 0;
    }

    public void updateBusinessTips() {
        if (mBusinessTipsListener != null) {
            String tips = mBusinessTipsListener.getBusinessTips(this);
            if (softKeyBoard != null) {
                softKeyBoard.setBusinessTips(tips);
            }
        }
    }

    public void setRootView(ViewGroup root) {
        mRootView = root;
    }

    public int getType() {
        return mType;
    }

    private void showAnimator() {
        if (mRootView == null)
            return;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mMarginTop);
        valueAnimator.setDuration(100);

        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.setEvaluator(new TypeEvaluator<Integer>() {
            // fraction = t / duration
            @Override
            public Integer evaluate(float fraction, Integer startValue,
                                    Integer endValue) {
                return (int) (-mMarginTop * fraction);
            }
        });
        valueAnimator.start();


        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int top = (Integer) animation.getAnimatedValue();

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRootView.getLayoutParams();
                params.topMargin = top;
                mRootView.setLayoutParams(params);
            }
        });

    }

    private void hideAnimator() {
        if (mRootView == null)
            return;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mMarginTop);
        valueAnimator.setDuration(100);
        //valueAnimator.setTarget(mRootView);
        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.setEvaluator(new TypeEvaluator<Integer>() {
            // fraction = t / duration
            @Override
            public Integer evaluate(float fraction, Integer startValue,
                                    Integer endValue) {
                return (int) (-mMarginTop * (1 - fraction));
            }
        });
        valueAnimator.start();


        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int top = (Integer) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRootView.getLayoutParams();
                params.topMargin = top;
                mRootView.setLayoutParams(params);
            }
        });
    }

    @Override
    public void updateEncodeString(String encodeStr) {
        mEncodeStr.replace(0, mEncodeStr.length(), encodeStr);
    }

    @Override
    public String getEncodeString() {
        return mEncodeStr.toString();
    }

    public void setMinUnit(float unit) {
        mMinUnit = unit;
    }

    public void setBusinessListener(IBusinessProcess listener) {
        mBusinessListener = listener;
    }

    public void setBusinessTipsListener(IBusinessTips listener) {
        mBusinessTipsListener = listener;
    }

    public void processKeyValue(BusinessType type, String keyValue) {
        switch (type) {
            case BUSINESS_TYPE_DEFAULT:
                if (mBusinessListener != null) {
                    mBusinessListener.commit(this);
                }
                break;
            case BUSINESS_TYPE_ALL:
            case BUSINESS_TYPE_HALF:
            case BUSINESS_TYPE_ONETHIRD:
            case BUSINESS_TYPE_TWOTHIRD:
            case BUSINESS_TYPE_ONEFOURTH:
            case BUSINESS_TYPE_INCREASE:
            case BUSINESS_TYPE_DECREASE:
                if (mBusinessListener != null) {
                    mBusinessListener.doBusiness(this, type);
                }
                break;
            case BUSINESS_TYPE_KEYVALUE:
                if (TextUtils.isEmpty(keyValue)) {
                    break;
                }
                if (mType == SOFTKEYBOARD_TYPE_PASSWORD || mType == SOFTKEYBOARD_TYPE_INOUTPASSWORD) {
                    if (inputStr.length() >= mMaxLength)
                        return;
                    inputStr.append(keyValue);
                    setText(inputStr);
                    setSelection(inputStr.length());
                } else if (mType == SOFTKEYBOARD_TYPE_PACKAGE) {
                    if (inputStr.length() == 0 && (keyValue.equalsIgnoreCase("00") || keyValue.equalsIgnoreCase("0"))) {
                        return;
                    }
                    if (inputStr.length() >= mMaxLength)
                        return;
                    if (!inputStr.toString().equals("0")) {
                        inputStr.append(keyValue);
                    } else if (!keyValue.equals("00")) {
                        inputStr.delete(0, inputStr.length());
                        inputStr.append(keyValue);
                    }
                    setText(inputStr);
                    setSelection(inputStr.length());

                } else if (mType == SOFTKEYBOARD_TYPE_PRICE) {
                    if (inputStr.length() >= mMaxLength)
                        return;
                    if (!keyValue.equalsIgnoreCase(".")) {
                        inputStr.append(keyValue);
                        setText(inputStr);
                        setSelection(inputStr.length());
                    } else {
                        if (inputStr.length() > 0 && !inputStr.toString().contains(".")) {
                            inputStr.append(keyValue);
                            setText(inputStr);
                            setSelection(inputStr.length());
                        }
                    }
                } else if (mType == SOFTKEYBOARD_TYPE_INOUTAMOUNT) {
                    if (inputStr.length() >= mMaxLength)
                        return;
                    if (!keyValue.equals("0") && !keyValue.equals(".")) {
                        inputStr.append(keyValue);
                    } else if (keyValue.equals(".") && (inputStr.length() > 0 && !inputStr.toString().contains("."))) {
                        inputStr.append(keyValue);

                    } else if (inputStr.length() > 0 && keyValue.equals("0")) {
                        inputStr.append(keyValue);
                    }
                    setText(inputStr);
                    setSelection(inputStr.length());
                }
                break;
            case BUSINESS_TYPE_DELETE:

                if (!TextUtils.isEmpty(inputStr.toString())) {
                    setText(inputStr.deleteCharAt(inputStr.length() - 1));
                    setSelection(inputStr.length());
                }
                break;
        }


    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (text != null && inputStr != null) {
            inputStr = inputStr.replace(0, inputStr.length(), text.toString());
        }
    }

    //获取可输入最大字符串长度
    public int getMaxLength() {
        int length = 0;
        try {
            InputFilter[] inputFilters = getFilters();
            for (InputFilter filter : inputFilters) {
                Class<?> c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMaxLength = length;
        return length;
    }

}
