package ohos.agp.components.webengine;

public interface PickFilesParams {
    public static final int TYPE_OPEN_MULTIPLE_FILES = 3;
    public static final int TYPE_OPEN_NOT_EXIST = 1;
    public static final int TYPE_OPEN_SINGLE_FILE = 2;

    String getDefaultName();

    String[] getMimeTypes();

    CharSequence getTitle();

    int getType();

    boolean isLiveMedia();
}
