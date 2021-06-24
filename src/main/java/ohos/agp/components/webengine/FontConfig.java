package ohos.agp.components.webengine;

public interface FontConfig {

    public enum FontGenericFamily {
        GENERIC_FAMILY_STANDARD,
        GENERIC_FAMILY_FIXED,
        GENERIC_FAMILY_SERIF,
        GENERIC_FAMILY_SANS_SERIF,
        GENERIC_FAMILY_CURSIVE,
        GENERIC_FAMILY_FANTASY
    }

    public enum FontSizeType {
        SIZE_TYPE_DEFAULT,
        SIZE_TYPE_DEFAULT_FIXED,
        SIZE_TYPE_MINIMUM,
        SIZE_TYPE_MINIMUM_LOGICAL
    }

    String getFontFamily(FontGenericFamily fontGenericFamily);

    int getFontSize(FontSizeType fontSizeType);

    void setFontFamily(FontGenericFamily fontGenericFamily, String str);

    void setFontSize(FontSizeType fontSizeType, int i);
}
