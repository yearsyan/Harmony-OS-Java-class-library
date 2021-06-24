package com.huawei.ace.systemplugin.httpaccess.adapter;

import com.huawei.ace.systemplugin.httpaccess.HttpProbe;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import ohos.app.Context;

public class HttpAccessProxy {
    private Context context;

    public HttpAccessProxy(Context context2) {
        this.context = context2;
    }

    public void httpUrlUpload(RequestData requestData, HttpProbe httpProbe) {
        new HttpUploadImpl(this.context).httpUrlUpload(requestData, httpProbe);
    }

    public void httpUrlFetch(RequestData requestData, HttpProbe httpProbe) {
        new HttpFetchImpl(this.context).httpUrlFetch(requestData, httpProbe);
    }
}
