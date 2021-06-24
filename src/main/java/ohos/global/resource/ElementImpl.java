package ohos.global.resource;

import java.io.IOException;
import java.text.Format;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.icu.text.PluralRules;
import ohos.global.resource.solidxml.PatternImpl;
import ohos.global.resource.solidxml.Theme;
import ohos.global.resource.solidxml.ThemeImpl;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.global.resource.solidxml.TypedAttributeImpl;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ElementImpl extends Element {
    private static final Pattern COLOR_PATTERN = Pattern.compile("^#(?<color>([A-F0-9]{6}|[A-F0-9]{8}))$");
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "ElementImpl");
    private static final Pattern REGEX_PATTERN = Pattern.compile("^(\\$theme:|\\$ohos:theme:|\\$pattern:|\\$ohos:pattern:)(?<value>\\d+$)");
    private final Object LOCK = new Object();
    private Configuration config;
    private DeviceCapability deviceCapability;
    private volatile ohos.global.resource.solidxml.Pattern mPattern = null;
    private volatile Theme mTheme = null;
    private Object obj;
    private ResourceManagerImpl resourceManager;

    @Override // ohos.global.resource.Element
    public String getConfig() throws NotExistException, IOException, WrongTypeException {
        return "";
    }

    private ElementImpl() {
    }

    public ElementImpl(Object obj2, ResourceManagerImpl resourceManagerImpl) throws NotExistException, IOException, WrongTypeException {
        if (obj2 != null) {
            this.obj = obj2;
            this.resourceManager = resourceManagerImpl;
            this.config = resourceManagerImpl.getConfiguration();
            this.deviceCapability = resourceManagerImpl.getDeviceCapability();
            return;
        }
        throw new NotExistException("resource not found");
    }

    @Override // ohos.global.resource.Element
    public String getString() throws NotExistException, IOException, WrongTypeException {
        Object obj2 = this.obj;
        if (obj2 instanceof String) {
            return this.resourceManager.tryDereferrence1((String) obj2);
        }
        throw new WrongTypeException("type is not string");
    }

    @Override // ohos.global.resource.Element
    public String getString(Object... objArr) throws NotExistException, IOException, WrongTypeException {
        return String.format(this.config.getFirstLocale(), getString(), objArr);
    }

    @Override // ohos.global.resource.Element
    public String getString(Object obj2, Format format) throws NotExistException, IOException, WrongTypeException {
        String string = getString();
        return String.format(this.config.getFirstLocale(), string, format.format(obj2));
    }

    @Override // ohos.global.resource.Element
    public String getString(Object[] objArr, Format[] formatArr) throws NotExistException, IOException, WrongTypeException {
        Object[] objArr2 = new Object[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            objArr2[i] = formatArr[i].format(objArr[i]);
        }
        return String.format(this.config.getFirstLocale(), getString(), objArr2);
    }

    @Override // ohos.global.resource.Element
    public boolean getBoolean() throws NotExistException, IOException, WrongTypeException {
        return Boolean.parseBoolean(getString());
    }

    @Override // ohos.global.resource.Element
    public int getColor() throws NotExistException, IOException, WrongTypeException {
        Matcher matcher = COLOR_PATTERN.matcher(getString().toUpperCase());
        if (matcher.find()) {
            String group = matcher.group("color");
            int parseUnsignedInt = Integer.parseUnsignedInt(group, 16);
            return group.length() == 6 ? parseUnsignedInt | -16777216 : parseUnsignedInt;
        }
        throw new WrongTypeException("color format not correct.");
    }

    @Override // ohos.global.resource.Element
    public float getFloat() throws NotExistException, IOException, WrongTypeException {
        return this.resourceManager.parseFloat(getString());
    }

    @Override // ohos.global.resource.Element
    public int getInteger() throws NotExistException, IOException, WrongTypeException {
        try {
            return Integer.parseInt(getString());
        } catch (NumberFormatException unused) {
            throw new WrongTypeException("not a valid integer");
        }
    }

    @Override // ohos.global.resource.Element
    public String[] getStringArray() throws NotExistException, IOException, WrongTypeException {
        Object obj2 = this.obj;
        if (obj2 instanceof String[]) {
            String[] strArr = (String[]) obj2;
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = this.resourceManager.tryDereferrence1(strArr[i]);
            }
            return strArr;
        }
        throw new WrongTypeException("type is not array");
    }

    @Override // ohos.global.resource.Element
    public int[] getIntArray() throws NotExistException, IOException, WrongTypeException {
        String[] stringArray = getStringArray();
        if (stringArray == null || stringArray.length <= 0) {
            return new int[0];
        }
        int[] iArr = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            try {
                iArr[i] = Integer.parseInt(this.resourceManager.tryDereferrence1(stringArray[i]));
            } catch (NumberFormatException unused) {
                throw new WrongTypeException("wrong type");
            }
        }
        return iArr;
    }

    @Override // ohos.global.resource.Element
    public String getPluralString(int i) throws NotExistException, IOException, WrongTypeException {
        PluralRules pluralRules = this.resourceManager.getPluralRules();
        String[] stringArray = getStringArray();
        String select = pluralRules.select((double) i);
        String str = null;
        String str2 = null;
        for (int i2 = 0; i2 < stringArray.length; i2 += 2) {
            if (stringArray[i2].equals(select)) {
                str = stringArray[i2 + 1];
            } else if (stringArray[i2].equals("other")) {
                str2 = stringArray[i2 + 1];
            } else {
                HiLog.error(LABEL, "No suitable plural rules", new Object[0]);
            }
        }
        if (str != null) {
            return str;
        }
        if (str2 != null) {
            return str2;
        }
        throw new NotExistException("Suitable plural rules not exist");
    }

    @Override // ohos.global.resource.Element
    public String getPluralString(int i, Object... objArr) throws NotExistException, IOException, WrongTypeException {
        return String.format(this.config.getFirstLocale(), getPluralString(i), objArr);
    }

    @Override // ohos.global.resource.Element
    public Theme getTheme() throws NotExistException, IOException, WrongTypeException {
        if (this.mTheme == null) {
            synchronized (this.LOCK) {
                if (this.mTheme == null) {
                    this.mTheme = new ThemeImpl(generateAttr());
                }
            }
        }
        return this.mTheme;
    }

    @Override // ohos.global.resource.Element
    public ohos.global.resource.solidxml.Pattern getPattern() throws NotExistException, IOException, WrongTypeException {
        if (this.mPattern == null) {
            synchronized (this.LOCK) {
                if (this.mPattern == null) {
                    this.mPattern = new PatternImpl(generateAttr());
                }
            }
        }
        return this.mPattern;
    }

    private HashMap<String, TypedAttribute> generateAttr() throws NotExistException, IOException, WrongTypeException {
        String[] stringArray = getStringArray();
        Stack stack = new Stack();
        if (stringArray == null || stringArray.length == 0) {
            return new HashMap<>();
        }
        stack.push(stringArray);
        while (true) {
            String[] strArr = (String[]) stack.peek();
            if (strArr == null || strArr.length <= 0) {
                break;
            }
            Matcher matcher = REGEX_PATTERN.matcher(strArr[0]);
            if (!matcher.find()) {
                break;
            }
            try {
                String[] stringArray2 = this.resourceManager.getElement(Integer.parseInt(matcher.group("value"))).getStringArray();
                if (stringArray2 != null) {
                    if (stringArray2.length == 0) {
                        break;
                    }
                    stack.push(stringArray2);
                    if (stack.size() <= 0) {
                        break;
                    }
                } else {
                    break;
                }
            } catch (NumberFormatException unused) {
                HiLog.error(LABEL, "Theme reference invalid", new Object[0]);
            }
        }
        HashMap<String, TypedAttribute> hashMap = new HashMap<>();
        do {
            String[] strArr2 = (String[]) stack.pop();
            for (int i = (strArr2.length <= 0 || !REGEX_PATTERN.matcher(strArr2[0]).find()) ? 0 : 1; i < strArr2.length - 1; i += 2) {
                hashMap.put(strArr2[i], new TypedAttributeImpl(this.resourceManager, strArr2[i], strArr2[i + 1]));
            }
        } while (stack.size() != 0);
        return hashMap;
    }
}
