package com.xloger.xlib.tool;

import android.os.Looper;

/**
 * Created on 2017/5/3 10:33.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */

public class XTool {
    public static boolean isBlankString(String str) {
        return str == null || str.trim().equals("");
    }

    /**
     * 返回一个[1,max]的int
     *
     * @param max 大于等于1的正整数
     * @return
     */
    public static int randomInt(int max) {
        return (int) (Math.random() * max) + 1;
    }

    public static boolean isOnMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

}
