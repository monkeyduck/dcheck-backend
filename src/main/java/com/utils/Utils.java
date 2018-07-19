package com.utils;

import com.mvc.model.Group;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
