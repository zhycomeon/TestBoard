package com.datai.safesoftboard.interfaces;

import com.datai.safesoftboard.BusinessType;
import com.datai.safesoftboard.view.SafeEdit;

public interface IBusinessProcess {

	 void commit(SafeEdit edit);

	 void doBusiness(SafeEdit edit,BusinessType type);

}
