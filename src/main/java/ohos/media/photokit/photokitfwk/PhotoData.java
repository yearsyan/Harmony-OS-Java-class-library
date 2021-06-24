package ohos.media.photokit.photokitfwk;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.media.photokit.adapter.PhotoDataAdapter;
import ohos.media.photokit.metadata.AVStorage;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;
import ohos.utils.net.Uri;

public class PhotoData {
    private static final Uri CLASSIFY_ALBUMS_URI = Uri.parse("dataability:///com.open.gallery.smart.provider/albums");
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(PhotoData.class);
    private static final Uri PRIVATE_ALBUM_URI = Uri.parse("dataability:///com.open.gallery.smart.provider/private");
    private static final Uri SEARCH_ALBUMS_URI = Uri.parse("dataability:///com.open.gallery.smart.provider/search");
    private static final Uri SMART_ALBUMS_INFO_URI = Uri.parse("dataability:///com.open.gallery.smart.provider/albumInfo");
    private Context context;
    private DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(this.context);
    private String digest;

    public PhotoData(Context context2) {
        this.context = context2;
        String bundleName = this.context.getBundleName();
        LOGGER.debug("bundleName:%{public}s", bundleName);
        this.digest = PhotoDataAdapter.getSignatureDigest(this.context, bundleName);
    }

    public ResultSet getPrivateAlbums() {
        String[] strArr = {"categoryID", "categoryName", "albumID", "albumName", "albumCapacity", "coverData", "albumTag", "coverID"};
        try {
            LOGGER.info("start query private albums", new Object[0]);
            DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates();
            dataAbilityPredicates.setOrder(this.digest);
            return this.dataAbilityHelper.query(PRIVATE_ALBUM_URI, strArr, dataAbilityPredicates);
        } catch (DataAbilityRemoteException unused) {
            LOGGER.error("a remote process exception occurs.", new Object[0]);
        } catch (IllegalStateException unused2) {
            LOGGER.error("the dataAbility does not exist.", new Object[0]);
        } catch (Exception unused3) {
            LOGGER.error("an unknown exception occurs, maybe permission is denied", new Object[0]);
        }
        return null;
    }

    public ResultSet getSmartAlbumsInfo(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        String[] strArr2 = {"_id", AVStorage.AVBaseColumns.DATA, AVStorage.AVBaseColumns.MIME_TYPE, AVStorage.AVBaseColumns.DATE_MODIFIED, "datetaken", "latitude", "longitude"};
        try {
            LOGGER.info("start query albums info", new Object[0]);
            DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates();
            dataAbilityPredicates.setOrder(this.digest);
            dataAbilityPredicates.equalTo("albumID", strArr[0]);
            return this.dataAbilityHelper.query(SMART_ALBUMS_INFO_URI, strArr2, dataAbilityPredicates);
        } catch (DataAbilityRemoteException unused) {
            LOGGER.error("a remote process exception occurs.", new Object[0]);
            return null;
        } catch (IllegalStateException unused2) {
            LOGGER.error("the dataAbility does not exist.", new Object[0]);
            return null;
        } catch (Exception unused3) {
            LOGGER.error("an unknown exception occurs, maybe permission is denied", new Object[0]);
            return null;
        }
    }

    public ResultSet getClassifyAlbums() {
        String[] strArr = {"categoryName", "albumID", "albumName", "albumCapacity", "coverID", "coverData", "categoryID", "albumTag"};
        try {
            LOGGER.info("start query classify albums", new Object[0]);
            DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates();
            dataAbilityPredicates.setOrder(this.digest);
            return this.dataAbilityHelper.query(CLASSIFY_ALBUMS_URI, strArr, dataAbilityPredicates);
        } catch (DataAbilityRemoteException unused) {
            LOGGER.error("a remote process exception occurs.", new Object[0]);
        } catch (IllegalStateException unused2) {
            LOGGER.error("the dataAbility does not exist.", new Object[0]);
        } catch (Exception unused3) {
            LOGGER.error("an unknown exception occurs, maybe permission is denied", new Object[0]);
        }
        return null;
    }

    public ResultSet getSearchAlbums(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        String[] strArr2 = {"categoryID", "categoryName", "albumID", "albumName", "albumCapacity", "coverData", "albumTag", "coverID"};
        try {
            LOGGER.info("start query search albums", new Object[0]);
            DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates();
            dataAbilityPredicates.setOrder(this.digest);
            dataAbilityPredicates.equalTo("keyword", strArr[0]);
            return this.dataAbilityHelper.query(SEARCH_ALBUMS_URI, strArr2, dataAbilityPredicates);
        } catch (DataAbilityRemoteException unused) {
            LOGGER.error("a remote process exception occurs.", new Object[0]);
            return null;
        } catch (IllegalStateException unused2) {
            LOGGER.error("the dataAbility does not exist.", new Object[0]);
            return null;
        } catch (Exception unused3) {
            LOGGER.error("an unknown exception occurs, maybe permission is denied", new Object[0]);
            return null;
        }
    }
}
