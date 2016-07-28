package com.datai.safesoftboard;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;



import java.util.Random;

/**
 * 数字键盘布局
 */
public class SoftKeyNumLayView extends SoftKeyView {

    /**
     * 数字键盘的分布4行3列
     */
    private int row = 4;//5;
    private int col = 3;//4;

//    private SoftKey mKeyAdd;
//    private SoftKey mKeyMin;
    private SoftKey mKeyHold;
    private SoftKey mKeyHold2;
    private SoftKey mKeyHold3;
    private SoftKey mKeyHold4;

	public SoftKeyNumLayView(Context context) {
		this(context, null);
	}

	public SoftKeyNumLayView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SoftKeyNumLayView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public SoftKey[] initSoftKeys() {
		SoftKey[] result = new SoftKey[10];
		for (int i = 0; i < result.length; i++) {
			SoftKey btn = new SoftKey();
			btn.setText(String.valueOf(i));
			result[i] = btn;
		}
		return result;
	}

    @Override
    public SoftKey[] measureSoftKeysPos(SoftKey[] softKeys) {
        for (int i = 0; i < softKeys.length-4; i++) {
            softKeys[i].setX(blockWidth / 2 + (i % col) * blockWidth);
            softKeys[i].setY(blockHeight / 2 + (i / col  % row) * blockHeight);
            softKeys[i].setWidth(blockWidth - gapWidth*2);
            softKeys[i].setHeight(blockHeight - gapHeight*2);
        }
        //第三行和第四行的数字
        for(int i = softKeys.length - 4; i < softKeys.length; i++){
            softKeys[i].setX(blockWidth / 2 + (i % (col-1)) * blockWidth);
            softKeys[i].setY(blockHeight / 2 + (i / (col-1)  % (row-1)+2) * blockHeight);
            softKeys[i].setWidth(blockWidth - gapWidth*2);
            softKeys[i].setHeight(blockHeight -gapHeight*2);
        }
        return softKeys;
    }

	@Override
	public int measureBlockWidth(int keyBoardwidth) {
		return keyBoardwidth/col;
	}

	@Override
	public int measureBlockHeight(int keyBoardHeight) {
		return keyBoardHeight/row;
	}

	@Override
	public void drawSoftKeysPos(Canvas canvas, SoftKey[] softKeys) {
		if (softKeys == null) {
			return ;
		}

//		canvas.drawColor(Color.WHITE);
        canvas.drawColor(0xffcccccc);
//		//画垂直分割线
//		for (int index = 0; index < col; index++) {
//			canvas.drawLine((index + 1) * blockWidth, 0, (index + 1)* blockWidth, getHeight(), keyBorderPaint);
//		}
//
//        //画水平分割线
//        for (int index = 0; index < row; index++) {
////            if (index % 2 != 0) {
////                canvas.drawLine(0, index * blockHeight, getWidth() * (col - 1) / col, index * blockHeight, keyBorderPaint);
////            } else {
//                canvas.drawLine(0, index * blockHeight, getWidth(), index * blockHeight, keyBorderPaint);
////            }
//        }

		//画软键盘的9个数字按钮
		for (int index = 0; index < softKeys.length; index++) {
			drawSoftKey(canvas, softKeys[index]);
		}

		//创建软键盘的删除按钮
		if(delBtn==null){
			delBtn=new SoftKey();
			delBtn.setKeyType(SoftKey.KeyType.ICON);
			delBtn.setIcon(R.drawable.ic_softkey_delete);
			delBtn.setHeight(blockHeight - gapHeight*2);
			delBtn.setWidth(blockWidth - gapWidth*2);
            delBtn.setX((col - 1)*blockWidth  + blockWidth /2);
            delBtn.setY((row -2) *blockHeight + blockHeight /2);
//			softKeys[softKeys.length-1].setX(blockWidth / 2 + ((col-2)% col) * blockWidth);
//			softKeys[softKeys.length-1].setY(blockHeight / 2 + ((row-1) % row) * blockHeight);
		}

		//画软键盘的删除按钮
		drawSoftKey(canvas,delBtn);

        if (confirmBtn == null) {
            confirmBtn = new SoftKey();
            confirmBtn.setText("确定");
            confirmBtn.setHeight(blockHeight - gapHeight*2);
            confirmBtn.setWidth(blockWidth - gapWidth*2);
            confirmBtn.setX(blockWidth / 2 + (col - 1) * blockWidth);
            confirmBtn.setY(blockHeight / 2 + (row - 1) * blockHeight);
        }

        drawSoftKey(canvas, confirmBtn);

    }

    private void drawHold(Canvas canvas) {
        if (mKeyHold == null) {
            mKeyHold = new SoftKey();
            mKeyHold.setText("全仓");
            mKeyHold.setHeight(blockHeight);
            mKeyHold.setWidth(blockWidth);
            mKeyHold.setX(blockWidth / 2);
            mKeyHold.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold);

        if (mKeyHold2 == null) {
            mKeyHold2 = new SoftKey();
            mKeyHold2.setText("1/2");
            mKeyHold2.setHeight(blockHeight * 2);
            mKeyHold2.setWidth(blockWidth);
            mKeyHold2.setX(blockWidth / 2 + blockWidth);
            mKeyHold2.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold2);

        if (mKeyHold3 == null) {
            mKeyHold3 = new SoftKey();
            mKeyHold3.setText("1/2");
            mKeyHold3.setHeight(blockHeight * 2);
            mKeyHold3.setWidth(blockWidth);
            mKeyHold3.setX(blockWidth / 2 + 2 * blockWidth);
            mKeyHold3.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold3);

        if (mKeyHold4 == null) {
            mKeyHold4 = new SoftKey();
            mKeyHold4.setText("1/3");
            mKeyHold4.setHeight(blockHeight * 2);
            mKeyHold4.setWidth(blockWidth);
            mKeyHold4.setX(blockWidth / 2 + 3 * blockWidth);
            mKeyHold4.setY(blockHeight / 2 + ((row - 1) % row) * blockHeight);
        }

        drawSoftKey(canvas, mKeyHold4);
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

//        mKeyAdd.updatePressed(eventX, eventY);
//        mKeyMin.updatePressed(eventX, eventY);

        return needRefresh;
    }

    private void performQuickAdd() {

    }

    private void cancelQuickAdd() {

    }

    @Override
    public boolean handleTouchUp(int eventX, int eventY, int action) {
//        mKeyAdd.setPreessed(false);
//        mKeyMin.setPreessed(false);
//        if (mKeyAdd.inRange(eventX, eventY)) {
//            if (listener != null) {
//                listener.onJMEAdd();
//            }
//            return true;
//        }
//
//        if (mKeyMin.inRange(eventX, eventY)) {
//            if (listener != null) {
//                listener.onJMEMin();
//            }
//            return true;
//        }

        return super.handleTouchUp(eventX, eventY, action);
    }

    @Override
    public SoftKey[] makeSoftKeysRandom(SoftKey[] softKeys) {
        if(keyRandom) {
            int w;
            Random rand = new Random();
            for (int i = softKeys.length - 1; i > 0; i--) {
                w = rand.nextInt(i);
                String number = softKeys[i].getText();
                softKeys[i].setText(softKeys[w].getText());
                softKeys[w].setText(number);
            }
        }
        return softKeys;
    }
}
