package ohos.media.photokit.support;

import ohos.data.resultset.ResultSet;

public class AlbumList {
    private static final String ALBUM_CAPACITY = "albumCapacity";
    private static final String ALBUM_ID = "albumID";
    private static final String ALBUM_NAME = "albumName";
    private static final String ALBUM_TAG = "albumTag";
    private static final String CATEGORY_ID = "categoryID";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String COVER_DATA = "coverData";
    private static final String COVER_ID = "coverID";
    private String albumCapacity = "";
    private String albumId = "";
    private String albumName = "";
    private String albumTag = "";
    private String categoryId = "";
    private String categoryName = "";
    private String coverData = "";
    private String coverId = "";

    public String getAlbumName() {
        return "**";
    }

    public AlbumList(ResultSet resultSet) {
        if (resultSet != null) {
            this.categoryId = resultSet.getString(resultSet.getColumnIndexForName(CATEGORY_ID));
            this.categoryName = resultSet.getString(resultSet.getColumnIndexForName(CATEGORY_NAME));
            this.albumName = resultSet.getString(resultSet.getColumnIndexForName(ALBUM_NAME));
            this.albumId = resultSet.getString(resultSet.getColumnIndexForName(ALBUM_ID));
            this.albumCapacity = resultSet.getString(resultSet.getColumnIndexForName(ALBUM_CAPACITY));
            this.coverId = resultSet.getString(resultSet.getColumnIndexForName(COVER_ID));
            this.coverData = resultSet.getString(resultSet.getColumnIndexForName(COVER_DATA));
            this.albumTag = resultSet.getString(resultSet.getColumnIndexForName(ALBUM_TAG));
        }
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public String getAlbumCapacity() {
        return this.albumCapacity;
    }

    public String getCoverId() {
        return this.coverId;
    }

    public String getCoverData() {
        return this.coverData;
    }

    public String getAlbumTag() {
        return this.albumTag;
    }
}
