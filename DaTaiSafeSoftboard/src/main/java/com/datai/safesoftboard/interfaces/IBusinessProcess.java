package com.datai.safesoftboard.interfaces;

import android.view.View;

import com.datai.safesoftboard.BusinessType;
import com.datai.safesoftboard.SafeEdit;

public interface IBusinessProcess {

	 void commit(SafeEdit edit);

	 void doBusiness(SafeEdit edit,BusinessType type);
//	 void allBusinessPos(SafeEdit edit);
//
//	 void halfBusinessPos(SafeEdit edit);
//
//	 void oneQuarterBusinessPos(SafeEdit edit);
//
//	 void oneThirdBusinessPos(SafeEdit edit);
//
//	 void twoThirdBusinessPos(SafeEdit edit);
//
//	void  inCreaseBusinessValue(SafeEdit edit);
//
//	void  deCreaseBusinessValue(SafeEdit edit);

}
