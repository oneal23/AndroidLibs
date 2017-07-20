package com.lei.baselib_java.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;


import com.lei.baselib_java.EasyApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 缓存管理工具类
 *
 * @author Lei 2016年7月29日15:02:43
 */

public class SharePrefUtil {
    private static final String FILE_NAME = "sharePref";
    private static Context context = EasyApplication.getContext();

    private SharePrefUtil() {
        checkNotNull(context);
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    static SharedPreferences.Editor editor;
    static SharePrefUtil helper;
    static SharedPreferences preferences;

    public static SharePrefUtil getInstance() {
        if (helper == null) {
            synchronized (SharePrefUtil.class) {
                if (helper == null) {
                    helper = new SharePrefUtil();
                }
            }
        }
        return helper;
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public void remove(String key) {
        editor.remove(key).commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear().commit();
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {
        editor.putString(key, value).commit();
    }

    /**
     * 读取 String数据
     *
     * @param key
     * @return String 数据
     */
    public String getAsString(String key, String def) {
        return preferences.getString(key, def);
    }

    /**
     * 保存 Boolean数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    /**
     * 读取 String数据
     *
     * @param key
     * @return String 数据
     */
    public boolean getAsBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    public void put(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public int getAsInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    public void put(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public long getAsLong(String key, long def) {
        return preferences.getLong(key, def);
    }

    public void put(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public float getAsFloat(String key, float def) {
        return preferences.getFloat(key, def);
    }

    public void put(String key, Set<String> value) {
        editor.putStringSet(key, value);
    }

    public Set<String> getAsStringSet(String key, Set<String> def) {
        return preferences.getStringSet(key, def);
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSON数据
     */
    public void put(String key, JSONObject value) {
        saveObject(key, value);
    }

    /**
     * 读取JSONObject数据
     *
     * @param key
     * @return JSONObject数据
     */
    public JSONObject getAsJSONObject(String key) {
        Object object = readObject(key);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        return null;
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSONArray数据
     */
    public void put(String key, JSONArray value) {
        saveObject(key, value);
    }

    /**
     * 读取JSONArray数据
     *
     * @param key
     * @return JSONArray数据
     */
    public JSONArray getAsJSONArray(String key) {
        Object object = readObject(key);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        return null;
    }

    /**
     * @param fileDir  存储的文件路径
     * @param filename 文件的名称
     * @return file的绝对路径
     *//*
    public String putFile(File fileDir, String filename) {
        return "";
    }

    *//**
     * @param filename 文件名称
     * @return 文件的绝对路径
     *//*
    public String put(String filename) {
        return aCache.put(filename);
    }

    *//**
     * 获取缓存文件
     *
     * @param key
     * @return value 缓存的文件
     *//*
    public File getFile(String key) {
        return aCache.file(key);
    }

    *//**
     * 获取文件路径
     *
     * @param fileDir
     * @param filename
     * @return
     *//*
    public String getFilePath(File fileDir, String filename) {
        return aCache.getFile(fileDir, filename);
    }

    *//**
     * 获取文件路径
     *
     * @param destFilePath
     * @return
     *//*
    public String getFilePath(String destFilePath) {
        return aCache.getFile(destFilePath);
    }*/

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        saveObject(key, value);
    }

    /**
     * 获取 byte 数据
     *
     * @param key
     * @return byte 数据
     */
    public byte[] getAsBinary(String key) {
        Object object = readObject(key);
        if (object instanceof byte[]) {
            return (byte[]) object;
        }
        return null;
    }

    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的value
     */
    public static void put(String key, Serializable value) {
        saveObject(key, value);
    }

    /**
     * 读取 Serializable数据
     *
     * @param key
     * @return Serializable 数据
     */
    public Object getAsObject(String key) {
        Object object = readObject(key);
        if (object instanceof Serializable) {
            return (Serializable) object;
        }
        return null;
    }

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的bitmap数据
     */
    public void put(String key, Bitmap value) {
        saveObject(key, value);
    }


    /**
     * 读取 bitmap 数据
     *
     * @param key
     * @return bitmap 数据
     */
    public Bitmap getAsBitmap(String key) {
        Object object = readObject(key);
        if (object instanceof Bitmap) {
            return (Bitmap) object;
        }
        return null;
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的drawable数据
     */
    public void put(String key, Drawable value) {
        saveObject(key, value);
    }

    /**
     * 读取 Drawable 数据
     *
     * @param key
     * @return Drawable 数据
     */
    public Drawable getAsDrawable(String key) {
        Object object = readObject(key);
        if (object instanceof Drawable) {
            return (Drawable) object;
        }
        return null;
    }

    private static void saveObject(String key, Object obj) {
        try {
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            editor.putString(key, bytesToHexString);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "保存obj失败");
        }
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    private static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:获取保存的Object对象
     *
     * @return modified:
     */
    private static Object readObject(String key) {
        try {
            if (preferences.contains(key)) {
                String string = preferences.getString(key, "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(string);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //所有异常返回null
        return null;

    }

    /**
     * desc:将16进制的数据转为数组
     * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
     *
     * @param data
     * @return modified:
     */
    private static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch3;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch3 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch3 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch4;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch4 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch4 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch3 + int_ch4;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }

}
