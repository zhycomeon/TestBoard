package com.datai.safesoftboard.interfaces;

import android.view.View;

import com.datai.safesoftboard.BusinessType;
import com.datai.safesoftboard.SafeEdit;

public interface IBusinessProcess {

	 void commit(SafeEdit edit);

	 void doBusiness(SafeEdit edit,BusinessType type);

}
