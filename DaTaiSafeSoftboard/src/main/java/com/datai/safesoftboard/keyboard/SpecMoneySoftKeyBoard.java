package com.datai.safesoftboard.keyboard;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.datai.safesoftboard.R;
import com.datai.safesoftboard.keyboardview.SoftKeyMoneyNumLayView;


/**
 * 自定义软件键盘
 */
public class SpecMoneySoftKeyBoard extends BaseSoftKeyBoard {

	private static final String TAG=SpecMoneySoftKeyBoard.class.getSimpleName();

	private SoftKeyMoneyNumLayView numLayView;

	private float mStep = 0f,mLimitUp = 0f,mLimitDown = 0f;

	public SpecMoneySoftKeyBoard(Context context) {
		this(context,null);
	}

	public SpecMoneySoftKeyBoard(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	@SuppressWarnings("deprecation")
	public SpecMoneySoftKeyBoard(Context context, AttributeSet attrs,
								 int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		contentView=View.inflate(context, R.layout.layout_keyboard_money, null);
		colseSoftKey=(ImageView) contentView.findViewById(R.id.softkeyBoard_colse);
		switchType=(ImageView) contentView.findViewById(R.id.softkeyBoard_switchType);
		softKeyBoard_tip = (TextView)contentView.findViewById(R.id.softKeyBoard_tip);
		softKeyBoard_tip.setTextColor(Color.BLACK);
		numLayView=(SoftKeyMoneyNumLayView) contentView.findViewById(R.id.keyBoard_Number_money);
		contentView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		numLayView.setSoftKeyListener(this);

		softKeyHeight=contentView.getMeasuredHeight();
		decorViewHeight=decorView.getHeight();

		switchType.setOnClickListener(this);
		colseSoftKey.setOnClickListener(this);

		setFocusable(true);
		setOutsideTouchable(false);
		setContentView(contentView);

		setHeight(softKeyHeight);
	}


	@Override
	public void setBusinessTips(String businessStr) {
		if(softKeyBoard_tip != null) {
			softKeyBoard_tip.setText(businessStr);
		}
	}


}
