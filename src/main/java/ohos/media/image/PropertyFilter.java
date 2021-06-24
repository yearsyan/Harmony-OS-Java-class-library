package ohos.media.image;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import ohos.media.image.common.Filter;
import ohos.media.image.exifadapter.ExifAdapter;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class PropertyFilter extends Filter {
    private static final Logger LOGGER = LoggerFactory.getImageLogger(PropertyFilter.class);
    private ExifAdapter mExifAdapter;
    private ConcurrentHashMap<String, String> mProperties = new ConcurrentHashMap<>();
    private String nullKeyTag = "NULL_KEY";

    public PropertyFilter setPropertyInt(String str, int i) {
        if (str == null) {
            ConcurrentHashMap<String, String> concurrentHashMap = this.mProperties;
            String str2 = this.nullKeyTag;
            concurrentHashMap.put(str2, str2);
            LOGGER.error("invalid input key of setPropertyInt.", new Object[0]);
            return this;
        }
        this.mProperties.put(str, String.valueOf(i));
        return this;
    }

    public PropertyFilter setPropertyDouble(String str, double d) {
        if (str == null) {
            ConcurrentHashMap<String, String> concurrentHashMap = this.mProperties;
            String str2 = this.nullKeyTag;
            concurrentHashMap.put(str2, str2);
            LOGGER.error("invalid input key of setPropertyDouble.", new Object[0]);
            return this;
        }
        this.mProperties.put(str, String.valueOf(d));
        return this;
    }

    public PropertyFilter setPropertyString(String str, String str2) {
        if (str == null) {
            ConcurrentHashMap<String, String> concurrentHashMap = this.mProperties;
            String str3 = this.nullKeyTag;
            concurrentHashMap.put(str3, str3);
            LOGGER.error("invalid input key of setPropertyString.", new Object[0]);
            return this;
        }
        this.mProperties.put(str, str2);
        return this;
    }

    public PropertyFilter rollbackProperty(String str) {
        if (str == null) {
            this.mProperties.remove(this.nullKeyTag);
            return this;
        }
        this.mProperties.remove(str);
        return this;
    }

    @Override // ohos.media.image.common.Filter
    public PropertyFilter restore() {
        this.mProperties.clear();
        return this;
    }

    @Override // ohos.media.image.common.Filter
    public long applyToSource(ImageSource imageSource) throws IOException {
        if (imageSource == null) {
            throw new IllegalArgumentException("invalid input ImageSource null value of applyToSource.");
        } else if (imageSource.isReleased()) {
            throw new IllegalStateException("invalid input ImageSource status of applyToSource.");
        } else if (this.mProperties.size() != 1 || !this.mProperties.containsKey(this.nullKeyTag)) {
            this.mProperties.remove(this.nullKeyTag);
            if (this.mProperties.size() == 0) {
                return imageSource.getFileSize();
            }
            this.mExifAdapter = imageSource.getExifAdapterInstance();
            for (Map.Entry<String, String> entry : this.mProperties.entrySet()) {
                this.mExifAdapter.setImageProperty(entry.getKey(), entry.getValue());
            }
            this.mExifAdapter.saveAttributes();
            ImageSource updateImageSource = imageSource.updateImageSource();
            if (updateImageSource != null) {
                return updateImageSource.getFileSize();
            }
            LOGGER.error("update source failed", new Object[0]);
            return -1;
        } else {
            LOGGER.error("invalid key set operation", new Object[0]);
            return -1;
        }
    }
}
