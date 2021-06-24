package com.huawei.ace.systemplugin.httpaccess;

import com.huawei.ace.runtime.ALog;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import com.huawei.ace.systemplugin.httpaccess.data.ResponseData;
import com.huawei.ace.systemplugin.httpaccess.task.HttpDownloadTask;
import com.huawei.ace.systemplugin.httpaccess.task.HttpFetchTask;
import com.huawei.ace.systemplugin.httpaccess.task.HttpUploadTask;
import com.huawei.ace.systemplugin.httpaccess.task.OnDownloadCompleteHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ohos.app.Context;

public class HttpAccess {
    private static final String TAG = "HttpAccess";
    private ExecutorService executorService = Executors.newSingleThreadExecutor(new HttpThreadFactory(TAG));

    public void fetch(RequestData requestData, HttpProbe httpProbe, Context context) {
        if (!CheckParamUtils.checkFetchRequest(requestData)) {
            ALog.e(TAG, "fetch request param error!");
            ResponseData responseData = new ResponseData();
            responseData.setCode(202);
            httpProbe.onResponse(responseData);
            return;
        }
        this.executorService.execute(new HttpFetchTask(requestData, httpProbe, context));
    }

    public void upload(RequestData requestData, HttpProbe httpProbe, Context context) {
        if (!CheckParamUtils.checkUploadRequest(requestData)) {
            ALog.e(TAG, "upload request param error!");
            ResponseData responseData = new ResponseData();
            responseData.setCode(202);
            httpProbe.onResponse(responseData);
            return;
        }
        this.executorService.execute(new HttpUploadTask(requestData, httpProbe, context));
    }

    public void download(RequestData requestData, HttpProbe httpProbe, Context context) {
        if (!CheckParamUtils.checkDownloadRequest(requestData)) {
            ALog.e(TAG, "download request param error!");
            ResponseData responseData = new ResponseData();
            responseData.setCode(202);
            httpProbe.onResponse(responseData);
            return;
        }
        new HttpDownloadTask(requestData, httpProbe, context).start();
    }

    public void onDownloadComplete(RequestData requestData, HttpProbe httpProbe, Context context) {
        if (!CheckParamUtils.checkOnDownloadCompleteRequest(requestData)) {
            ALog.e(TAG, "onDownloadComplete request param error!");
            ResponseData responseData = new ResponseData();
            responseData.setCode(202);
            httpProbe.onResponse(responseData);
            return;
        }
        new OnDownloadCompleteHandler(requestData, httpProbe, context).handle();
    }
}
