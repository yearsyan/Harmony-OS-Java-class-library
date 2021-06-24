package com.huawei.ace.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.huawei.ace.runtime.ALog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import ohos.agp.window.view.AGPSurfaceControl;
import ohos.agp.window.wmc.AGPWindow;
import ohos.agp.window.wmc.AGPWindowManager;
import ohos.com.sun.org.apache.xpath.internal.XPath;

public class AceWindowBlurAdapter {
    public static final int BLUR_DYNAMIC_CHANGE = 1;
    public static final int FLAG_BLUR_BEHIND = 33554432;
    private static final String LOG_TAG = "AceWindowBlurAdapter";
    private static final float OVAL_X_OFFSET = 0.2f;
    private static final int PER_COORD_NUMS = 6;
    public static final int STYLE_BACKGROUND_SMALL_LIGHT = 100;
    public static final int STYLE_BEGIN = 100;
    public static final int STYLE_END = 107;
    private static final int WINDOW_BLUR_ARGS = 8;
    private AGPSurfaceControl mAgpSurfaceControl;
    private Window mAndroidWindow;
    private boolean mChanges = false;
    private Context mContext;
    private float mMaxProgress = 0.0f;
    private Region mRegion = new Region();
    private final Rect mTempBlurRect = new Rect();
    private ArrayList<Rect> mViewRectList = new ArrayList<>();
    private int mWindowBlurStyle = 100;

    public AceWindowBlurAdapter(Object obj) {
        if (obj instanceof Context) {
            this.mContext = (Context) obj;
        }
        Context context = this.mContext;
        if (context instanceof Activity) {
            this.mAndroidWindow = ((Activity) context).getWindow();
        }
        Optional<AGPWindow> topWindow = AGPWindowManager.getInstance().getTopWindow();
        if (topWindow.isPresent()) {
            this.mAgpSurfaceControl = topWindow.get().getAGPSurfaceControl();
        }
        if (AGPSurfaceControl.getBlurFeatureEnabled()) {
            enableWindowBlurFlag();
        }
    }

    public void enableWindowBlurFlag() {
        Context context = this.mContext;
        if (context != null && (context instanceof Activity)) {
            Activity activity = (Activity) context;
            WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
            attributes.hwFlags = (attributes.hwFlags & -33554433) | 33554432;
            activity.getWindow().setAttributes(attributes);
        }
    }

    public void updateWindowBlurRegion(float[][] fArr, int i, int i2) {
        if (fArr != null) {
            enableWindowBlur();
            ArrayList<Rect> arrayList = new ArrayList<>();
            Region region = new Region();
            region.setEmpty();
            float f = 0.0f;
            int i3 = 100;
            for (int i4 = 0; i4 < fArr.length; i4++) {
                if (fArr[i4] != null && fArr[i4].length >= 8) {
                    Rect rect = new Rect(Math.round(fArr[i4][2]), Math.round(fArr[i4][3]), Math.round(fArr[i4][4]), Math.round(fArr[i4][5]));
                    rect.offset(i, i2);
                    Region region2 = new Region();
                    getRoundRect(region2, rect, (int) fArr[i4][6], (int) fArr[i4][7]);
                    arrayList.add(rect);
                    if (fArr[i4].length > 8) {
                        clipToGlobalOutline(region2, i, i2, Arrays.copyOfRange(fArr[i4], 8, fArr[i4].length));
                    }
                    region.op(region2, Region.Op.UNION);
                    float f2 = fArr[i4][0];
                    if (f2 >= 0.0f && f2 <= 1.0f) {
                        f = Math.max(f, f2);
                    }
                    int i5 = (int) fArr[i4][1];
                    if (i5 >= 100 && i5 <= 107) {
                        i3 = i5;
                    }
                }
            }
            if (!(this.mRegion.equals(region) && this.mMaxProgress == f && i3 == this.mWindowBlurStyle)) {
                this.mRegion = region;
                this.mViewRectList = arrayList;
                this.mMaxProgress = f;
                this.mWindowBlurStyle = i3;
                this.mChanges = true;
            }
        }
    }

    public void updateWindowDrawOp(boolean z) {
        View decorView;
        View findViewById;
        if (this.mAgpSurfaceControl == null) {
            ALog.e(LOG_TAG, "mAgpSurfaceControl is null");
        } else if (this.mChanges) {
            enableWindowBlur();
            this.mAgpSurfaceControl.openTransaction();
            if (!this.mRegion.isEmpty()) {
                int size = this.mViewRectList.size();
                float[] fArr = new float[size];
                Arrays.fill(fArr, 1.0f);
                this.mAgpSurfaceControl.setViewParam(this.mViewRectList, fArr, size);
                this.mAgpSurfaceControl.setBlurProgress(this.mMaxProgress);
            }
            this.mAgpSurfaceControl.setBlurRegion(this.mRegion);
            this.mAgpSurfaceControl.closeTransaction();
            Window window = this.mAndroidWindow;
            if (!(window == null || !z || (decorView = window.getDecorView()) == null || (findViewById = decorView.findViewById(16908290)) == null)) {
                findViewById.invalidate();
            }
            this.mChanges = false;
        }
    }

    public void onActivityResume() {
        this.mChanges = true;
    }

    private void enableWindowBlur() {
        AGPSurfaceControl aGPSurfaceControl = this.mAgpSurfaceControl;
        if (aGPSurfaceControl == null) {
            ALog.e(LOG_TAG, "mAgpSurfaceControl is null");
            return;
        }
        aGPSurfaceControl.openTransaction();
        this.mAgpSurfaceControl.setBlurEnabled(true);
        this.mAgpSurfaceControl.setBehindLayerBlurStyle(this.mWindowBlurStyle);
        this.mAgpSurfaceControl.setFrontLayerBlurStyle(this.mWindowBlurStyle);
        this.mAgpSurfaceControl.setBlurEnabledForViewRoot(false);
        this.mAgpSurfaceControl.setBlurMode(1);
        this.mAgpSurfaceControl.closeTransaction();
    }

    private void getRoundRect(Region region, Rect rect, int i, int i2) {
        int i3 = rect.left;
        int i4 = rect.top;
        int i5 = rect.right;
        int i6 = rect.bottom;
        int min = Math.min((i5 - i3) / 2, i);
        int min2 = Math.min((i6 - i4) / 2, i2);
        if (min <= 0 || min2 <= 0) {
            region.set(rect);
            return;
        }
        region.set(i3 + min, i4, i5 - min, i6);
        Rect rect2 = this.mTempBlurRect;
        for (int i7 = 0; i7 <= min; i7++) {
            double pow = Math.pow((1.0d - (Math.pow(Math.max((double) (((float) (min - i7)) - OVAL_X_OFFSET), (double) XPath.MATCH_SCORE_QNAME), 2.0d) / Math.pow((double) min, 2.0d))) * Math.pow((double) min2, 2.0d), 0.5d);
            int i8 = i3 + i7;
            int i9 = i8 + 1;
            int floor = (i4 + min2) - ((int) Math.floor(pow));
            int floor2 = (i6 - min2) + ((int) Math.floor(pow));
            rect2.set(i8, floor, i9, floor2);
            region.union(rect2);
            int i10 = i3 + i5;
            rect2.set(i10 - i9, floor, i10 - i8, floor2);
            region.union(rect2);
        }
    }

    private void clipToGlobalOutline(Region region, int i, int i2, float[] fArr) {
        if (fArr != null) {
            if (fArr.length % 6 != 0) {
                ALog.e(LOG_TAG, "coords's length can not divide by 6");
                return;
            }
            int length = fArr.length / 6;
            for (int i3 = 0; i3 < length; i3++) {
                int i4 = i3 * 6;
                float f = fArr[i4];
                float f2 = fArr[i4 + 1];
                float f3 = fArr[i4 + 2];
                float f4 = fArr[i4 + 3];
                int i5 = (int) fArr[i4 + 4];
                int i6 = (int) fArr[i4 + 5];
                Region region2 = new Region();
                Rect rect = new Rect(Math.round(f), Math.round(f2), Math.round(f3), Math.round(f4));
                rect.offset(i, i2);
                if (i5 <= 0 || i6 <= 0) {
                    region2.set(rect);
                } else {
                    getRoundRect(region2, rect, i5, i6);
                }
                region.op(region2, Region.Op.INTERSECT);
            }
        }
    }
}
