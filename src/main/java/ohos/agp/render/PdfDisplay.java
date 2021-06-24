package ohos.agp.render;

import java.io.FileInputStream;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.utils.Matrix;
import ohos.agp.utils.NativeMemoryCleanerHelper;
import ohos.agp.utils.RectFloat;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;

public class PdfDisplay implements AutoCloseable {
    private static final int DISPLAY_MODE = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_PdfDisplay");
    private Page mActivePage;
    private FileInputStream mFileInputStream;
    private final Object mLock = new Object();
    private long mNativeDocument;
    private int mPageCount;
    private long mPdfHandle;

    private native void nativeClosePdf(long j);

    private native long nativeCreateDocument(long j, int i, int i2);

    private native long nativeCreatePdfHandle();

    private native int nativeGetPageCount(long j);

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        ohos.hiviewdfx.HiLog.info(ohos.agp.render.PdfDisplay.TAG, "get FileDescriptor.getInt$() failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0051 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public PdfDisplay(java.io.FileInputStream r6) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.render.PdfDisplay.<init>(java.io.FileInputStream):void");
    }

    public Page openPage(int i) {
        if (this.mFileInputStream == null || i < 0 || i >= this.mPageCount) {
            HiLog.warn(TAG, "pdf has close or index is wrong", new Object[0]);
            return null;
        }
        if (this.mActivePage != null) {
            HiLog.warn(TAG, "old page will be replaced by new page", new Object[0]);
        }
        this.mActivePage = new Page(i);
        return this.mActivePage;
    }

    public int getPageCount() {
        if (this.mFileInputStream != null) {
            return this.mPageCount;
        }
        HiLog.warn(TAG, "pdf has close", new Object[0]);
        return 0;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.mLock) {
            if (this.mFileInputStream != null) {
                if (this.mActivePage == null) {
                    doClose();
                    return;
                }
            }
            HiLog.warn(TAG, "pdf has close or page is showing", new Object[0]);
        }
    }

    private void doClose() {
        Page page = this.mActivePage;
        if (page != null) {
            page.close();
            this.mActivePage = null;
        }
        if (this.mNativeDocument != 0) {
            nativeClosePdf(this.mPdfHandle);
            this.mNativeDocument = 0;
        }
        if (this.mFileInputStream != null) {
            this.mFileInputStream = null;
        }
    }

    protected static class PdfDisplayCleaner extends NativeMemoryCleanerHelper {
        private native void nativePdfDisplayRelease(long j);

        public PdfDisplayCleaner(long j) {
            super(j);
        }

        /* access modifiers changed from: protected */
        @Override // ohos.agp.utils.NativeMemoryCleanerHelper
        public void releaseNativeMemory(long j) {
            if (j != 0) {
                nativePdfDisplayRelease(j);
            }
        }
    }

    public final class Page implements AutoCloseable {
        private final int mHeight;
        private final int mIndex;
        private long mNativePage;
        private final int mWidth;

        private native void nativeClosePage(long j);

        private native float nativeGetPageHeight(long j);

        private native float nativeGetPageWidth(long j);

        private native long nativeOpenPage(long j, int i);

        private native void nativeShowPage(long j, PixelMap pixelMap, RectFloat rectFloat, long j2, int i);

        private Page(int i) {
            synchronized (PdfDisplay.this.mLock) {
                this.mNativePage = nativeOpenPage(PdfDisplay.this.mPdfHandle, i);
            }
            this.mWidth = (int) nativeGetPageWidth(PdfDisplay.this.mPdfHandle);
            this.mHeight = (int) nativeGetPageHeight(PdfDisplay.this.mPdfHandle);
            this.mIndex = i;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public void show(PixelMap pixelMap) {
            if (this.mNativePage == 0) {
                throw new NullPointerException();
            } else if (pixelMap == null || pixelMap.getImageInfo().pixelFormat != PixelFormat.ARGB_8888) {
                HiLog.info(PdfDisplay.TAG, "pixelFormat not supported", new Object[0]);
            } else {
                RectFloat rectFloat = new RectFloat();
                rectFloat.left = 0.0f;
                rectFloat.top = 0.0f;
                rectFloat.right = (float) pixelMap.getImageInfo().size.width;
                rectFloat.bottom = (float) pixelMap.getImageInfo().size.height;
                int i = (int) (rectFloat.right - rectFloat.left);
                int i2 = (int) (rectFloat.bottom - rectFloat.top);
                Matrix matrix = new Matrix();
                if (!(getWidth() == 0 || getHeight() == 0)) {
                    matrix.postScale(((float) i) / ((float) getWidth()), ((float) i2) / ((float) getHeight()));
                }
                long nativeHandle = matrix.getNativeHandle();
                synchronized (PdfDisplay.this.mLock) {
                    nativeShowPage(PdfDisplay.this.mPdfHandle, pixelMap, rectFloat, nativeHandle, 1);
                }
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (this.mNativePage != 0) {
                synchronized (PdfDisplay.this.mLock) {
                    nativeClosePage(PdfDisplay.this.mPdfHandle);
                }
                this.mNativePage = 0;
            }
            PdfDisplay.this.mActivePage = null;
        }
    }
}
