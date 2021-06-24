package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class MusicItem extends Schema<MusicItem> {
    public static final String ALBUM = "album";
    public static final String ARTIST = "artist";
    public static final String DURATION = "duration";
    public static final String GENRE = "genre";
    public static final String SIZE = "size";

    public static List<IndexForm> getMusicSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.MusicItem.AnonymousClass1 */

            {
                add(new IndexForm(MusicItem.ARTIST, IndexType.SORTED, false, true, true));
                add(new IndexForm(MusicItem.GENRE, IndexType.SORTED, false, true, true));
                add(new IndexForm("duration", IndexType.NO, false, true, false));
                add(new IndexForm(MusicItem.ALBUM, IndexType.SORTED, false, true, true));
                add(new IndexForm("size", IndexType.NO, false, true, false));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public MusicItem() {
        super.set(this);
    }

    public MusicItem setArtist(String str) {
        super.put(ARTIST, str);
        return this;
    }

    public MusicItem setGenre(String str) {
        super.put(GENRE, str);
        return this;
    }

    public MusicItem setDuration(Integer num) {
        super.put("duration", num);
        return this;
    }

    public MusicItem setAlbum(String str) {
        super.put(ALBUM, str);
        return this;
    }

    public MusicItem setSize(Integer num) {
        super.put("size", num);
        return this;
    }

    public Integer getDuration() {
        return super.getAsInteger("duration");
    }

    public String getArtist() {
        return super.getAsString(ARTIST);
    }

    public String getGenre() {
        return super.getAsString(GENRE);
    }

    public String getAlbum() {
        return super.getAsString(ALBUM);
    }

    public Integer getSize() {
        return super.getAsInteger("size");
    }
}
