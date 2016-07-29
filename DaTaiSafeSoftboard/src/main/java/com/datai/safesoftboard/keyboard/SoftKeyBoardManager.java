package com.datai.safesoftboard.keyboard;

import android.content.Context;

import com.datai.safesoftboard.view.SafeEdit;


/**
 * Created by zhangyang on 2016/7/19.
 */
public class SoftKeyBoardManager {
    private static BaseSoftKeyBoard softKeyBoard = null;

    public static BaseSoftKeyBoard getSoftKeyBoard(Context context, int type) {

        if (softKeyBoard != null) {
            softKeyBoard.dismiss();
            softKeyBoard = null;
        }
        if (type == SafeEdit.SOFTKEYBOARD_TYPE_PASSWORD) {
            softKeyBoard = new SafeSoftKeyBoard(context);
        } else if (type == SafeEdit.SOFTKEYBOARD_TYPE_PACKAGE) {
            softKeyBoard = new Spec1SoftKeyBoard(context);
        } else if (type == SafeEdit.SOFTKEYBOARD_TYPE_PRICE) {
            softKeyBoard = new Spec2SoftKeyBoard(context);
        } else if (type == SafeEdit.SOFTKEYBOARD_TYPE_INOUTAMOUNT) {
            softKeyBoard = new SpecMoneySoftKeyBoard(context);
        } else if (type == SafeEdit.SOFTKEYBOARD_TYPE_INOUTPASSWORD) {
            softKeyBoard = new SpecSecurityNumPasswordBoard(context);
        }

        return softKeyBoard;
    }
}
