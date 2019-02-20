package com.utils;

import com.mvc.model.Group;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by linchuanli on 2018/7/11.
 */
public class Utils {
    private static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static String encodeMD5(String data) {
        md.reset();
        return Base64.encodeBase64URLSafeString(md.digest(StringUtils.getBytesUtf8(data)));
    }

    public static JSONObject genGroupInfoJson(Group group) {
        JSONObject json = JSONObject.fromObject(group);
        System.out.println(json.toString());
        return json;
    }

    public static void main(String[] args) {
        String data = "adfjlfjalfjajj2018-10-11";
        System.out.println(encodeMD5(data).length());
    }

    public static Object map2Object(Map<String, Object> map, Class<?> clazz) throws Exception{
        if (map == null) {
            return null;
        }
        Object obj = null;
        Object value = null;
        obj = clazz.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            Class<?> fieldTypeClass = field.getType();
            String propertyName = field.getName();
            value = map.get(propertyName);
            value = convertValType(value, fieldTypeClass);
            String setMethodName = "set"
                    + propertyName.substring(0, 1).toUpperCase()
                    + propertyName.substring(1);
            clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);

//            field.setAccessible(true);
//            field.set(obj, map.get(field.getName()));
        }

        return obj;
    }

    private static Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;
        if(Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if(Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if(Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }
}
