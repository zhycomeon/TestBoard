package com.datai.safesoftboard.keyboardview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.datai.safesoftboard.R;
import com.datai.safesoftboard.key.SoftKey;


/**
 * 数字键盘布局
 */
public class SoftKeySpec1NumLayView extends SoftKeyView {

    /**
     * 数字键盘的分布4行3列
     */
    private int row = 5;
    private int col = 4;

    private SoftKey mKeyAdd;
    private SoftKey mKeyMin;
    private SoftKey mKeyHold;
    private SoftKey mKeyHold2;
    private SoftKey mKeyHold3;
    private SoftKey mKeyHold4;
    private SoftKey mKeyHold5;

    public SoftKeySpec1NumLayView(Context context) {
        this(context, null);
    }

    public SoftKeySpec1NumLayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftKeySpec1NumLayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public SoftKey[] initSoftKeys() {
        SoftKey[] result = new SoftKey[11];
        for (int i = 0; i < result.length; i++) {
            SoftKey btn = new SoftKey();
            if (i == result.length - 1) {
                btn.setText("00");
            } else {
                btn.setText(String.valueOf(i));
            }
            result[i] = btn;
        }
        return result;
    }

    @Override
    public SoftKey[] measureSoftKeysPos(SoftKey[] softKeys) {
        for (int i = 1; i < softKeys.length; i++) {
            softKeys[i].setX(blockWidth / 2 + ((i - 1) % (col - 1)) * blockWidth);
            softKeys[i].setY(blockHeight / 2 + ((i - 1) / (col - 1) % row) * blockHeight);
            softKeys[i].setWidth(blockWidth - 2 * gapWidth);
            softKeys[i].setHeight(blockHeight - 2 * gapHeight);
        }

        softKeys[0].setX(blockWidth / 2 + blockWidth);
        softKeys[0].setY(blockHeight / 2 + 3 * blockHeight);
        softKeys[0].setWidth(blockWidth - 2 * gapWidth);
        softKeys[0].setHeight(blockHeight - 2 * gapHeight);
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

        //画软键盘的10个数字按钮
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
            delBtn.setX(blockWidth / 2 + ((col - 2) % col) * blockWidth);
            delBtn.setY(blockHeight / 2 + ((row - 2) % row) * blockHeight);

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
            mKeyMin.setY(blockHeight + ((row - 3) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyMin);

        drawHold(canvas);
    }

    private void drawHold(Canvas canvas) {
        int holdWidth = blockWidth * 4 / 5;
        if (mKeyHold == null) {
            mKeyHold = new SoftKey();
            mKeyHold.setText("全仓");
            mKeyHold.setHeight(blockHeight - gapHeight * 2);
            mKeyHold.setWidth(holdWidth - gapWidth * 2);
            mKeyHold.setX(holdWidth / 2);
            mKeyHold.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold);

        if (mKeyHold2 == null) {
            mKeyHold2 = new SoftKey();
            mKeyHold2.setText("2/3仓");
            mKeyHold2.setHeight(blockHeight - gapHeight * 2);
            mKeyHold2.setWidth(holdWidth - gapWidth * 2);
            mKeyHold2.setX(holdWidth / 2 + holdWidth);
            mKeyHold2.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold2);

        if (mKeyHold3 == null) {
            mKeyHold3 = new SoftKey();
            mKeyHold3.setText("半仓");
            mKeyHold3.setHeight(blockHeight - gapHeight * 2);
            mKeyHold3.setWidth(holdWidth - gapWidth * 2);
            mKeyHold3.setX(holdWidth / 2 + 2 * holdWidth);
            mKeyHold3.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold3);

        if (mKeyHold4 == null) {
            mKeyHold4 = new SoftKey();
            mKeyHold4.setText("1/3仓");
            mKeyHold4.setHeight(blockHeight - gapHeight * 2);
            mKeyHold4.setWidth(holdWidth - gapWidth * 2);
            mKeyHold4.setX(holdWidth / 2 + 3 * holdWidth);
            mKeyHold4.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold4);

        if (mKeyHold5 == null) {
            mKeyHold5 = new SoftKey();
            mKeyHold5.setText("1/4仓");
            mKeyHold5.setHeight(blockHeight - gapHeight * 2);
            mKeyHold5.setWidth(holdWidth - gapWidth * 2);
            mKeyHold5.setX(holdWidth / 2 + 4 * holdWidth);
            mKeyHold5.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold5);
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

        needRefresh = needRefresh || mKeyHold.updatePressed(eventX, eventY);
        needRefresh = needRefresh || mKeyHold2.updatePressed(eventX, eventY);
        needRefresh = needRefresh || mKeyHold3.updatePressed(eventX, eventY);
        needRefresh = needRefresh || mKeyHold4.updatePressed(eventX, eventY);
        needRefresh = needRefresh || mKeyHold5.updatePressed(eventX, eventY);

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

        mKeyHold.setPreessed(false);
        mKeyHold2.setPreessed(false);
        mKeyHold3.setPreessed(false);
        mKeyHold4.setPreessed(false);
        mKeyHold5.setPreessed(false);

        if (mKeyHold.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onAllPosition();
            }
            return true;
        }

        if (mKeyHold2.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onTwoThirdsPosition();
            }
            return true;
        }

        if (mKeyHold3.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onHalfPosition();
            }
            return true;
        }

        if (mKeyHold4.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onOneThirdsPosition();
            }
            return true;
        }

        if (mKeyHold5.inRange(eventX, eventY)) {
            if (listener != null) {
                listener.onOneFourthsPosition();
            }
            return true;
        }
        return super.handleTouchUp(eventX, eventY, action);
    }

}
