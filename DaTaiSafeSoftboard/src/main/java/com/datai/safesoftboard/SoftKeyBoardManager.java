package com.datai.safesoftboard;

import android.content.Context;


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
        if (type == 0) {
            softKeyBoard = new SafeSoftKeyBoard(context);
        } else if (type == 1) {
            softKeyBoard = new Spec1SoftKeyBoard(context);
        } else if (type == 2) {
            softKeyBoard = new Spec2SoftKeyBoard(context);
        } else if (type == 3) {
            softKeyBoard = new SpecMoneySoftKeyBoard(context);
        } else if (type == 4) {
            softKeyBoard = new SpecSecurityNumPasswordBoard(context);
        }

        return softKeyBoard;
    }
}
