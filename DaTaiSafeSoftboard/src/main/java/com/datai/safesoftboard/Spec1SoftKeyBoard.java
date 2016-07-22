package com.datai.safesoftboard;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import android.widget.ImageView;
import android.widget.TextView;


/**
 * 自定义软件键盘
 */
public class Spec1SoftKeyBoard extends BaseSoftKeyBoard{

	private static final String TAG=Spec1SoftKeyBoard.class.getSimpleName();
	private SoftKeySpec1NumLayView numLayView;

	public Spec1SoftKeyBoard(Context context) {
		this(context,null);
	}

	public Spec1SoftKeyBoard(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	@SuppressWarnings("deprecation")
	public Spec1SoftKeyBoard(Context context, AttributeSet attrs,
							 int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//decorView=((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
		contentView=View.inflate(context, R.layout.layout_keyboard_spec1, null);
		colseSoftKey=(ImageView) contentView.findViewById(R.id.softkeyBoard_colse);
		switchType=(ImageView) contentView.findViewById(R.id.softkeyBoard_switchType);
		softKeyBoard_tip = (TextView)contentView.findViewById(R.id.softKeyBoard_tip);
		softKeyBoard_tip.setTextColor(Color.BLACK);
		numLayView=(SoftKeySpec1NumLayView) contentView.findViewById(R.id.keyBoard_Number_spec1);
		contentView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		numLayView.setSoftKeyListener(this);

		softKeyHeight=contentView.getMeasuredHeight();
		decorViewHeight=decorView.getHeight();

		switchType.setOnClickListener(this);
		colseSoftKey.setOnClickListener(this);

		setFocusable(true);
		setOutsideTouchable(false);
		setContentView(contentView);
//        setBackgroundDrawable(new BitmapDrawable());
//        setWidth(decorView.getWidth());
		setHeight(softKeyHeight);
//		setAnimationStyle(android.R.style.Animation_InputMethod);
		//contentView.getViewTreeObserver().addOnGlobalLayoutListener(contetviewLayoutListener);
	}

	@Override
	public void setBusinessTips(String businessStr) {
		if(softKeyBoard_tip != null) {
			softKeyBoard_tip.setText(businessStr);
		}
	}

	@Override
    public void onJMEAdd() {
		if (edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE7, "");
		}
    }

    @Override
    public void onJMEMin() {
		if (edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE8, "");
		}
    }

	@Override
	public void onConfirm() {
		if(edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE0,"");
		}
		super.onConfirm();
	}

	@Override
	public void onAllPosition() {
		if(edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE1,"");
		}
	}

	@Override
	public void onHalfPosition() {
		if(edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE2,"");
		}
	}

	@Override
	public void onOneThirdsPosition() {
		if(edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE3,"");
		}
	}

	@Override
	public void onTwoThirdsPosition() {
		if(edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE4,"");
		}
	}

	@Override
	public void onOneFourthsPosition() {
		if(edit != null) {
			edit.processKeyValue(BusinessType.BUSINESS_TYPE5,"");
		}
	}
}
