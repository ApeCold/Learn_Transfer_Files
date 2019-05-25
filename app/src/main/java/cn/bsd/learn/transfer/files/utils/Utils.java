package cn.bsd.learn.transfer.files.utils;

import com.netease.async.library.EventBus;
import com.netease.async.library.util.Constants;

import java.io.File;
import java.text.DecimalFormat;

public class Utils {

    // 获取文件大小
    public static String getFileSize(long length) {
        DecimalFormat df = new DecimalFormat("######0.00");
        double d1 = 3.23456;
        double d2 = 0.0;
        double d3 = 2.0;
        df.format(d1);
        df.format(d2);
        df.format(d3);
        long l = length / 1000;//KB
        if (l < 1024) {
            return df.format(l) + "KB";
        } else if (l < 1024 * 1024.f) {
            return df.format((l / 1024.f)) + "MB";
        }
        return df.format(l / 1024.f / 1024.f) + "GB";
    }

    // 删除所有文件
    public static void deleteAll() {
        File dir = Constants.DIR;
        if (dir.exists() && dir.isDirectory()) {
            File[] fileNames = dir.listFiles();
            if (fileNames != null) {
                for (File fileName : fileNames) {
                    fileName.delete();
                }
            }
        }
        EventBus.getDefault().post(Constants.RxBusEventType.LOAD_BOOK_LIST);
    }

    // 获取文件名
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }
}
