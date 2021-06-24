package com.huawei.ace.systemplugin.httpaccess.task;

import com.huawei.ace.systemplugin.httpaccess.HttpProbe;
import com.huawei.ace.systemplugin.httpaccess.adapter.HttpAccessProxy;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import ohos.app.Context;

public class HttpFetchTask implements Runnable {
    private HttpAccessProxy httpAccessProxy;
    private HttpProbe httpProbe;
    private RequestData requestData;

    public HttpFetchTask(RequestData requestData2, HttpProbe httpProbe2, Context context) {
        this.httpProbe = httpProbe2;
        this.requestData = requestData2;
        this.httpAccessProxy = new HttpAccessProxy(context);
    }

    public void run() {
        this.httpAccessProxy.httpUrlFetch(this.requestData, this.httpProbe);
    }
}
