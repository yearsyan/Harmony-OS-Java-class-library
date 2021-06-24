package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class VideoItem extends Schema<VideoItem> {
    public static final String ACTOR = "actor";
    public static final String DOWNLOAD_COUNT = "downloadCount";
    public static final String DURATION = "duration";
    public static final String RATING = "rating";
    public static final String SIZE = "size";
    public static final String WATCH_DURATION = "watchDuration";

    public static List<IndexForm> getVideoSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.VideoItem.AnonymousClass1 */

            {
                add(new IndexForm(VideoItem.ACTOR, IndexType.SORTED, false, true, true));
                add(new IndexForm("duration", IndexType.NO, false, true, false));
                add(new IndexForm(VideoItem.WATCH_DURATION, IndexType.NO, false, true, false));
                add(new IndexForm("size", IndexType.NO, false, true, false));
                add(new IndexForm("downloadCount", "long", false, true, false));
                add(new IndexForm("rating", "double", false, true, false));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public VideoItem() {
        super.set(this);
    }

    public VideoItem setActor(String str) {
        super.put(ACTOR, str);
        return this;
    }

    public VideoItem setDuration(Integer num) {
        super.put("duration", num);
        return this;
    }

    public VideoItem setWatchDuration(Integer num) {
        super.put(WATCH_DURATION, num);
        return this;
    }

    public VideoItem setRating(Double d) {
        super.put("rating", d);
        return this;
    }

    public VideoItem setDownloadCount(Long l) {
        super.put("downloadCount", l);
        return this;
    }

    public VideoItem setSize(Integer num) {
        super.put("size", num);
        return this;
    }

    public String getActor() {
        return super.getAsString(ACTOR);
    }

    public Integer getDuration() {
        return super.getAsInteger("duration");
    }

    public Integer getWatchDuration() {
        return super.getAsInteger(WATCH_DURATION);
    }

    public Double getRating() {
        return super.getAsDouble("rating");
    }

    public Long getDownloadCount() {
        return super.getAsLong("downloadCount");
    }

    public Integer getSize() {
        return super.getAsInteger("size");
    }
}
