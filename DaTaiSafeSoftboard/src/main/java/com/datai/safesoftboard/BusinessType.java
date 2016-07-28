package com.datai.safesoftboard;

/**
 * Created by Administrator on 2016/7/20.
 */
public enum BusinessType {
    /*
		type: 代表要处理的类型
		1:全仓；2：半仓：3:1/3仓; 4:2/3仓; 5: 1/4仓; 6:获取按键的值 ;7;increase;8:decrease;9:删除
	 */
    BUSINESS_TYPE_DEFAULT,
    BUSINESS_TYPE_ALL,
    BUSINESS_TYPE_HALF,
    BUSINESS_TYPE_ONETHIRD,
    BUSINESS_TYPE_TWOTHIRD,
    BUSINESS_TYPE_ONEFOURTH,
    BUSINESS_TYPE_KEYVALUE,
    BUSINESS_TYPE_INCREASE,
    BUSINESS_TYPE_DECREASE,
    BUSINESS_TYPE_DELETE
}
