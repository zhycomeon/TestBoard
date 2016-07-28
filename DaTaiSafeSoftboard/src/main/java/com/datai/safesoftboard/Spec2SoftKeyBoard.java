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
public class Spec2SoftKeyBoard extends BaseSoftKeyBoard {

    private static final String TAG = Spec2SoftKeyBoard.class.getSimpleName();

    private SoftKeySpec2NumLayView numLayView;

    public Spec2SoftKeyBoard(Context context) {
        this(context, null);
    }

    public Spec2SoftKeyBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("deprecation")
    public Spec2SoftKeyBoard(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        contentView = View.inflate(context, R.layout.layout_keyboard_spec2, null);
        colseSoftKey = (ImageView) contentView.findViewById(R.id.softkeyBoard_colse);
        switchType = (ImageView) contentView.findViewById(R.id.softkeyBoard_switchType);
        softKeyBoard_tip = (TextView) contentView.findViewById(R.id.softKeyBoard_tip);
        softKeyBoard_tip.setTextColor(Color.BLACK);
        numLayView = (SoftKeySpec2NumLayView) contentView.findViewById(R.id.keyBoard_Number_spec2);
        contentView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        numLayView.setSoftKeyListener(this);

        softKeyHeight = contentView.getMeasuredHeight();
        decorViewHeight = decorView.getHeight();

        switchType.setOnClickListener(this);
        colseSoftKey.setOnClickListener(this);

//		setFocusable(true);
        setOutsideTouchable(false);
        setContentView(contentView);

        setHeight(softKeyHeight);
    }


    @Override
    public void setBusinessTips(String businessStr) {
        if (softKeyBoard_tip != null) {
            softKeyBoard_tip.setText(businessStr);
        }
    }

    @Override
    public void onJMEAdd() {
        if (edit != null) {
            edit.processKeyValue(BusinessType.BUSINESS_TYPE_INCREASE, "");
        }
    }

    @Override
    public void onJMEMin() {
        if (edit != null) {
            edit.processKeyValue(BusinessType.BUSINESS_TYPE_DECREASE, "");
        }
    }

}
