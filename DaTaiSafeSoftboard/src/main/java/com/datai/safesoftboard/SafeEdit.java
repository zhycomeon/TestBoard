package com.datai.safesoftboard;


import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.datai.safesoftboard.Utils.DESUtils;
import com.datai.safesoftboard.Utils.ScreenUtils;
import com.datai.safesoftboard.interfaces.IBusinessProcess;
import com.datai.safesoftboard.interfaces.IBusinessTips;
import com.datai.safesoftboard.interfaces.IUpdateEncodeStr;

import java.lang.reflect.Field;
import java.text.DecimalFormat;


public class SafeEdit extends EditText implements OnDismissListener,IUpdateEncodeStr {

//	private DecimalFormat mDecimalFormat=new DecimalFormat(".00");

	private String mTwoDecimalFormat ;
	protected BaseSoftKeyBoard softKeyBoard;
	protected ViewGroup mRootView;

	protected int mMarginTop = 0;

	protected StringBuilder mEncodeStr;
	protected StringBuilder inputStr;

	private  float mMinUnit = 1;
	private int mMaxLength;
	private boolean bOpenSystemInput = false;
	/*
		mType: 0 代表输入密码的安全键盘；1 达标输入手数的键盘 ;2代表输入价格的键盘
	 */
	protected int mType = 0;

	private IBusinessProcess mBusinessListener = null;
	private IBusinessTips    mBusinessTipsListener = null;
	public SafeEdit(Context context) {
		this(context,null);
		//initSafeEdit(context);
	}

	public SafeEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSafeEdit(context,attrs);
	}

	public SafeEdit(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initSafeEdit(context,attrs);
	}
	
	public void initSafeEdit(Context context,AttributeSet attrs){
		//setOnFocusChangeListener(this);
		mEncodeStr = new StringBuilder();
		inputStr = new StringBuilder();
		mTwoDecimalFormat = context.getString(R.string.str_two_decimal_places);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EditTextType);

		mType = ta.getInteger(R.styleable.EditTextType_inputType,0);
		//addTextChangedListener(new EditChangedListener());
		mMaxLength = getMaxLength();
	}

	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==MotionEvent.ACTION_UP){
			requestFocus();
			setFocusable(true);
			setSelection(length());
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.hideSoftInputFromWindow(this.getWindowToken(), 0);

			this.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (softKeyBoard == null) {
						switch(mType){
							case 0:
								softKeyBoard=SoftKeyBoardManager.getSoftKeyBoard(getContext(),mType);//new SafeSoftKeyBoard(getContext());
								mEncodeStr.delete(0,mEncodeStr.length());
								setText("");
								inputStr.delete(0,inputStr.length());

								break;
							case 1:
								softKeyBoard=SoftKeyBoardManager.getSoftKeyBoard(getContext(),mType);//new Spec1SoftKeyBoard(getContext());
								if(getText().toString().equals("0")){
									setText("");
								}
								mEncodeStr.delete(0,mEncodeStr.length());
								break;
							case 2:
								softKeyBoard=SoftKeyBoardManager.getSoftKeyBoard(getContext(),mType);//new Spec2SoftKeyBoard(getContext());
								inputStr.replace(0,inputStr.length(),getText().toString());
								mEncodeStr.delete(0,mEncodeStr.length());
								break;
							case 3:
								softKeyBoard=SoftKeyBoardManager.getSoftKeyBoard(getContext(),mType);//new Spec2SoftKeyBoard(getContext());
								inputStr.replace(0,inputStr.length(),getText().toString());
								mEncodeStr.delete(0,mEncodeStr.length());
								break;
							case 4:
								softKeyBoard =SoftKeyBoardManager.getSoftKeyBoard(getContext(),mType);
								inputStr.replace(0,inputStr.length(),getText().toString());
								mEncodeStr.delete(0,mEncodeStr.length());
								break;
						}

						updateBusinessTips();
						softKeyBoard.setEdit(SafeEdit.this);
						softKeyBoard.setOnDismissListener(SafeEdit.this);
						softKeyBoard.show();

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
						int titleHeight = ((Activity)getContext()).findViewById(Window.ID_ANDROID_CONTENT).getTop();
						int editBottom = viewY + SafeEdit.this.getHeight();
						if(screenHeight - editBottom < boardHeight) {
							mMarginTop = boardHeight - screenHeight  + editBottom;
							showAnimator();
						}
					}
				}
			},500);

		}
		return true;
	}

	@Override
	public void onDismiss() {
		if(softKeyBoard != null) {
			softKeyBoard.recycle();
			softKeyBoard = null;
		}
		hideAnimator();
		requestFocus();
		setFocusable(true);

		if(bOpenSystemInput) {
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

	public void setSystemInputSwitch(boolean bSwitch){
		bOpenSystemInput = bSwitch;
	}

	public int getSoftKeyboardHeight(){
		if(softKeyBoard != null){
			return softKeyBoard.getSoftKeyboardHeight();
		}

		return 0;
	}

	public void updateBusinessTips(){
		if(mBusinessTipsListener != null){
			String tips = mBusinessTipsListener.getBusinessTips(this);
			if(softKeyBoard != null){
				softKeyBoard.setBusinessTips(tips);
			}
		}
	}

	public void setRootView(ViewGroup root){
		mRootView = root;
	}

	public int getType(){
		return mType;
	}
	private void showAnimator( ){
		if(mRootView == null)
			return ;
		ValueAnimator valueAnimator = ValueAnimator.ofInt(0,mMarginTop);
		valueAnimator.setDuration(100);

		valueAnimator.setInterpolator(new LinearInterpolator());

		valueAnimator.setEvaluator(new TypeEvaluator<Integer>()
		{
			// fraction = t / duration
			@Override
			public Integer evaluate(float fraction, Integer startValue,
									Integer endValue)
			{
				return (int)(-mMarginTop*fraction);
			}
		});
		valueAnimator.start();


		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				int top = (Integer)animation.getAnimatedValue();

				ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mRootView.getLayoutParams();
				params.topMargin = top;
				mRootView.setLayoutParams(params);
			}
		});

	}

	private void hideAnimator(){
		if(mRootView == null)
			return ;
		ValueAnimator valueAnimator = ValueAnimator.ofInt(0,mMarginTop);
		valueAnimator.setDuration(100);
		//valueAnimator.setTarget(mRootView);
		valueAnimator.setInterpolator(new LinearInterpolator());

		valueAnimator.setEvaluator(new TypeEvaluator<Integer>()
		{
			// fraction = t / duration
			@Override
			public Integer evaluate(float fraction, Integer startValue,
									Integer endValue)
			{
				return (int)(-mMarginTop*(1-fraction));
			}
		});
		valueAnimator.start();


		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				int top = (Integer)animation.getAnimatedValue();
				ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mRootView.getLayoutParams();
				params.topMargin = top;
				mRootView.setLayoutParams(params);
			}
		});
	}

	@Override
	public void updateEncodeString(String encodeStr) {
		mEncodeStr.replace(0,mEncodeStr.length(),encodeStr);
	}

	@Override
	public String getEncodeString() {
		return mEncodeStr.toString();
	}

	public void setMinUnit(float unit){
		mMinUnit = unit;
	}

	public void setBusinessListener(IBusinessProcess listener){
		mBusinessListener = listener;
	}

	public void setBusinessTipsListener(IBusinessTips listener){
		mBusinessTipsListener = listener;
	}



	public void processKeyValue(BusinessType type,String keyValue){
			switch(type){
				case BUSINESS_TYPE0:
					if(mBusinessListener != null) {
						mBusinessListener.commit(this);
					}
					break;
				case BUSINESS_TYPE1:
					if(mBusinessListener != null) {
						mBusinessListener.doBusiness(this,BusinessType.BUSINESS_TYPE1);
					}
						break;
				case BUSINESS_TYPE2:
					if(mBusinessListener != null) {
						mBusinessListener.doBusiness(this,BusinessType.BUSINESS_TYPE2);
					}
					break;
				case BUSINESS_TYPE3:
					if(mBusinessListener != null) {
						mBusinessListener.doBusiness(this,BusinessType.BUSINESS_TYPE3);
					}
					break;
				case BUSINESS_TYPE4:
					if(mBusinessListener != null) {
						mBusinessListener.doBusiness(this,BusinessType.BUSINESS_TYPE4);
					}
					break;
				case BUSINESS_TYPE5:
					if(mBusinessListener != null) {
						mBusinessListener.doBusiness(this,BusinessType.BUSINESS_TYPE5);
					}
					break;
				case BUSINESS_TYPE6:
					if(TextUtils.isEmpty(keyValue)){
						break;
					}
					if(mType == 0 || mType == 4){
						if(inputStr.length()>=mMaxLength)
							return ;
						inputStr.append(keyValue);
						String oriStr = inputStr.toString();
//						String desString = DESUtils.getInstance().encode(oriStr);
//						updateEncodeString(desString);
//
//						setText(desString.substring(0, inputStr.length()));
						setText(inputStr);
						setSelection(inputStr.length());
					}else if(mType == 1){
						if(inputStr.length()==0 && (keyValue.equalsIgnoreCase("00")|| keyValue.equalsIgnoreCase("0"))){
							return ;
						}
						if(inputStr.length()>=mMaxLength)
							return ;
						if(inputStr.toString().equals("0")){
							inputStr.delete(0,inputStr.length());
							inputStr.append(keyValue);
						}else {
							inputStr.append(keyValue);
						}
						setText(inputStr);
						setSelection(inputStr.length());

					}else if(mType == 2){
						if(inputStr.length()>=mMaxLength)
							return ;
						if(!keyValue.equalsIgnoreCase(".")) {
//							if(inputStr.toString().contains(".") && (inputStr.indexOf(".")+3) == inputStr.length()){
//								break;
//							}
							inputStr.append(keyValue);

							setText(inputStr);
							setSelection(inputStr.length());
						}else{
							if(inputStr.length() > 0 && !inputStr.toString().contains(".")){
								inputStr.append(keyValue);
								setText(inputStr);
								setSelection(inputStr.length());
							}
						}
					}else if(mType == 3){
						if(inputStr.length()>=mMaxLength)
							return ;
						if(!keyValue.equals("0") && !keyValue.equals(".")){
							inputStr.append(keyValue);
						} else if(keyValue.equals(".") && (inputStr.length() > 0 && !inputStr.toString().contains("."))){
							inputStr.append(keyValue);

						}else if(inputStr.length() > 0 &&  keyValue.equals("0")){
							inputStr.append(keyValue);
						}
						setText(inputStr);
						setSelection(inputStr.length());
					}
					break;
				case BUSINESS_TYPE7:
					if(mBusinessListener != null) {
						mBusinessListener.doBusiness(this,BusinessType.BUSINESS_TYPE7);
					}
					/*
					if(mType == 0){

					}else if(mType == 2){
						if (!TextUtils.isEmpty(inputStr.toString())) {
							long current = Long.parseLong(inputStr.toString());
							current += mMinUnit;
							inputStr = inputStr.replace(0, inputStr.length(), String.valueOf(current));
							setText(inputStr);
							setSelection(inputStr.length());
						}
					}else if(mType == 1){
						if (!TextUtils.isEmpty(inputStr.toString())) {
							Double current = Double.parseDouble(inputStr.toString());
							current += mMinUnit;
							inputStr = inputStr.replace(0, inputStr.length(), String.valueOf(current));
							setText(inputStr);
							setSelection(inputStr.length());
						}
					}
					*/
					break;
				case BUSINESS_TYPE8:

					if(mBusinessListener != null) {
						mBusinessListener.doBusiness(this,BusinessType.BUSINESS_TYPE8);
					}
					/*
					if(mType == 0){

					}else if(mType == 2){
						if (!TextUtils.isEmpty(inputStr.toString())) {
							long current = Long.parseLong(inputStr.toString());
							if (current <=mMinUnit) {
								current = 0;
							} else {
								current -= 1;
							}
							inputStr = inputStr.replace(0, inputStr.length(), String.valueOf(current));
							setText(inputStr);
							setSelection(inputStr.length());
						}
					}else if(mType == 1){
						if (!TextUtils.isEmpty(inputStr.toString())) {
							double current = Double.parseDouble(inputStr.toString());
							if (current <= mMinUnit) {
								current = 0;
							} else {
								current -= mMinUnit;
							}

							inputStr = inputStr.replace(0, inputStr.length(), String.valueOf(current));
							setText(inputStr);
							setSelection(inputStr.length());
						}
					}
					*/
					break;
				case BUSINESS_TYPE9:
//					if(mType == 0){
//						if(!TextUtils.isEmpty(inputStr.toString())){
//							inputStr.deleteCharAt(inputStr.length()-1);
//							String desString = DESUtils.getInstance().encode(inputStr.toString());
//							updateEncodeString(desString);
//
//							setText(desString.substring(0, inputStr.length()));
//							setSelection(inputStr.length());
//						}
//					}else if(mType == 1 || mType == 2){
						if(!TextUtils.isEmpty(inputStr.toString())){
							setText(inputStr.deleteCharAt(inputStr.length()-1));
							setSelection(inputStr.length());
						}
//					}
					break;
			}


	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		if(text != null && inputStr != null) {
			inputStr = inputStr.replace(0, inputStr.length(), text.toString());
		}
	}

	public int getMaxLength()
	{
		int length =0;
		try
		{
			InputFilter[] inputFilters = getFilters();
			for(InputFilter filter:inputFilters)
			{
				Class<?> c = filter.getClass();
				if(c.getName().equals("android.text.InputFilter$LengthFilter"))
				{
					Field[] f = c.getDeclaredFields();
					for(Field field:f)
					{
						if(field.getName().equals("mMax"))
						{
							field.setAccessible(true);
							length = (Integer)field.get(filter);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		mMaxLength = length;
		return length;
	}

}
