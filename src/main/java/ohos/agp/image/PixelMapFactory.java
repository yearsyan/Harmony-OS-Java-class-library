package ohos.agp.image;

import java.io.File;
import java.io.InputStream;
import ohos.aafwk.utils.log.LogDomain;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.PropertyKey;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

public class PixelMapFactory {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "PixelMapFactory");

    public static PixelMap createFromPath(String str) {
        if (!new File(str).exists()) {
            return null;
        }
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        sourceOptions.formatHint = "image/png";
        ImageSource create = ImageSource.create(str, sourceOptions);
        if (create == null) {
            return null;
        }
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredSize = new Size(0, 0);
        decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOptions.desiredPixelFormat = PixelFormat.RGBA_8888;
        return create.createPixelmap(decodingOptions);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00be, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00bf, code lost:
        if (r4 != null) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00c5, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c6, code lost:
        r5.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c9, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<ohos.media.image.PixelMap> createFromResourceId(ohos.app.Context r4, int r5) {
        /*
        // Method dump skipped, instructions count: 230
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.image.PixelMapFactory.createFromResourceId(ohos.app.Context, int):java.util.Optional");
    }

    public static ImageSource createGifFromPath(String str) {
        if (!new File(str).exists()) {
            return null;
        }
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        sourceOptions.formatHint = "image/gif";
        return ImageSource.create(str, sourceOptions);
    }

    public static ImageSource createGifFromInputStream(InputStream inputStream) {
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        sourceOptions.formatHint = "image/gif";
        return ImageSource.create(inputStream, sourceOptions);
    }

    public static int getGifDuration(ImageSource imageSource) {
        if (imageSource == null || imageSource.getSourceInfo() == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < imageSource.getSourceInfo().topLevelImageNum; i2++) {
            i += imageSource.getImagePropertyInt(i2, PropertyKey.GIF.DELAY_TIME, 0);
        }
        return i;
    }

    public static int getGifWidth(ImageSource imageSource) {
        if (imageSource == null || imageSource.getSourceInfo() == null) {
            return 0;
        }
        return imageSource.getImageInfo().size.width;
    }

    public static int getGifHeight(ImageSource imageSource) {
        if (imageSource == null || imageSource.getSourceInfo() == null) {
            return 0;
        }
        return imageSource.getImageInfo().size.height;
    }

    public static PixelMap getGifIndexPixelMap(ImageSource imageSource, long j, long j2) {
        boolean z;
        if (imageSource == null) {
            return null;
        }
        int i = imageSource.getSourceInfo().topLevelImageNum;
        long j3 = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j3 += (long) imageSource.getImagePropertyInt(i2, PropertyKey.GIF.DELAY_TIME, 0);
        }
        if (j3 == 0) {
            return null;
        }
        int i3 = (int) ((j - j2) % j3);
        int i4 = 0;
        int i5 = 0;
        while (true) {
            if (i4 >= i) {
                z = false;
                i4 = 0;
                break;
            }
            i5 += imageSource.getImagePropertyInt(i4, PropertyKey.GIF.DELAY_TIME, 0);
            if (i5 >= i3) {
                z = true;
                break;
            }
            i4++;
        }
        if (!z) {
            i4 = i - 1;
        }
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredSize = new Size(0, 0);
        decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOptions.desiredPixelFormat = PixelFormat.RGBA_8888;
        return imageSource.createPixelmap(i4, decodingOptions);
    }

    public static PixelMap createGifPixelmap(int i, ImageSource imageSource) {
        if (imageSource == null) {
            return null;
        }
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredSize = new Size(0, 0);
        decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
        decodingOptions.desiredPixelFormat = PixelFormat.RGBA_8888;
        return imageSource.createPixelmap(i, decodingOptions);
    }
}
