package com.example.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.datai.safesoftboard.BusinessType;
import com.datai.safesoftboard.view.SafeEdit;
import com.datai.safesoftboard.view.SecurityPassword;
import com.datai.safesoftboard.keyboard.SoftKeyBoardManager;
import com.datai.safesoftboard.keyboard.SpecSecurityNumPasswordBoard;
import com.datai.safesoftboard.interfaces.IBusinessProcess;
import com.datai.safesoftboard.interfaces.IBusinessTips;
import com.datai.safesoftboard.interfaces.IInputComplete;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements IBusinessProcess,IBusinessTips,
        SpecSecurityNumPasswordBoard.KeyValueListener ,View.OnClickListener,IInputComplete,PopupWindow.OnDismissListener{

    DecimalFormat decmalFormat = new DecimalFormat("#######.00");
    SpecSecurityNumPasswordBoard softBoard = null;
    SecurityPassword passwordView = null;
    SafeEdit spec1SafeEdit;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (RelativeLayout) findViewById(R.id.root);

        spec1SafeEdit = (SafeEdit) findViewById(R.id.test1);
        spec1SafeEdit.setText("0");
        spec1SafeEdit.setRootView(root);
        spec1SafeEdit.setBusinessListener(this);
        spec1SafeEdit.setBusinessTipsListener(this);
        spec1SafeEdit.setMinUnit(2);


        SafeEdit spec2SafeEdit = (SafeEdit) findViewById(R.id.test2);
        spec2SafeEdit.setText("");
        spec2SafeEdit.setRootView(root);
        spec2SafeEdit.setBusinessListener(this);
        spec2SafeEdit.setBusinessTipsListener(this);
        spec2SafeEdit.setMinUnit(3);

        SafeEdit spec3SafeEdit = (SafeEdit) findViewById(R.id.test3);
        spec3SafeEdit.setText("");
        spec3SafeEdit.setRootView(root);
        spec3SafeEdit.setBusinessListener(this);
        spec3SafeEdit.setBusinessTipsListener(this);
//        spec2SafeEdit.setMinUnit(3);

        SafeEdit moneyPwd = (SafeEdit)findViewById(R.id.test_test);
        moneyPwd.setBusinessTipsListener(this);

        final SafeEdit safeEdit = (SafeEdit) findViewById(R.id.test);
        safeEdit.setRootView(root);
//        safeEdit.onTouchEvent(MotionEvent.obtain(0,0,MotionEvent.ACTION_UP,0,0,0));

        Button btn = (Button) findViewById(R.id.commit);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String desStr = safeEdit.getEncodeString();
////                String oriStr = DESUtils.getInstance().decode(desStr);
//
////                Toast.makeText(MainActivity.this,oriStr,Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this,safeEdit.getText().toString(),Toast.LENGTH_SHORT).show();
//            }
//        });

        btn.setOnClickListener(this);
        passwordView = (SecurityPassword) findViewById(R.id.input_password);
        passwordView.setOnClickListener(this);
        passwordView.setCompleteListener(this);
        passwordView.requestFocus();
        passwordView.setFocusable(true);

//        passwordView.setOnTouchListener(this);

    }


    @Override
    public String getBusinessTips(View edit) {
        String tips = "";

        int id = edit.getId();
        switch (id) {
            case R.id.test1:
                tips = String.format(getString(R.string.business_format1), 2f, 5000f, 3000f);
                break;
            case R.id.test2:
                tips = String.format(getString(R.string.business_format1), 0.5f, 2000f, 1000f);
                break;
        }
        return tips;
    }

    @Override
    public void commit(SafeEdit edit) {

    }

    @Override
    public void doBusiness(SafeEdit edit, BusinessType type) {

        switch (type) {
            case BUSINESS_TYPE_ALL:

                break;
            case BUSINESS_TYPE_HALF:

                break;
            case BUSINESS_TYPE_ONETHIRD:

                break;
            case BUSINESS_TYPE_TWOTHIRD:

                break;
            case BUSINESS_TYPE_ONEFOURTH:

                break;
            case BUSINESS_TYPE_KEYVALUE:
                break;
            case BUSINESS_TYPE_INCREASE: {
                int eType = edit.getType();
                switch (edit.getId()) {

                    case R.id.test1: {
                        long amountValue = 0;
                        if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
                            amountValue = Long.parseLong(edit.getText().toString().trim());
                        }
                        amountValue += 1;

                        edit.setText(String.valueOf(amountValue));
                        edit.setSelection(edit.length());

                    }
                    break;
                    case R.id.test2: {
                        float pricevalue = 0;
                        if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
                            pricevalue = Float.parseFloat(edit.getText().toString().trim());
                        }
                        pricevalue += 0.01;
                        edit.setText(decmalFormat.format(pricevalue));
                        edit.setSelection(edit.length());
                    }
                    break;

                }
            }
            break;
            case BUSINESS_TYPE_DECREASE: {
                switch (edit.getId()) {
                    case R.id.test1:
                        long amountValue = 0;
                        if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
                            amountValue = Long.parseLong(edit.getText().toString().trim());
                        }

                        amountValue -= 1;

                        edit.setText(String.valueOf(amountValue));
                        edit.setSelection(edit.length());
                        break;
                    case R.id.test2:
                        float pricevalue = 0.00f;
                        if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
                            pricevalue = Float.parseFloat(edit.getText().toString().trim());
                        }
                        pricevalue -= 0.01;


                        edit.setText(decmalFormat.format(pricevalue));
                        edit.setSelection(edit.length());
                        break;

                }
            }
            break;
        }
    }

    @Override
    public void getKeyValue(String value) {
        if (passwordView != null) {
            passwordView.appendValue(value);
        }
    }

    @Override
    public void delete() {
        if (passwordView != null) {
            passwordView.deleteLastValue();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.input_password) {
            view.postDelayed(new Runnable() {

                @Override
                public void run() {
//                    if (softBoard != null && !softBoard.isShowing()) {
                        softBoard = (SpecSecurityNumPasswordBoard) SoftKeyBoardManager.getSoftKeyBoard(MainActivity.this, 4);
                        softBoard.setOnKeyValueListener(MainActivity.this);
                        softBoard.show();
//                    }

                }
            }, 500);

        } else if(id==R.id.commit)
        {
//            view.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    if (softBoard != null && !softBoard.isShowing()) {
//                        softBoard.show();
//                    }
//                }
//            }, 500);
        }

    }

    @Override
    public void completeNotification() {
        Toast.makeText(this,passwordView.getPasswordContent(),Toast.LENGTH_SHORT).show();
       if(softBoard != null) {
           softBoard.dismiss();
       }
    }


    @Override
    public void onDismiss() {
        if(softBoard != null) {
            softBoard.recycle();
            softBoard = null;
        }
    }

}
