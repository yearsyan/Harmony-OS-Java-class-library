package ohos.media.photokit;

import ohos.app.Context;
import ohos.data.resultset.ResultSet;
import ohos.media.photokit.photokitfwk.PhotoData;
import ohos.media.photokit.support.AlbumList;
import ohos.media.photokit.support.AlbumListInfo;
import ohos.media.photokit.support.FetchResult;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class MediaLibrary {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(MediaLibrary.class);
    private Context context;
    private PhotoData photoData = new PhotoData(this.context);

    public MediaLibrary(Context context2) {
        this.context = context2;
    }

    public FetchResult<AlbumList> getPrivateAlbums() {
        if (this.context == null) {
            return null;
        }
        ResultSet privateAlbums = this.photoData.getPrivateAlbums();
        FetchResult<AlbumList> fetchResult = new FetchResult<>(this.context, privateAlbums, FetchResult.MEDIA_TYPE_ALBUM_LIST);
        if (privateAlbums == null) {
            LOGGER.error("private albums result is null", new Object[0]);
        }
        return fetchResult;
    }

    public FetchResult<AlbumList> getSearchAlbums(String[] strArr) {
        if (this.context == null) {
            return null;
        }
        ResultSet searchAlbums = this.photoData.getSearchAlbums(strArr);
        FetchResult<AlbumList> fetchResult = new FetchResult<>(this.context, searchAlbums, FetchResult.MEDIA_TYPE_ALBUM_LIST);
        if (searchAlbums == null) {
            LOGGER.error("search albums result is null", new Object[0]);
        }
        return fetchResult;
    }

    public FetchResult<AlbumList> getClassifyAlbums() {
        if (this.context == null) {
            return null;
        }
        ResultSet classifyAlbums = this.photoData.getClassifyAlbums();
        FetchResult<AlbumList> fetchResult = new FetchResult<>(this.context, classifyAlbums, FetchResult.MEDIA_TYPE_ALBUM_LIST);
        if (classifyAlbums == null) {
            LOGGER.error("classify albums result is null", new Object[0]);
        }
        return fetchResult;
    }

    public FetchResult<AlbumListInfo> getAlbumListInfo(String[] strArr) {
        if (this.context == null) {
            return null;
        }
        ResultSet smartAlbumsInfo = this.photoData.getSmartAlbumsInfo(strArr);
        FetchResult<AlbumListInfo> fetchResult = new FetchResult<>(this.context, smartAlbumsInfo, FetchResult.MEDIA_TYPE_ALBUM_LIST_INFO);
        if (smartAlbumsInfo == null) {
            LOGGER.error("album list info result is null", new Object[0]);
        }
        return fetchResult;
    }
}
