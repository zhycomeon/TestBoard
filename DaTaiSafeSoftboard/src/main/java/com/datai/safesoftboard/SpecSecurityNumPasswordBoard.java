package com.datai.safesoftboard;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 自定义软件键盘
 */
public class SpecSecurityNumPasswordBoard extends BaseSoftKeyBoard {

	private static final String TAG=SpecSecurityNumPasswordBoard.class.getSimpleName();

	private SoftKeyTradePasswordNumLayView numLayView;

	public interface KeyValueListener{
		void getKeyValue(String value);
		void delete();
	}

	private KeyValueListener mListener = null;
	public SpecSecurityNumPasswordBoard(Context context) {
		this(context,null);
	}

	public SpecSecurityNumPasswordBoard(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	@SuppressWarnings("deprecation")
	public SpecSecurityNumPasswordBoard(Context context, AttributeSet attrs,
										int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		contentView=View.inflate(context, R.layout.layout_keyboard_security_numpassword, null);
		colseSoftKey=(ImageView) contentView.findViewById(R.id.softkeyBoard_colse);
		switchType=(ImageView) contentView.findViewById(R.id.softkeyBoard_switchType);
		softKeyBoard_tip = (TextView)contentView.findViewById(R.id.softKeyBoard_tip);
		softKeyBoard_tip.setTextColor(Color.BLACK);
		numLayView=(SoftKeyTradePasswordNumLayView) contentView.findViewById(R.id.keyBoard_security_numpassword);
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
	public void show() {
		super.show();
//		showAsDropDown(decorView, 0, -getHeight());
	}

	@Override
	public void setBusinessTips(String businessStr) {
		if(softKeyBoard_tip != null) {
			softKeyBoard_tip.setText(businessStr);
		}
	}

	@Override
	public void onPressed(SoftKey softKey) {
		if(edit != null){
			super.onPressed(softKey);
		}else {
			if (mListener != null) {
				mListener.getKeyValue(softKey.getText());
			}
		}
	}


	@Override
	public void onDeleted() {
		if(edit != null){
			super.onDeleted();
		}else {
			if (mListener != null) {
				mListener.delete();
			}
		}
	}

	public void setOnKeyValueListener(KeyValueListener listener){
		mListener = listener;
	}

}
