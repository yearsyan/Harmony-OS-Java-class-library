package ohos.media.photokit.adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import java.io.FileDescriptor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ohos.app.Context;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.inner.ImageDoubleFwConverter;
import ohos.media.photokit.common.PixelMapConfigs;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;
import ohos.utils.net.Uri;

public class AVMetadataHelperAdapter {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVMetadataHelperAdapter.class);
    private ImageSource imageSource = null;
    private final MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public boolean setSource(String str) {
        this.imageSource = ImageSource.create(str, (ImageSource.SourceOptions) null);
        this.retriever.setDataSource(str);
        return true;
    }

    public boolean setSource(FileDescriptor fileDescriptor) {
        this.imageSource = ImageSource.create(fileDescriptor, (ImageSource.SourceOptions) null);
        this.retriever.setDataSource(fileDescriptor);
        return true;
    }

    public boolean setSource(FileDescriptor fileDescriptor, long j, long j2) {
        this.imageSource = ImageSource.create(fileDescriptor, (ImageSource.SourceOptions) null);
        this.retriever.setDataSource(fileDescriptor, j, j2);
        return true;
    }

    public boolean setSource(String str, Map<String, String> map) {
        this.retriever.setDataSource(str, map);
        return true;
    }

    public boolean setSource(Context context, Uri uri) {
        this.retriever.setDataSource((android.content.Context) context.getHostContext(), android.net.Uri.parse(uri.toString()));
        return true;
    }

    public PixelMap fetchVideoScaledPixelMapByTime(long j, int i, int i2, int i3) {
        return ImageDoubleFwConverter.createShellPixelMap(this.retriever.getScaledFrameAtTime(j, i, i2, i3));
    }

    public PixelMap fetchVideoPixelMapByTime(long j, int i) {
        return ImageDoubleFwConverter.createShellPixelMap(this.retriever.getFrameAtTime(j, i));
    }

    public PixelMap fetchVideoPixelMapByTime(long j) {
        return ImageDoubleFwConverter.createShellPixelMap(this.retriever.getFrameAtTime(j));
    }

    public PixelMap fetchVideoPixelMapByTime() {
        return ImageDoubleFwConverter.createShellPixelMap(this.retriever.getFrameAtTime());
    }

    public String resolveMetadata(int i) {
        return this.retriever.extractMetadata(i);
    }

    public byte[] resolveImage() {
        return this.retriever.getEmbeddedPicture();
    }

    public PixelMap fetchVideoPixelMapByIndex(int i, PixelMapConfigs pixelMapConfigs) {
        return fetchVideoPixelMapsByIndex(i, 1, pixelMapConfigs).get(0);
    }

    public PixelMap fetchVideoPixelMapByIndex(int i) {
        return fetchVideoPixelMapsByIndex(i, 1).get(0);
    }

    public List<PixelMap> fetchVideoPixelMapsByIndex(int i, int i2, PixelMapConfigs pixelMapConfigs) {
        return getFramesAtIndexInternal(i, i2, pixelMapConfigs);
    }

    public List<PixelMap> fetchVideoPixelMapsByIndex(int i, int i2) {
        return getFramesAtIndexInternal(i, i2, null);
    }

    private List<PixelMap> getFramesAtIndexInternal(int i, int i2, PixelMapConfigs pixelMapConfigs) {
        return (List) this.retriever.getFramesAtIndex(i, i2, convertPixelMapConfigsToBitmapParams(pixelMapConfigs)).stream().map($$Lambda$UgcuZ9GI5zEleF3oROMthyyZ2U.INSTANCE).collect(Collectors.toList());
    }

    private MediaMetadataRetriever.BitmapParams convertPixelMapConfigsToBitmapParams(PixelMapConfigs pixelMapConfigs) {
        MediaMetadataRetriever.BitmapParams bitmapParams = new MediaMetadataRetriever.BitmapParams();
        if (pixelMapConfigs == null) {
            return bitmapParams;
        }
        int i = AnonymousClass1.$SwitchMap$ohos$media$image$common$PixelFormat[pixelMapConfigs.getPreferredConfig().ordinal()];
        if (i == 1) {
            bitmapParams.setPreferredConfig(Bitmap.Config.ARGB_8888);
        } else if (i == 2) {
            bitmapParams.setPreferredConfig(Bitmap.Config.RGB_565);
        }
        return bitmapParams;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.media.photokit.adapter.AVMetadataHelperAdapter$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$media$image$common$PixelFormat = new int[PixelFormat.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                ohos.media.image.common.PixelFormat[] r0 = ohos.media.image.common.PixelFormat.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.media.photokit.adapter.AVMetadataHelperAdapter.AnonymousClass1.$SwitchMap$ohos$media$image$common$PixelFormat = r0
                int[] r0 = ohos.media.photokit.adapter.AVMetadataHelperAdapter.AnonymousClass1.$SwitchMap$ohos$media$image$common$PixelFormat     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.media.image.common.PixelFormat r1 = ohos.media.image.common.PixelFormat.ARGB_8888     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.media.photokit.adapter.AVMetadataHelperAdapter.AnonymousClass1.$SwitchMap$ohos$media$image$common$PixelFormat     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.media.image.common.PixelFormat r1 = ohos.media.image.common.PixelFormat.RGB_565     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.media.photokit.adapter.AVMetadataHelperAdapter.AnonymousClass1.<clinit>():void");
        }
    }

    public PixelMap fetchImagePixelMapByIndex(int i, PixelMapConfigs pixelMapConfigs) {
        return getImageAtIndexInternal(i, pixelMapConfigs);
    }

    public PixelMap fetchImagePixelMapByIndex(int i) {
        return getImageAtIndexInternal(i, null);
    }

    public PixelMap fetchImagePrimaryPixelMap(PixelMapConfigs pixelMapConfigs) {
        return getImageAtIndexInternal(-1, pixelMapConfigs);
    }

    public PixelMap fetchImagePrimaryPixelMap() {
        return getImageAtIndexInternal(-1, null);
    }

    private PixelMap getImageAtIndexInternal(int i, PixelMapConfigs pixelMapConfigs) {
        Bitmap imageAtIndex = this.retriever.getImageAtIndex(i, convertPixelMapConfigsToBitmapParams(pixelMapConfigs));
        if (imageAtIndex == null) {
            LOGGER.error("retriever getImageAtIndex bitmap is null so try imageSource", new Object[0]);
            ImageSource imageSource2 = this.imageSource;
            if (imageSource2 != null) {
                return imageSource2.createThumbnailPixelmap(null, true);
            }
        }
        return ImageDoubleFwConverter.createShellPixelMap(imageAtIndex);
    }

    public void release() {
        this.retriever.close();
    }
}
