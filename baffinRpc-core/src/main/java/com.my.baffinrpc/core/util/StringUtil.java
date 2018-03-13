package com.my.baffinrpc.core.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

    public static String convertFirstLetterToLowerCase(String original)
    {
        if (original == null || original.length() < 1)
            return original;
        else
        {
            char c[] = original.toCharArray();
            c[0] = Character.toLowerCase(c[0]);
            return new String(c);
        }
    }

    public static boolean isEmptyOrNull(String string)
    {
        if (string == null)
            return true;
        return "".equals(string.trim());
    }


    public static byte[] encodeString(String string, String encodingType)
    {
        if (string == null)
            throw new IllegalArgumentException("string == null");
        try
        {
            return string.getBytes(encodingType);
        } catch (UnsupportedEncodingException e) {
            return string.getBytes();
        }
    }

    public static String decodeString(byte[] bytes, String encodingType)
    {
        if (bytes == null)
            throw new IllegalArgumentException("bytes == null");
        try {
            return new String(bytes,encodingType);
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }


}
