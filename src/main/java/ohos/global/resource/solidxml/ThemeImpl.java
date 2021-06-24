package ohos.global.resource.solidxml;

import java.io.IOException;
import java.util.HashMap;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;

public class ThemeImpl extends Theme {
    private HashMap<String, TypedAttribute> themeHash = new HashMap<>();

    public ThemeImpl(HashMap<String, TypedAttribute> hashMap) {
        if (hashMap != null) {
            this.themeHash = hashMap;
        }
    }

    public void setThemeHash(HashMap<String, TypedAttribute> hashMap) {
        if (hashMap != null) {
            this.themeHash = hashMap;
        }
    }

    @Override // ohos.global.resource.solidxml.Theme
    public HashMap<String, TypedAttribute> getThemeHash() {
        return this.themeHash;
    }

    @Override // ohos.global.resource.solidxml.Theme
    public Theme getCombinedTheme(Theme theme) {
        HashMap<String, TypedAttribute> themeHash2 = theme.getThemeHash();
        HashMap hashMap = new HashMap(this.themeHash.size() + themeHash2.size());
        hashMap.putAll(this.themeHash);
        hashMap.putAll(themeHash2);
        return new ThemeImpl(hashMap);
    }

    @Override // ohos.global.resource.solidxml.Theme
    public void set(Theme theme) {
        if (theme != null) {
            if (this.themeHash == null) {
                this.themeHash = new HashMap<>();
            }
            this.themeHash.clear();
            this.themeHash.putAll(theme.getThemeHash());
        }
    }

    @Override // ohos.global.resource.solidxml.Theme
    public HashMap<String, TypedAttribute> getThemeHash(String[] strArr) {
        if (this.themeHash == null || strArr == null || strArr.length == 0) {
            return new HashMap<>();
        }
        HashMap<String, TypedAttribute> hashMap = new HashMap<>(strArr.length);
        for (String str : strArr) {
            TypedAttribute typedAttribute = this.themeHash.get(str);
            if (typedAttribute != null) {
                hashMap.put(str, typedAttribute);
            }
        }
        return hashMap;
    }

    @Override // ohos.global.resource.solidxml.Theme
    public String getStringValue(String str) throws NotExistException, IOException, WrongTypeException {
        if (this.themeHash.containsKey(str)) {
            return this.themeHash.get(str).getStringValue();
        }
        throw new NotExistException("get string value error, key not found");
    }

    @Override // ohos.global.resource.solidxml.Theme
    public boolean getBooleanValue(String str, boolean z) throws NotExistException, IOException, WrongTypeException {
        return this.themeHash.containsKey(str) ? this.themeHash.get(str).getBooleanValue() : z;
    }

    @Override // ohos.global.resource.solidxml.Theme
    public int getColorValue(String str) throws NotExistException, IOException, WrongTypeException {
        if (this.themeHash.containsKey(str)) {
            return this.themeHash.get(str).getColorValue();
        }
        throw new NotExistException("get color value error, key not found");
    }

    @Override // ohos.global.resource.solidxml.Theme
    public int getIntegerValue(String str, int i) throws NotExistException, IOException, WrongTypeException {
        return this.themeHash.containsKey(str) ? this.themeHash.get(str).getIntegerValue() : i;
    }

    @Override // ohos.global.resource.solidxml.Theme
    public float getFloatValue(String str, float f) throws NotExistException, IOException, WrongTypeException {
        return this.themeHash.containsKey(str) ? this.themeHash.get(str).getFloatValue() : f;
    }

    @Override // ohos.global.resource.solidxml.Theme
    public int getPixelValue(String str, boolean z) throws NotExistException, IOException, WrongTypeException {
        if (this.themeHash.containsKey(str)) {
            float floatValue = this.themeHash.get(str).getFloatValue();
            if (z) {
                return (int) floatValue;
            }
            return Math.round(floatValue);
        }
        throw new NotExistException("get pixel size value error, key not found");
    }

    @Override // ohos.global.resource.solidxml.Theme
    public Resource getMediaValue(String str, ResourceManager resourceManager) throws NotExistException, IOException, WrongTypeException {
        if (this.themeHash.containsKey(str)) {
            return resourceManager.getRawFileEntry(this.themeHash.get(str).getMediaValue()).openRawFile();
        }
        throw new NotExistException("get media value error, key not found");
    }

    @Override // ohos.global.resource.solidxml.Theme
    public TypedAttribute getValue(String str) throws NotExistException {
        if (this.themeHash.containsKey(str)) {
            return this.themeHash.get(str);
        }
        throw new NotExistException("get attribute error, key not found");
    }

    @Override // ohos.global.resource.solidxml.Theme
    public int size() {
        return this.themeHash.size();
    }

    @Override // ohos.global.resource.solidxml.Theme
    public Pattern getPatternValue(String str) throws NotExistException, IOException, WrongTypeException {
        if (this.themeHash.containsKey(str)) {
            return this.themeHash.get(str).getPatternValue();
        }
        throw new NotExistException("get pattern error, key not found");
    }
}
