package com.datai.safesoftboard;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.datai.safesoftboard.Utils.DESUtils;


/**
 * 自定义软件键盘
 */
public class SafeSoftKeyBoard extends BaseSoftKeyBoard implements
	OnCheckedChangeListener{
	
	private static final String TAG=SafeSoftKeyBoard.class.getSimpleName();

	//private SafeEdit edit;
	private RadioGroup inputTypeGroup;
//	private TextView softKeyBoard_tip;
	private SoftKeyAzLayView azLayView;
	private SoftKeyNumLayView numLayView;
	private SoftKeyPunctLayView punctLayView;

	
	public SafeSoftKeyBoard(Context context) {
		this(context,null);
	}

	public SafeSoftKeyBoard(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	@SuppressWarnings("deprecation")
	public SafeSoftKeyBoard(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//decorView=((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
		contentView=View.inflate(context, R.layout.layout_keyboard, null);
		colseSoftKey=(ImageView) contentView.findViewById(R.id.softkeyBoard_colse);
		switchType=(ImageView) contentView.findViewById(R.id.softkeyBoard_switchType);
		inputTypeGroup=(RadioGroup) contentView.findViewById(R.id.sofkeyBoard_Type);
//		softKeyBoard_tip=(TextView) contentView.findViewById(R.id.softKeyBoard_tip);
		azLayView=(SoftKeyAzLayView) contentView.findViewById(R.id.softkeyBoard_Az);
		numLayView=(SoftKeyNumLayView) contentView.findViewById(R.id.softkeyBoard_Number);
		punctLayView=(SoftKeyPunctLayView) contentView.findViewById(R.id.softkeyBoard_Punct);
		contentView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		azLayView.setSoftKeyListener(this);
		numLayView.setSoftKeyListener(this);
		punctLayView.setSoftKeyListener(this);
		switchType.setOnClickListener(this);
		colseSoftKey.setOnClickListener(this);
		inputTypeGroup.setOnCheckedChangeListener(this);
		softKeyHeight=contentView.getMeasuredHeight();
		decorViewHeight=decorView.getHeight();
		setFocusable(true);
		setOutsideTouchable(false);
		setContentView(contentView);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(decorView.getWidth());
		setHeight(softKeyHeight);
		setAnimationStyle(android.R.style.Animation_InputMethod);
		//contentView.getViewTreeObserver().addOnGlobalLayoutListener(contetviewLayoutListener);
	}

	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		updateViewByMode(mapViewModeById(checkedId));	
	}


	/**
	 * 更新显示的视图
	 * @param viewMode
	 */
	public void updateViewByMode(int viewMode){
		switch (viewMode) {
		case SoftKeyView.MODE_NUMBER:
			numLayView.setVisibility(View.VISIBLE);
			azLayView.setVisibility(View.INVISIBLE);
			punctLayView.setVisibility(View.INVISIBLE);
			break;

		case SoftKeyView.MODE_AZ:
			azLayView.setVisibility(View.VISIBLE);
			numLayView.setVisibility(View.INVISIBLE);
			punctLayView.setVisibility(View.INVISIBLE);
			break;
		case SoftKeyView.MODE_PUNCT:
			punctLayView.setVisibility(View.VISIBLE);
			numLayView.setVisibility(View.INVISIBLE);
			azLayView.setVisibility(View.INVISIBLE);
			break;
		case SoftKeyView.MODE_DEFAULT:
			azLayView.setVisibility(View.VISIBLE);
			numLayView.setVisibility(View.INVISIBLE);
			punctLayView.setVisibility(View.INVISIBLE);
			break;
		}
	}

	/**
	 * 显示输入法
	 */
	public void show(){
		super.show();
		inputTypeGroup.check(mapCheckedIdByMode(getInputType(edit)));
		//updateViewByMode(getInputType(edit));
	}

	/**
	 * 获取输入文本的类型
	 * @param edit
	 * @return
	 */
	public int getInputType(EditText edit){
		if(edit==null){
			return SoftKeyView.MODE_DEFAULT;
		}
		if(EditorInfo.TYPE_CLASS_PHONE==edit.getInputType()||edit.getInputType()==EditorInfo.TYPE_CLASS_NUMBER){
			return SoftKeyView.MODE_NUMBER;
		}else{
			return SoftKeyView.MODE_DEFAULT;
		}
	}

	/**
	 * 映射出视图模式
	 * @return
	 */
	public int mapViewModeById(int checkedId){
		if(checkedId == R.id.softBoard_Punct){
			return SoftKeyView.MODE_PUNCT;
		}else if(checkedId ==  R.id.softBoard_Az){
			return SoftKeyView.MODE_AZ;
		}else if(checkedId == R.id.softBoard_Number){
			return SoftKeyView.MODE_NUMBER;
		}else{
			return SoftKeyView.MODE_AZ;
		}
//		switch (checkedId) {
//		case R.id.softBoard_Punct:
//			return SoftKeyView.MODE_PUNCT;
//		case R.id.softBoard_Az:
//			return SoftKeyView.MODE_AZ;
//		case R.id.softBoard_Number:
//			return SoftKeyView.MODE_NUMBER;
//		default:
//			return SoftKeyView.MODE_AZ;
//		}
	}

	/**
	 * 映射出视图模式
	 * @return
	 */
	public int mapCheckedIdByMode(int viewMode){
		switch (viewMode) {
		case SoftKeyView.MODE_PUNCT:
			return R.id.softBoard_Punct;
		case SoftKeyView.MODE_AZ:
			return R.id.softBoard_Az;
		case SoftKeyView.MODE_NUMBER:
			return R.id.softBoard_Number;
		default:
			return R.id.softBoard_Az;
		}
	}
	
	public OnGlobalLayoutListener contetviewLayoutListener=new OnGlobalLayoutListener(){

		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			if(decorViewHeight==decorView.getHeight()){
				Log.i(TAG, "updateViewDraw-->decorViewHeight="+decorViewHeight);
				TranslateAnimation translate=new TranslateAnimation(0, 0, 0, softKeyHeight);
				translate.setDuration(200);
				translate.setFillAfter(true);
				decorView.startAnimation(translate);
			}else{
				Log.i(TAG, "updateViewDraw-->decorViewHeight="+decorViewHeight);
				TranslateAnimation translate=new TranslateAnimation(0, 0, -softKeyHeight, 0);
				translate.setDuration(200);
				translate.setFillAfter(true);
				decorView.startAnimation(translate);
			}
		}
		
	};

    @Override
    public void onConfirm() {
        close();
    }

	public void setViewMode(int viewMode) {
		if(viewMode!=this.viewMode){
			this.viewMode = viewMode;
			updateViewByMode(viewMode);
		}
	}
	
}
