package com.xloger.xlib.tool;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created on 2017/7/10 11:40.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */

public class XActivity {

    public static void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, null);
    }

    public static void startActivity(Context context, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (extras != null) {
            intent.putExtras(extras);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Xlog.INSTANCE.toast(context, "未能找到可用以打开网页的应用");
        }
    }

}
