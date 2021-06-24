package ohos.global.resource.solidxml;

import java.io.IOException;
import java.util.HashMap;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;

public abstract class Theme {
    public abstract boolean getBooleanValue(String str, boolean z) throws NotExistException, IOException, WrongTypeException;

    public abstract int getColorValue(String str) throws NotExistException, IOException, WrongTypeException;

    public abstract Theme getCombinedTheme(Theme theme);

    public abstract float getFloatValue(String str, float f) throws NotExistException, IOException, WrongTypeException;

    public abstract int getIntegerValue(String str, int i) throws NotExistException, IOException, WrongTypeException;

    public abstract Resource getMediaValue(String str, ResourceManager resourceManager) throws NotExistException, IOException, WrongTypeException;

    public abstract Pattern getPatternValue(String str) throws NotExistException, IOException, WrongTypeException;

    public abstract int getPixelValue(String str, boolean z) throws NotExistException, IOException, WrongTypeException;

    public abstract String getStringValue(String str) throws NotExistException, IOException, WrongTypeException;

    public abstract HashMap<String, TypedAttribute> getThemeHash();

    public abstract HashMap<String, TypedAttribute> getThemeHash(String[] strArr);

    public abstract TypedAttribute getValue(String str) throws NotExistException;

    public abstract void set(Theme theme);

    public abstract int size();
}
