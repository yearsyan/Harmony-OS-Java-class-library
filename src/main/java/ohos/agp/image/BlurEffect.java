package ohos.agp.image;

import ohos.aafwk.utils.log.LogDomain;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.ImageInfo;
import ohos.media.image.common.PixelFormat;

@SystemApi
public class BlurEffect {
    public static final int BLUR_SUCCESS = 0;
    public static final int ERRBLUR_INPUT = 1;
    public static final int ERRBLUR_INPUT_CONFIG = 6;
    public static final int ERRBLUR_INPUT_SIZE = 4;
    public static final int ERRBLUR_OUTPUT = 2;
    public static final int ERRBLUR_RADIUS = 5;
    public static final int ERRBLUR_SIZE_MISMATCH = 3;
    public static final int ERROR_OUTPUT_EDITABLE_STATE = 7;
    public static final int MAX_RADIUS = 25;
    public static final int MIN_RADIUS = 3;
    public static final int MIN_SIZE = 3;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "BLUR_EFFECT");

    private native void nativeBoxBlur(PixelMap pixelMap, PixelMap pixelMap2, int i);

    public int blur(PixelMap pixelMap, PixelMap pixelMap2, int i) {
        int checkBlurInputParameter = checkBlurInputParameter(pixelMap, pixelMap2, i);
        if (checkBlurInputParameter != 0) {
            HiLog.error(TAG, "blur error code:%{public}d", Integer.valueOf(checkBlurInputParameter));
            return checkBlurInputParameter;
        }
        nativeBoxBlur(pixelMap, pixelMap2, i);
        return checkBlurInputParameter;
    }

    private int checkBlurInputParameter(PixelMap pixelMap, PixelMap pixelMap2, int i) {
        if (pixelMap == null) {
            return 1;
        }
        if (pixelMap2 == null) {
            return 2;
        }
        ImageInfo imageInfo = pixelMap.getImageInfo();
        ImageInfo imageInfo2 = pixelMap2.getImageInfo();
        if (!imageInfo.size.equals(imageInfo2.size)) {
            HiLog.error(TAG, "src and dst size should be same.", new Object[0]);
            return 3;
        } else if (i < 3 || i > 25) {
            HiLog.error(TAG, "current radius %{public}d should be in range [%{public}d, %{public}d].", Integer.valueOf(i), 3, 25);
            return 5;
        } else if (imageInfo.size.width < 3 || imageInfo.size.height < 3) {
            HiLog.error(TAG, "pixelMap size should lager than %d.", 3);
            return 4;
        } else if (imageInfo.pixelFormat != imageInfo2.pixelFormat || !checkFormat(imageInfo.pixelFormat) || !checkFormat(imageInfo2.pixelFormat)) {
            HiLog.error(TAG, "pixelMap format error.", new Object[0]);
            return 6;
        } else if (pixelMap2.isEditable()) {
            return 0;
        } else {
            HiLog.error(TAG, "dst pixelMap is not editable.", new Object[0]);
            return 7;
        }
    }

    private boolean checkFormat(PixelFormat pixelFormat) {
        if (pixelFormat == PixelFormat.ARGB_8888 || pixelFormat == PixelFormat.RGBA_8888 || pixelFormat == PixelFormat.BGRA_8888) {
            return true;
        }
        HiLog.error(TAG, "error pixelFormat, only surpport 32bits pixelMap.", new Object[0]);
        return false;
    }
}
