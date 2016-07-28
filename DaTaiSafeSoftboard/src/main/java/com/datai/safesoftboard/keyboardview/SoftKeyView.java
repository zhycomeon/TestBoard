package com.datai.safesoftboard.keyboardview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.datai.safesoftboard.R;
import com.datai.safesoftboard.interfaces.ISoftKeyBoard;
import com.datai.safesoftboard.interfaces.SoftKeyStatusListener;
import com.datai.safesoftboard.key.SoftKey;


@SuppressLint({"ClickableViewAccessibility", "HandlerLeak"})
public abstract class SoftKeyView extends View implements ISoftKeyBoard {

    //private static final String TAG=SoftKeyView.class.getSimpleName();
    /**
     * 字符数组
     */
    public SoftKey[] softKeys;

    /**
     * 删除按钮
     */
    public SoftKey delBtn;
    /**
     * 确认按钮
     */
    public SoftKey confirmBtn;
    /**
     * 数字按钮正常时候的背景样式
     */
    public Paint keyNormalBgPaint;
    /**
     * 数字按钮按压时候的背景样式
     */
    public Paint keyPressedBgPaint;
    /**
     * 数字按钮数值的样式
     */
    public Paint keyTextPaint;
    /**
     * 分割线的样式
     */
    public Paint keyBorderPaint;
    /**
     * 每个按钮的间隙
     */
    public int middleSpace = 10;
    /**
     * 按钮的宽度
     */
    public int blockWidth;

    /**
     * 按钮x方向间隔
     */
    public int gapWidth = 5;
    /**
     * 按钮y方向间隔
     */
    public int gapHeight = 10;
    /**
     * 按钮的高度
     */
    public int blockHeight;
    /**
     * 软键盘的背景颜色
     */
    public int softBoardBgColor;
    /**
     * 按钮正常显示的颜色
     */
    public int keyNormalBgColor;
    /**
     * 按钮按压显示的颜色
     */
    public int keyPressedBgColor;
    /**
     * 按钮文本的颜色
     */
    public int keyTextColor;
    /**
     * 按钮文本的大小
     */
    public int keyTextSize;
    /**
     * 按钮边框的颜色
     */
    public int keyborderColor;
    /**
     * 按钮边框的大小
     */
    public int keyborderSize;
    /**
     * 数字键盘是否需要混淆
     */
    public boolean keyRandom;
    /**
     * 按钮阴影颜色
     */
    public int keyShadowColor;
    /**
     * 视图的模式:数字，字母，符号
     */
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_NUMBER = 1;
    public static final int MODE_AZ = 2;
    public static final int MODE_PUNCT = 3;

    private int touchX;
    private int touchY;
    //private AudioManager audioManager;
    protected SoftKeyStatusListener listener;

    private Options opts = null;
    private Rect bounds = null;
    private RectF roundBounds = null;

    public SoftKeyView(Context context) {
        this(context, null);
    }

    public SoftKeyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSoftBoradStyle(context, attrs);
    }

    public void initSoftBoradStyle(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.SoftKeyView);

        softBoardBgColor = ta.getColor(
                R.styleable.SoftKeyView_keyborderColor, Color.TRANSPARENT);
        keyNormalBgColor = ta.getColor(
                R.styleable.SoftKeyView_keyNormalBgColor, Color.WHITE);
        keyPressedBgColor = ta.getColor(
                R.styleable.SoftKeyView_keyPressedBgColor,
                Color.parseColor("#66929292"));
        keyShadowColor = ta.getColor(R.styleable.SoftKeyView_keyShadowColor, Color.GRAY);
        keyTextColor = ta.getColor(R.styleable.SoftKeyView_keyTextColor,
                Color.BLACK);
        keyTextSize = ta.getDimensionPixelSize(
                R.styleable.SoftKeyView_keyTextSize, 20);
        keyborderColor = ta.getColor(
                R.styleable.SoftKeyView_keyborderColor,
                Color.parseColor("#66929292"));
        keyborderSize = ta.getDimensionPixelSize(
                R.styleable.SoftKeyView_keyborderSize, 1);

        keyRandom = ta.getBoolean(R.styleable.SoftKeyView_keyrandomPosition, false);

        ta.recycle();

        if (keyNormalBgPaint == null) {
            keyNormalBgPaint = new Paint();
            keyNormalBgPaint.setColor(keyNormalBgColor);
            keyNormalBgPaint.setAntiAlias(true);

        }

        if (keyPressedBgPaint == null) {
            keyPressedBgPaint = new Paint();
            keyPressedBgPaint.setColor(keyPressedBgColor);
            keyPressedBgPaint.setAntiAlias(true);
        }

        if (keyTextPaint == null) {
            keyTextPaint = new Paint();
            keyTextPaint.setColor(keyTextColor);
            keyTextPaint.setTextSize(keyTextSize);
            keyTextPaint.setAntiAlias(true);
        }

        if (keyBorderPaint == null) {
            keyBorderPaint = new Paint();
            keyBorderPaint.setColor(keyborderColor);
            keyBorderPaint.setTextSize(keyborderSize);
            keyBorderPaint.setAntiAlias(true);
        }

//		if (audioManager == null) {
//			audioManager = (AudioManager) context
//					.getSystemService(Context.AUDIO_SERVICE);
//		}

        opts = new Options();
        bounds = new Rect();
        roundBounds = new RectF();

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        blockWidth = measureBlockWidth(w);
        blockHeight = measureBlockHeight(h);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchX = (int) event.getX();
        touchY = (int) event.getY();
        boolean isrefresh = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isrefresh = handleKeyTouching(touchX, touchY, event.getAction());
                break;
            case MotionEvent.ACTION_MOVE:
                isrefresh = handleKeyTouching(touchX, touchY, event.getAction());
                break;
            case MotionEvent.ACTION_UP:
                isrefresh = handleTouchUp(touchX, touchY, event.getAction());
                break;
        }
        if (isrefresh) {
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (softKeys == null) {
            softKeys = initSoftKeys();
            softKeys = measureSoftKeysPos(softKeys);
            softKeys = makeSoftKeysRandom(softKeys);
        }
        drawSoftKeysPos(canvas, softKeys);
    }

    @Override
    public SoftKey[] makeSoftKeysRandom(SoftKey[] softKeys) {
//		int w;
//		Random rand = new Random();
//		for (int i = softKeys.length - 1; i > 0; i--) {
//			w = rand.nextInt(i);
//			String number = softKeys[i].getText();
//			softKeys[i].setText(softKeys[w].getText());
//			softKeys[w].setText(number);
//		}
        return softKeys;
    }

    @Override
    public void drawSoftKey(Canvas canvas, SoftKey softkey) {
        // TODO Auto-generated method stub
        switch (softkey.getKeyType()) {
            case TEXT:
                drawTextSoftKey(canvas, softkey);
                break;
            case ICON:
                drawIconSoftKey(canvas, softkey);
                break;
            case TEXT_ICON:
                drawIconSoftKey(canvas, softkey);
                break;
        }
        if (softkey.isPreessed()) {
            drawSoftKeyPressBg(canvas, softkey);
        }
    }

    /**
     * �����ı��İ�ť
     *
     * @param canvas
     * @param softkey
     */
    public void drawTextSoftKey(Canvas canvas, SoftKey softkey) {
        roundBounds.set(softkey.getX() - softkey.getWidth() / 2, softkey.getY() - softkey.getHeight() / 2,
                softkey.getX() + softkey.getWidth() / 2, softkey.getY() + softkey.getHeight() / 2);
        //绘制阴影
        keyNormalBgPaint.setShadowLayer(5, 0, 3, keyShadowColor);
        //绘制圆角矩形背景
        canvas.drawRoundRect(roundBounds, 10, 10, keyNormalBgPaint);
        bounds.setEmpty();
        keyTextPaint.getTextBounds(softkey.getText(), 0, softkey.getText().length(), bounds);
        int startX = softkey.getX() - bounds.width() / 2;
        int startY = softkey.getY() + bounds.height() / 2;
        canvas.drawText(softkey.getText(), startX, startY, keyTextPaint);
    }

    /**
     * ����ͼ��İ�ť
     *
     * @param canvas
     * @param softkey
     */
    public void drawIconSoftKey(Canvas canvas, SoftKey softkey) {
        roundBounds.setEmpty();
        roundBounds.set(softkey.getX() - softkey.getWidth() / 2, softkey.getY() - softkey.getHeight() / 2,
                softkey.getX() + softkey.getWidth() / 2, softkey.getY() + softkey.getHeight() / 2);
        //绘制阴影
        keyNormalBgPaint.setShadowLayer(5, 0, 3, keyShadowColor);
        //绘制圆角矩形背景
        canvas.drawRoundRect(roundBounds, 10, 10, keyNormalBgPaint);
        BitmapFactory.decodeResource(getContext().getResources(), softkey.getIcon(), opts);
        opts.inJustDecodeBounds = true;
        int minWH = Math.min(softkey.getHeight(), softkey.getWidth());
        int maxBP = Math.max(opts.outHeight, opts.outWidth);
        if (maxBP > minWH) {
            opts.inSampleSize = minWH / maxBP;
        } else {
            opts.inSampleSize = maxBP / minWH;
        }
        opts.inJustDecodeBounds = false;
        Bitmap newBit = BitmapFactory.decodeResource(getContext().getResources(), softkey.getIcon(), opts);
        canvas.drawBitmap(newBit, softkey.getX() - newBit.getWidth() / 2, softkey.getY() - newBit.getHeight() / 2, null);
        newBit.recycle();
    }

    /**
     * @param canvas
     * @param softkey
     */
    public void drawSoftKeyPressBg(Canvas canvas, SoftKey softkey) {
        int left = softkey.getX() - softkey.getWidth() / 2;
        int top = softkey.getY() - softkey.getHeight() / 2;
        int right = softkey.getX() + softkey.getWidth() / 2;
        int bottom = softkey.getY() + softkey.getHeight() / 2;
        roundBounds.setEmpty();
        roundBounds.set(left, top, right, bottom);

        //绘制阴影
        keyPressedBgPaint.setShadowLayer(5, 0, 3, keyShadowColor);
        //绘制圆角矩形背景
        canvas.drawRoundRect(roundBounds, 10, 10, keyPressedBgPaint);
    }

    @Override
    public boolean handleKeyTouching(int eventX, int eventY, int action) {
        boolean isrefresh = false;

        if (softKeys == null) {
            return false;
        }

        if (confirmBtn != null) {
            confirmBtn.updatePressed(eventX, eventY);

            isrefresh = confirmBtn.isPreessed();
        }

        if (delBtn != null) {
            if (delBtn.inRange(eventX, eventY) && action == MotionEvent.ACTION_DOWN) {
                performQuickDelete();
            }

            if (!delBtn.inRange(eventX, eventY) && action == MotionEvent.ACTION_MOVE) {
                cancelQuickDelete();
            }

            delBtn.updatePressed(eventX, eventY);

            isrefresh = (delBtn.isPreessed() || isrefresh);
        }

        for (int index = 0; index < softKeys.length; index++) {
            if (isrefresh) {
                softKeys[index].setPreessed(false);
            } else {
                isrefresh = softKeys[index].updatePressed(eventX, eventY);
            }
        }
        return isrefresh;
    }

    @Override
    public boolean handleTouchUp(int eventX, int eventY, int action) {
        resetSoftKeysState();
        if (delBtn != null && delBtn.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onDeleted();
            }
            return true;
        }
        if (confirmBtn != null && confirmBtn.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onConfirm();
            }
            return true;
        }
        SoftKey btn = obtainTouchSoftKey(eventX, eventY);
        if (btn != null) {
            if (listener != null) {
                listener.onPressed(btn);
            }
            return true;
        }
        return false;
    }

    @Override
    public SoftKey obtainTouchSoftKey(int eventX, int eventY) {
        if (softKeys == null) {
            return null;
        }
        for (int index = 0; index < softKeys.length; index++) {
            if (softKeys[index].inRange(eventX, eventY)) {
                return softKeys[index];
            }
        }
        return null;
    }

    @Override
    public void resetSoftKeysState() {
        cancelQuickDelete();
        if (delBtn != null) {
            delBtn.setPreessed(false);
        }
        if (confirmBtn != null) {
            confirmBtn.setPreessed(false);
        }
        for (int index = 0; index < softKeys.length; index++) {
            softKeys[index].setPreessed(false);
        }
    }

    @Override
    public void performQuickDelete() {
        pressHandler.sendEmptyMessageDelayed(KeyEvent.FLAG_KEEP_TOUCH_MODE, ViewConfiguration.getLongPressTimeout());
    }

    @Override
    public void cancelQuickDelete() {
        pressHandler.sendEmptyMessage(KeyEvent.FLAG_CANCELED_LONG_PRESS);
    }

    public Handler pressHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case KeyEvent.FLAG_LONG_PRESS:
                    if (listener != null) {
                        listener.onDeleted();
                        pressHandler.sendEmptyMessageDelayed(KeyEvent.FLAG_LONG_PRESS, 100);
                    }
                    break;
                case KeyEvent.FLAG_KEEP_TOUCH_MODE: {
                    pressHandler.sendEmptyMessage(KeyEvent.FLAG_LONG_PRESS);
                    pressHandler.removeMessages(KeyEvent.FLAG_KEEP_TOUCH_MODE);
                    break;
                }
                case KeyEvent.FLAG_CANCELED_LONG_PRESS:
                    pressHandler.removeMessages(KeyEvent.FLAG_KEEP_TOUCH_MODE);
                    pressHandler.removeMessages(KeyEvent.FLAG_LONG_PRESS);
                    break;
                default:
                    break;
            }
        }

    };

    public void setSoftKeyListener(SoftKeyStatusListener listener) {
        this.listener = listener;
    }

}
