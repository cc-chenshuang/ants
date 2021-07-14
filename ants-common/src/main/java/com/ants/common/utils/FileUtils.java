package com.ants.common.utils;

import java.text.DecimalFormat;

/**
 * TODO
 * Author Chen
 * Date   2021/2/5 16:46
 */
public class FileUtils {

    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String flagFileType(String fileType){
        String type = "";
        switch (fileType){
            case "local":
                type = "1";
                break;
            case "minio":
                type = "2";
                break;
            case "alioss":
                type = "3";
                break;
        }
        return type;
    }
}
