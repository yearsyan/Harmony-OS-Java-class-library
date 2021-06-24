package ohos.media.photokit.support;

import java.io.Closeable;
import ohos.app.Context;
import ohos.data.resultset.ResultSet;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class FetchResult<Type> implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(FetchResult.class);
    public static final String MEDIA_TYPE_ALBUM_LIST = "6";
    public static final String MEDIA_TYPE_ALBUM_LIST_INFO = "7";
    private static final String PERMISSION_MEDIA_LOCATION = "ohos.permission.MEDIA_LOCATION";
    private Context context;
    private int count;
    private boolean isClosed;
    private boolean isContain;
    private boolean isHideLocation;
    private ResultSet resultSet;
    private String type;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.isContain = false;
        this.count = 0;
        this.isClosed = true;
        ResultSet resultSet2 = this.resultSet;
        if (resultSet2 != null) {
            resultSet2.close();
        }
    }

    public FetchResult(Context context2, ResultSet resultSet2, String str) {
        this.context = context2;
        int verifySelfPermission = this.context.verifySelfPermission("ohos.permission.MEDIA_LOCATION");
        Logger logger = LOGGER;
        boolean z = true;
        Object[] objArr = new Object[1];
        objArr[0] = verifySelfPermission == 0 ? "granted" : "denied";
        logger.info("ohos.permission.MEDIA_LOCATION %{public}s", objArr);
        this.isHideLocation = verifySelfPermission != 0;
        this.resultSet = resultSet2;
        if (resultSet2 == null) {
            this.count = 0;
        } else {
            this.count = resultSet2.getRowCount();
        }
        this.isContain = this.count <= 0 ? false : z;
        this.isClosed = false;
        this.type = str;
    }

    public boolean isContain() {
        return this.isContain;
    }

    public int getCount() {
        return this.count;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public Type getPositionObject(int i) {
        ResultSet resultSet2;
        if (i < 0 || i > this.count - 1 || (resultSet2 = this.resultSet) == null || !resultSet2.goToRow(i)) {
            return null;
        }
        return getObject(this.resultSet);
    }

    public Type getFirstObject() {
        ResultSet resultSet2 = this.resultSet;
        if (resultSet2 != null && resultSet2.goToFirstRow()) {
            return getObject(this.resultSet);
        }
        return null;
    }

    public Type getNextObject() {
        ResultSet resultSet2 = this.resultSet;
        if (resultSet2 != null && resultSet2.goToNextRow()) {
            return getObject(this.resultSet);
        }
        return null;
    }

    public Type getLastObject() {
        ResultSet resultSet2 = this.resultSet;
        if (resultSet2 != null && resultSet2.goToLastRow()) {
            return getObject(this.resultSet);
        }
        return null;
    }

    private Type getObject(ResultSet resultSet2) {
        if (resultSet2 == null) {
            return null;
        }
        String str = this.type;
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != 54) {
            if (hashCode == 55 && str.equals(MEDIA_TYPE_ALBUM_LIST_INFO)) {
                c = 1;
            }
        } else if (str.equals(MEDIA_TYPE_ALBUM_LIST)) {
            c = 0;
        }
        if (c == 0) {
            return (Type) new AlbumList(resultSet2);
        }
        if (c != 1) {
            return null;
        }
        return (Type) new AlbumListInfo(resultSet2, this.isHideLocation);
    }
}
