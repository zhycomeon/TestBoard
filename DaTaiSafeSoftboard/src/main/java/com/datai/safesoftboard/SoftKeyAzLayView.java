package com.datai.safesoftboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;



/**
 * 字母键盘
 */
public class SoftKeyAzLayView extends SoftKeyView{


	/**
	 * 数字键盘的分布4行 -10列-9列-9列(包含2个其他的按钮)
	 */
	private int OneCol=10;
	private int twoCol=9;
	private int row=4;
	private SoftKey capsLockBtn;
	private SoftKey mBlankBtn;
	//private SoftKey [] punctKeys;

	private boolean capsLockPressed = false;
	

	public SoftKeyAzLayView(Context context) {
		this(context, null);
	}

	public SoftKeyAzLayView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SoftKeyAzLayView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	public SoftKey[] initSoftKeys() {
		SoftKey [] result=new SoftKey[26];
		char cha='a';
//		for(int index=0;index<result.length;index++,cha++){
//			SoftKey sofkey=new SoftKey();
//			sofkey.setText(String.valueOf(cha));
//			result[index]=sofkey;
//		}

		//add by zhangyang
		//q
		SoftKey sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 16)));
		result[0]=sofkey;
		//w
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 22)));
		result[1]=sofkey;
		//e
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 4)));
		result[2]=sofkey;
		//r
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 17)));
		result[3]=sofkey;
		//t
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 19)));
		result[4]=sofkey;
		//y
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 24)));
		result[5]=sofkey;
		//u
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 20)));
		result[6]=sofkey;
		//i
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 8)));
		result[7]=sofkey;
		//o
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 14)));
		result[8]=sofkey;
		//p
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 15)));
		result[9]=sofkey;
		//a
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 0)));
		result[10]=sofkey;
		//s
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 18)));
		result[11]=sofkey;
		//d
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 3)));
		result[12]=sofkey;
		//f
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 5)));
		result[13]=sofkey;
		//g
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 6)));
		result[14]=sofkey;
		//h
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 7)));
		result[15]=sofkey;
		//j
		 sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 9)));
		result[16]=sofkey;

		//k
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 10)));
		result[17]=sofkey;
		//l
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 11)));
		result[18]=sofkey;
		//z
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 25)));
		result[19]=sofkey;
		//x
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 23)));
		result[20]=sofkey;
		//c
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 2)));
		result[21]=sofkey;
		//v
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 21)));
		result[22]=sofkey;
		//b
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 1)));
		result[23]=sofkey;
		//n
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 13)));
		result[24]=sofkey;
		//m
		sofkey=new SoftKey();
		sofkey.setText(String.valueOf((char)(cha + 14)));
		result[25]=sofkey;


		//punctKeys=initPunctKeys();
		return result;
	}

	@Override
	public SoftKey[] measureSoftKeysPos(SoftKey[] softKeys) {
		if(softKeys==null){
			return null;
		}

		//第一行10个字符按钮
		for(int index=0;index<OneCol;index++){
			softKeys[index].setX(blockWidth/2+(index%OneCol)*blockWidth);
			softKeys[index].setY(blockHeight/2);
			softKeys[index].setWidth(blockWidth - gapWidth*2);
			softKeys[index].setHeight(blockHeight - gapHeight*2);
		}

		//第二行9个字符按钮
		for(int index=OneCol;index<OneCol+twoCol;index++){
			softKeys[index].setX((index-(OneCol-1))* blockWidth);
			softKeys[index].setY(blockHeight*3/2);
			softKeys[index].setWidth(blockWidth - gapWidth*2);
			softKeys[index].setHeight(blockHeight - gapHeight*2);
		}

		//第三行7个字符按钮
		for(int index=OneCol+twoCol;index<softKeys.length;index++){
			softKeys[index].setX((index-(OneCol+twoCol-1)+1)*blockWidth);
			softKeys[index].setY(blockHeight*5/2);
			softKeys[index].setWidth(blockWidth - gapWidth*2);
			softKeys[index].setHeight(blockHeight - gapHeight*2);
		}
		
		if(capsLockBtn==null){
			capsLockBtn=new SoftKey();
			capsLockBtn.setWidth(blockWidth*3/2 - gapWidth*2);
			capsLockBtn.setKeyType(SoftKey.KeyType.ICON);
			capsLockBtn.setIcon(R.drawable.capslock);
			capsLockBtn.setX(blockWidth*3/4);
			capsLockBtn.setY(blockHeight*5/2);
			capsLockBtn.setHeight(blockHeight - gapHeight*2);
		}
		
		if(delBtn==null){
			delBtn=new SoftKey();
			delBtn.setWidth(blockWidth*3/2 - gapWidth*2);
			delBtn.setKeyType(SoftKey.KeyType.ICON);
			delBtn.setIcon(R.drawable.ic_softkey_delete);
			delBtn.setX(getWidth()-blockWidth*3/4);
			delBtn.setY(blockHeight*5/2);
			delBtn.setHeight(blockHeight - gapHeight*2);
		}
		
//		for(int index=0;index<punctKeys.length;index++){
//			punctKeys[index].setX(index*punctKeys[index].getWidth()+(blockWidth*3/4));
//			punctKeys[index].setY(row*blockHeight-blockHeight/2);
//		}

		if(mBlankBtn == null){
			mBlankBtn = new SoftKey();
			mBlankBtn.setWidth(blockWidth*15/2 - gapWidth*2);
			mBlankBtn.setText("大泰安全键盘");
			mBlankBtn.setX(blockWidth*15/4);
			mBlankBtn.setY(blockHeight*7/2);
			mBlankBtn.setHeight(blockHeight - gapHeight*2);
		}
		
		if(confirmBtn==null){
			confirmBtn=new SoftKey();
			confirmBtn.setWidth(blockWidth*5/2 - gapWidth*2);
			confirmBtn.setText("确定");
			confirmBtn.setX(getWidth()-blockWidth*5/4);
			confirmBtn.setY(blockHeight*7/2);
			confirmBtn.setHeight(blockHeight - gapHeight*2);
		}
		
		return softKeys;
	}

	@Override
	public int measureBlockWidth(int keyBoardwidth) {
		// TODO Auto-generated method stub
		return keyBoardwidth/OneCol;
	}

	@Override
	public int measureBlockHeight(int keyBoardHeight) {
		// TODO Auto-generated method stub
		return keyBoardHeight/row;
	}

	@Override
	public void drawSoftKeysPos(Canvas canvas, SoftKey[] softKeys) {
		// TODO Auto-generated method stub
		if(softKeys==null){
			return ;
		}

		canvas.drawColor(0xffcccccc);
		for(int index=0;index<softKeys.length;index++){
			drawSoftKey(canvas, softKeys[index]);
		}
		
//		for(int index=0;index<punctKeys.length;index++){
//			drawSoftKey(canvas, punctKeys[index]);
//		}
		
//		canvas.drawLine(0, 0, getWidth(), 0, keyBorderPaint);
//		canvas.drawLine(0, blockHeight, getWidth(), blockHeight, keyBorderPaint);
//		canvas.drawLine(0, blockHeight*(row-2), getWidth(),blockHeight*(row-2), keyBorderPaint);
//		canvas.drawLine(0, blockHeight*(row-1), getWidth(),blockHeight*(row-1), keyBorderPaint);
//		for(int index=0;index<OneCol;index++){
//			int x=(index+1)*blockWidth;
//			canvas.drawLine(x, 0, x, blockHeight, keyBorderPaint);
//		}
//
//		for(int index=0;index<=twoCol;index++){
//			int x=index*blockWidth+blockWidth/2;
//			canvas.drawLine(x, blockHeight, x, blockHeight*2, keyBorderPaint);
//		}
//
//		for(int index=0;index<=softKeys.length-twoCol-OneCol;index++){
//			int x=(index+1)*blockWidth+blockWidth/2;
//			canvas.drawLine(x, blockHeight*2, x, blockHeight*3, keyBorderPaint);
//		}

//		//draw punctKeys bg
//		for(int index=4;index<5;index++){
//			int x=(index+1)*blockWidth*3/2;
//			canvas.drawLine(x, blockHeight*3, x, blockHeight*4, keyBorderPaint);
//		}
		
		drawSoftKey(canvas, capsLockBtn);
		drawSoftKey(canvas, delBtn);
		drawSoftKey(canvas, mBlankBtn);
		drawSoftKey(canvas, confirmBtn);
		
		for(int index=0;index<softKeys.length;index++){
			if(softKeys[index].isPreessed())
			drawSoftKeyPressBg(canvas, softKeys[index]);
		}
		
	}
	
	@Override
	public boolean handleKeyTouching(int eventX, int eventY, int action) {
		// TODO Auto-generated method stub
		boolean needRefresh=super.handleKeyTouching(eventX, eventY, action);
		if(!needRefresh){
			needRefresh= mBlankBtn.updatePressed(eventX, eventY);
			if(capsLockBtn.inRange(eventX, eventY)){
				if(action== MotionEvent.ACTION_DOWN){

				}
			}
			needRefresh=needRefresh || capsLockBtn.updatePressed(eventX,eventY);
		}
		return needRefresh;
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public boolean handleTouchUp(int eventX, int eventY, int action) {
		// TODO Auto-generated method stub
		boolean isDeal=super.handleTouchUp(eventX, eventY, action);
		if(!isDeal){
			if(capsLockBtn.inRange(eventX, eventY)){
				isDeal=true;
				capsLockPressed = !capsLockPressed;
				for(int index=0;index<softKeys.length;index++){
					String txt=softKeys[index].getText();
					txt= capsLockPressed ?txt.toUpperCase():txt.toLowerCase();
					softKeys[index].setText(txt);
				}
				capsLockBtn.setPreessed(false);
			}else if(mBlankBtn.inRange(eventX,eventY)){
				if(listener != null){
					SoftKey key = new SoftKey();
					key.setText(" ");
					listener.onPressed(key);
				}

				return true;
			}
		}
		return isDeal;
	}

	/**
	 * 初始化指定的标点字符
	 * @return
	 */
	public SoftKey [] initPunctKeys(){
		String puncts="@#*./";
		SoftKey [] result=new SoftKey[puncts.length()];
		for(int index=0;index<result.length;index++){
			SoftKey softkey=new SoftKey();
			softkey.setText(String.valueOf(puncts.charAt(index)));
			softkey.setWidth(blockWidth*3/2);
			softkey.setHeight(blockHeight);
			result[index]=softkey;
		}
		return result;
	}
	
	@Override
	public SoftKey obtainTouchSoftKey(int eventX, int eventY) {
		// TODO Auto-generated method stub
		SoftKey softey=super.obtainTouchSoftKey(eventX, eventY);
		//if(softey==null){
		//	return mBlackBtn;
//			for (int index = 0; index < punctKeys.length; index++) {
//				if (punctKeys[index].inRange(eventX, eventY)) {
//					return punctKeys[index];
//				}
//			}
		//}
		return softey;
	}

	/**
	 * 更新指定的字符集状态
	 * @param eventX
	 * @param eventY
	 * @param action
	 * @return
	 */
	public boolean updatePunctKeyState(int eventX, int eventY, int action){
		boolean isrefresh=false;
//		for (int index = 0; index < punctKeys.length; index++) {
//			if (isrefresh) {
//				punctKeys[index].setPreessed(false);
//			} else {
//				isrefresh = punctKeys[index].updatePressed(eventX, eventY);
//			}
//		}

		if (isrefresh) {
			mBlankBtn.setPreessed(false);
		} else {
			isrefresh = mBlankBtn.updatePressed(eventX, eventY);
		}
		return isrefresh;
	}
	
	@Override
	public void resetSoftKeysState() {
		// TODO Auto-generated method stub
		super.resetSoftKeysState();
//		for (int index = 0; index < punctKeys.length; index++) {
//			punctKeys[index].setPreessed(false);
//		}
		mBlankBtn.setPreessed(false);
	}

}
