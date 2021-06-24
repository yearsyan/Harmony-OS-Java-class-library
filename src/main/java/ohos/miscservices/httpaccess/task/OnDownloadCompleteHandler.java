package ohos.miscservices.httpaccess.task;

import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.download.DownloadConfig;
import ohos.miscservices.download.DownloadSession;
import ohos.miscservices.download.IDownloadListener;
import ohos.miscservices.httpaccess.HttpProbe;
import ohos.miscservices.httpaccess.data.RequestData;
import ohos.miscservices.httpaccess.data.ResponseData;

public class OnDownloadCompleteHandler {
    private static final String CACHE_DIR = "internal://cache/";
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "OnDownloadCompleteHandler");
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
        HiLog.debug(TAG, "enter onDownloadCompleteHandler!", new Object[0]);
        try {
            j = Long.parseLong(this.requestData.getToken());
        } catch (NumberFormatException unused) {
            HiLog.error(TAG, "parse sessionid error.", new Object[0]);
            j = -1;
        }
        DownloadSession downloadSession = new DownloadSession(this.context, (DownloadConfig) null);
        if (!downloadSession.attach(j)) {
            HiLog.error(TAG, "download task does not exsist.", new Object[0]);
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
            HiLog.error(TAG, "can not query download task!", new Object[0]);
            ResponseData responseData3 = new ResponseData();
            responseData3.setCode(401);
            this.httpProbe.onResponse(responseData3);
        }
    }

    private void addDownloadListener(final DownloadSession downloadSession, final HttpProbe httpProbe2) {
        downloadSession.addListener(new IDownloadListener() {
            /* class ohos.miscservices.httpaccess.task.OnDownloadCompleteHandler.AnonymousClass1 */

            @Override // ohos.miscservices.download.IDownloadListener
            public void onCompleted() {
                DownloadSession.DownloadInfo query = downloadSession.query();
                if (query == null || query.getTitle() == null) {
                    HiLog.error(OnDownloadCompleteHandler.TAG, "downloadInfo or title is null!", new Object[0]);
                    ResponseData responseData = new ResponseData();
                    responseData.setCode(401);
                    httpProbe2.onResponse(responseData);
                    return;
                }
                OnDownloadCompleteHandler.this.copyFile(query.getTitle(), httpProbe2);
                if (!downloadSession.removeListener(this)) {
                    HiLog.error(OnDownloadCompleteHandler.TAG, "remove listener failed!", new Object[0]);
                }
            }

            @Override // ohos.miscservices.download.IDownloadListener
            public void onFailed(int i) {
                HiLog.error(OnDownloadCompleteHandler.TAG, "download task error, errorCode : %{public}d!", Integer.valueOf(i));
                ResponseData responseData = new ResponseData();
                responseData.setCode(i);
                httpProbe2.onResponse(responseData);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00d8, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00d9, code lost:
        if (r1 != null) goto L_0x00db;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00db, code lost:
        $closeResource(r14, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00de, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00e1, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e2, code lost:
        if (r13 != null) goto L_0x00e4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e4, code lost:
        $closeResource(r14, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e7, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void copyFile(java.lang.String r14, ohos.miscservices.httpaccess.HttpProbe r15) {
        /*
        // Method dump skipped, instructions count: 264
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.httpaccess.task.OnDownloadCompleteHandler.copyFile(java.lang.String, ohos.miscservices.httpaccess.HttpProbe):void");
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
