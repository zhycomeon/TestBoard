package com.datai.safesoftboard.keyboardview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.datai.safesoftboard.R;
import com.datai.safesoftboard.key.SoftKey;


/**
 * 数字键盘布局
 */
public class SoftKeySpec2NumLayView extends SoftKeyView {

    /**
     * 数字键盘的分布4行3列
     */
    private int row = 4;
    private int col = 4;

    private SoftKey mKeyAdd;
    private SoftKey mKeyMin;

    public SoftKeySpec2NumLayView(Context context) {
        this(context, null);
    }

    public SoftKeySpec2NumLayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftKeySpec2NumLayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public SoftKey[] initSoftKeys() {
        SoftKey[] result = new SoftKey[11];
        for (int i = 0; i < result.length; i++) {
            SoftKey btn = new SoftKey();
            if (i == result.length - 1) {
                btn.setText(".");
            } else {
                btn.setText(String.valueOf(i));
            }
            result[i] = btn;
        }
        return result;
    }

    @Override
    public SoftKey[] measureSoftKeysPos(SoftKey[] softKeys) {
        for (int i = 0; i < softKeys.length; i++) {
            if (i != 0) {
                softKeys[i].setX(blockWidth / 2 + ((i - 1) % (col - 1)) * blockWidth);
                softKeys[i].setY(blockHeight / 2 + ((i - 1) / (col - 1) % row) * blockHeight);
            } else {
                softKeys[i].setX(blockWidth * 3 / 2);
                softKeys[i].setY(blockHeight / 2 + 3 * blockHeight);
            }
            softKeys[i].setWidth(blockWidth - gapWidth * 2);
            softKeys[i].setHeight(blockHeight - gapHeight * 2);
        }
        return softKeys;
    }

    @Override
    public int measureBlockWidth(int keyBoardwidth) {
        return keyBoardwidth / col;
    }

    @Override
    public int measureBlockHeight(int keyBoardHeight) {
        return keyBoardHeight / row;
    }

    @Override
    public void drawSoftKeysPos(Canvas canvas, SoftKey[] softKeys) {
        if (softKeys == null) {
            return;
        }

        canvas.drawColor(0xffcccccc);

        //画软键盘的10个数字按钮 和一个小数点
        for (int index = 0; index < softKeys.length; index++) {
            drawSoftKey(canvas, softKeys[index]);
        }


        //创建软键盘的删除按钮
        if (delBtn == null) {
            delBtn = new SoftKey();
            delBtn.setKeyType(SoftKey.KeyType.ICON);
            delBtn.setIcon(R.drawable.ic_softkey_delete);
            delBtn.setHeight(blockHeight - gapHeight * 2);
            delBtn.setWidth(blockWidth - gapWidth * 2);
            delBtn.setX(2 * blockWidth + blockWidth / 2);
            delBtn.setY(3 * blockHeight + blockHeight / 2);
        }

        //画软键盘的删除按钮
        drawSoftKey(canvas, delBtn);

        if (mKeyAdd == null) {
            mKeyAdd = new SoftKey();
            mKeyAdd.setText("+");
            mKeyAdd.setHeight(blockHeight * 2 - gapHeight * 2);
            mKeyAdd.setWidth(blockWidth - gapWidth * 2);
            mKeyAdd.setX(blockWidth / 2 + ((col - 1) % col) * blockWidth);
            mKeyAdd.setY(blockHeight);
        }

        drawSoftKey(canvas, mKeyAdd);

        if (mKeyMin == null) {
            mKeyMin = new SoftKey();
            mKeyMin.setText("-");
            mKeyMin.setHeight(blockHeight * 2 - gapHeight * 2);
            mKeyMin.setWidth(blockWidth - gapWidth * 2);
            mKeyMin.setX(blockWidth / 2 + ((col - 1) % col) * blockWidth);
            mKeyMin.setY(((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyMin);

//        drawHold(canvas);
    }

    @Override
    public boolean handleKeyTouching(int eventX, int eventY, int action) {
        boolean needRefresh = super.handleKeyTouching(eventX, eventY, action);
        /*if (mKeyAdd.inRange(eventX, eventY) && action == MotionEvent.ACTION_DOWN) {
            performQuickAdd();
        }

        if (!mKeyAdd.inRange(eventX, eventY)&& action == MotionEvent.ACTION_MOVE) {
            cancelQuickAdd();
        }

        needRefresh = needRefresh || mKeyAdd.isPreessed();*/

        needRefresh = needRefresh || mKeyAdd.updatePressed(eventX, eventY);
        needRefresh = needRefresh || mKeyMin.updatePressed(eventX, eventY);

        return needRefresh;
    }

    @Override
    public boolean handleTouchUp(int eventX, int eventY, int action) {
        mKeyAdd.setPreessed(false);
        mKeyMin.setPreessed(false);
        if (mKeyAdd.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onJMEAdd();
            }
            return true;
        }

        if (mKeyMin.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onJMEMin();
            }
            return true;
        }

        return super.handleTouchUp(eventX, eventY, action);
    }

}
