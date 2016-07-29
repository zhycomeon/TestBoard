package com.datai.safesoftboard.Utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Administrator on 2016/7/11.
 */
public class DESUtils {

   // public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static  Key key;// 密钥的key值
    private byte[] DESkey;
    private static byte[] DESIV = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB,
            (byte) 0xCD, (byte) 0xEF };
    private static AlgorithmParameterSpec iv = null;// 加密算法的参数接口

    private volatile static DESUtils mInstance = null;

    private DESUtils (){
        try {
            DESkey = "abcdefghijk".getBytes("UTF-8");// 设置密钥
            DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
            iv = new IvParameterSpec(DESIV);// 设置向量
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
            key = keyFactory.generateSecret(keySpec);// 得到密钥对象
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DESUtils getInstance(){
        if (mInstance == null) {
            synchronized(DESUtils.class){
                if(mInstance == null){
                    mInstance = new DESUtils();
                }
            }
        }

        return mInstance;
    }


    //加密
    private static byte[] getEncCode(byte[] bt) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            // 得到Cipher实例
            cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byteFina = cipher.doFinal(bt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    public static String encode(String inputStr){
        byte[] byteMi = null;
        byte[] byteMing = null;
        String outputStr= null;

        try {
            byteMing = inputStr.getBytes("UTF-8");
            byteMi = getEncCode(byteMing);
            byte[] temp = Base64.encode(byteMi, Base64.DEFAULT);
            return  new String(temp,"UTF8");
        } catch (Exception e) {
        } finally {
            byteMing = null;
            byteMi = null;
            outputStr="";
        }
        return outputStr;
    }

    //解密
    private static  byte[] getDesCode(byte[] bt) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            // 得到Cipher实例
            cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byteFina = cipher.doFinal(bt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    public String decode(String inputString) {
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = null;
        if(TextUtils.isEmpty(inputString)){
            return strMing;
        }
        try {
            byteMi = Base64.decode(inputString.getBytes("UTF-8"), Base64.DEFAULT);
            byteMing = getDesCode(byteMi);
           return new String(byteMing, "UTF8");
        } catch (Exception e) {
        } finally {
            byteMing = null;
            byteMi = null;
            strMing="";
        }
        return strMing;
    }
}
