package com.huawei.ace.systemplugin.httpaccess.task;

import com.huawei.ace.runtime.ALog;
import com.huawei.ace.systemplugin.httpaccess.HttpProbe;
import com.huawei.ace.systemplugin.httpaccess.data.RequestData;
import com.huawei.ace.systemplugin.httpaccess.data.ResponseData;
import ohos.app.Context;
import ohos.miscservices.download.DownloadConfig;
import ohos.miscservices.download.DownloadSession;
import ohos.miscservices.download.IDownloadListener;

public class OnDownloadCompleteHandler {
    private static final String CACHE_DIR = "internal://cache/";
    private static final String TAG = "OnDownloadCompleteHandler";
    private Context context;
    private HttpProbe httpProbe;
    private RequestData requestData;

    public OnDownloadCompleteHandler(RequestData requestData2, HttpProbe httpProbe2, Context context2) {
        this.httpProbe = httpProbe2;
        this.requestData = requestData2;
        this.context = context2;
    }

    public void handle() {
        long j;
        ALog.d(TAG, "enter onDownloadCompleteHandler!");
        try {
            j = Long.parseLong(this.requestData.getToken());
        } catch (NumberFormatException unused) {
            ALog.e(TAG, "parse sessionid error.");
            j = -1;
        }
        DownloadSession downloadSession = new DownloadSession(this.context, (DownloadConfig) null);
        if (!downloadSession.attach(j)) {
            ALog.e(TAG, "download task does not exsist.");
            ResponseData responseData = new ResponseData();
            responseData.setCode(401);
            this.httpProbe.onResponse(responseData);
            return;
        }
        DownloadSession.DownloadInfo query = downloadSession.query();
        if (query != null) {
            int status = query.getStatus();
            if (status == 1 || status == 2 || status == 4) {
                addDownloadListener(downloadSession, this.httpProbe);
            } else if (status == 8) {
                copyFile(query.getTitle(), this.httpProbe);
            } else if (status == 16) {
                ResponseData responseData2 = new ResponseData();
                responseData2.setCode(16);
                this.httpProbe.onResponse(responseData2);
            }
        } else {
            ALog.e(TAG, "can not query download task!");
            ResponseData responseData3 = new ResponseData();
            responseData3.setCode(401);
            this.httpProbe.onResponse(responseData3);
        }
    }

    private void addDownloadListener(final DownloadSession downloadSession, final HttpProbe httpProbe2) {
        downloadSession.addListener(new IDownloadListener() {
            /* class com.huawei.ace.systemplugin.httpaccess.task.OnDownloadCompleteHandler.AnonymousClass1 */

            @Override // ohos.miscservices.download.IDownloadListener
            public void onCompleted() {
                DownloadSession.DownloadInfo query = downloadSession.query();
                if (query == null || query.getTitle() == null) {
                    ALog.e(OnDownloadCompleteHandler.TAG, "downloadInfo or title is null!");
                    ResponseData responseData = new ResponseData();
                    responseData.setCode(401);
                    httpProbe2.onResponse(responseData);
                    return;
                }
                OnDownloadCompleteHandler.this.copyFile(query.getTitle(), httpProbe2);
                if (!downloadSession.removeListener(this)) {
                    ALog.e(OnDownloadCompleteHandler.TAG, "remove listener failed!");
                }
            }

            @Override // ohos.miscservices.download.IDownloadListener
            public void onFailed(int i) {
                ALog.e(OnDownloadCompleteHandler.TAG, "download task error, errorCode : " + i);
                ResponseData responseData = new ResponseData();
                responseData.setCode(i);
                httpProbe2.onResponse(responseData);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00d2, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00d3, code lost:
        if (r2 != null) goto L_0x00d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00d5, code lost:
        $closeResource(r14, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00d8, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00db, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00dc, code lost:
        if (r13 != null) goto L_0x00de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00de, code lost:
        $closeResource(r14, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e1, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void copyFile(java.lang.String r14, com.huawei.ace.systemplugin.httpaccess.HttpProbe r15) {
        /*
        // Method dump skipped, instructions count: 250
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ace.systemplugin.httpaccess.task.OnDownloadCompleteHandler.copyFile(java.lang.String, com.huawei.ace.systemplugin.httpaccess.HttpProbe):void");
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }
}
