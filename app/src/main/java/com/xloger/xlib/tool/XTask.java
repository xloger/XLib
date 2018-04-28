package com.xloger.xlib.tool;

import android.os.AsyncTask;

/**
 * Created on 2017/9/29 10:13.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */

public abstract class XTask extends AsyncTask<String, Integer, Object> {

    private final XTaskCallback callback;
    private final int tag;

    public XTask() {
        callback = null;
        tag = 0;
    }

    public XTask(XTaskCallback callback) {
        this.callback = callback;
        tag = 0;
    }

    public XTask(XTaskCallback callback, int tag) {
        this.callback = callback;
        this.tag = tag;
    }


    @Override
    protected void onPostExecute(Object o) {
        if (callback != null) {
            callback.onTaskFinish(o, tag);
        }
    }

    public void runOnQueue(String... params) {
        this.execute(params);
    }

    public void runNow(String... params) {
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    public interface XTaskCallback {
        void onTaskFinish(Object object, int tag);
    }
}
