package org.example.utils;

public class DiplomaUtil {
    public static long getChatId(String data) {
        String[] partArray = data.split(", ");
        return Long.parseLong(partArray[partArray.length - 1].substring(6));
    }
}
