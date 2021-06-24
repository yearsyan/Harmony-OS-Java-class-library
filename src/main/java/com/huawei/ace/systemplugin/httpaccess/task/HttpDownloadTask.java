package com.huawei.ace.systemplugin.httpaccess.task;

import com.huawei.ace.runtime.ALog;
import com.huawei.ace.systemplugin.httpaccess.HttpProbe;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import com.huawei.ace.systemplugin.httpaccess.data.ResponseData;
import java.util.Map;
import ohos.app.Context;
import ohos.miscservices.download.DownloadConfig;
import ohos.miscservices.download.DownloadSession;
import ohos.utils.net.Uri;

public class HttpDownloadTask {
    private static final String TAG = "HttpDownloadTask";
    private Context context;
    private HttpProbe httpProbe;
    private RequestData requestData;

    public HttpDownloadTask(RequestData requestData2, HttpProbe httpProbe2, Context context2) {
        this.httpProbe = httpProbe2;
        this.requestData = requestData2;
        this.context = context2;
    }

    public void start() {
        int lastIndexOf;
        ALog.d(TAG, "start download!");
        String url = this.requestData.getUrl();
        Uri parse = Uri.parse(url);
        String fileName = this.requestData.getFileName();
        if ("".equals(fileName) && (lastIndexOf = url.lastIndexOf("/")) > 0) {
            fileName = url.substring(lastIndexOf + 1);
        }
        String description = this.requestData.getDescription();
        if ("".equals(description)) {
            description = fileName;
        }
        DownloadConfig.Builder description2 = new DownloadConfig.Builder(this.context, parse).setPath(null, fileName).setDescription(description);
        if (this.requestData.getHeader() != null && !this.requestData.getHeader().isEmpty()) {
            for (Map.Entry<String, String> entry : this.requestData.getHeader().entrySet()) {
                description2.addHttpheader(entry.getKey(), entry.getValue());
            }
        }
        long start = new DownloadSession(this.context, description2.build()).start();
        if (start > 0) {
            ResponseData responseData = new ResponseData();
            responseData.setCode(8);
            responseData.setToken(start);
            this.httpProbe.onResponse(responseData);
        }
    }
}
