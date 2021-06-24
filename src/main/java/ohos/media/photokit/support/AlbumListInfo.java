package ohos.media.photokit.support;

import ohos.data.resultset.ResultSet;

public class AlbumListInfo {
    private static final String DATE_MODIFIED = "date_modified";
    private static final String DATE_TAKEN = "datetaken";
    private static final String ID = "_id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String MIME_TYPE = "mime_type";
    private static final String PATH = "_data";
    private String data = "";
    private String dateModified = "";
    private String dateTaken = "";
    private String id = "";
    private String latitude = "0";
    private String longitude = "0";
    private String mimeType = "";

    public AlbumListInfo(ResultSet resultSet, boolean z) {
        if (resultSet != null) {
            this.id = resultSet.getString(resultSet.getColumnIndexForName("_id"));
            this.mimeType = resultSet.getString(resultSet.getColumnIndexForName("mime_type"));
            this.data = resultSet.getString(resultSet.getColumnIndexForName("_data"));
            this.dateTaken = resultSet.getString(resultSet.getColumnIndexForName(DATE_TAKEN));
            this.dateModified = resultSet.getString(resultSet.getColumnIndexForName("date_modified"));
            if (!z) {
                this.latitude = resultSet.getString(resultSet.getColumnIndexForName("latitude"));
                this.longitude = resultSet.getString(resultSet.getColumnIndexForName("longitude"));
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getData() {
        return this.data;
    }

    public String getDateTaken() {
        return this.dateTaken;
    }

    public String getDateModified() {
        return this.dateModified;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }
}
