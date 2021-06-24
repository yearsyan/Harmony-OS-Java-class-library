package ohos.aafwk.abilityjet.databinding;

import java.io.File;
import java.io.IOException;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class DataBindingUtil {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108672, "DataBindingUtil:");
    private static final String XML_SUFFIX = ".sxml";

    private DataBindingUtil() {
    }

    public static <T extends DataBinding> T createBinding(int i, Context context) throws IOException {
        if (context != null) {
            ResourceManager resourceManager = context.getResourceManager();
            if (resourceManager != null) {
                try {
                    String string = resourceManager.getElement(i).getString();
                    if (string.endsWith(XML_SUFFIX)) {
                        HiLog.debug(LABEL, "createBinding get file path: %{public}s", string);
                        return (T) DataBindingFactory.create(generateClassName(string), context.getClassloader(), null);
                    }
                    throw new IllegalArgumentException("layoutId is invalid.");
                } catch (NotExistException unused) {
                    throw new IllegalArgumentException("layoutId is not exist");
                } catch (WrongTypeException unused2) {
                    throw new IllegalArgumentException("layoutId is wrong type.");
                }
            } else {
                throw new IllegalArgumentException("resource manager is null.");
            }
        } else {
            throw new IllegalArgumentException("context is null.");
        }
    }

    private static String upperFirstLetter(String str) {
        char[] charArray = str.toCharArray();
        if (charArray[0] >= 'a' && charArray[0] <= 'z') {
            charArray[0] = (char) (charArray[0] - ' ');
        }
        return String.valueOf(charArray);
    }

    public static <T extends DataBinding> T createBinding(int i, Context context, String str) throws IOException {
        if (context == null || str == null) {
            throw new IllegalArgumentException("context or packageName is null.");
        }
        ResourceManager resourceManager = context.getResourceManager();
        if (resourceManager != null) {
            try {
                String string = resourceManager.getElement(i).getString();
                if (string.endsWith(XML_SUFFIX)) {
                    HiLog.debug(LABEL, "createBinding get file path : %{public}s", string);
                    return (T) DataBindingFactory.create(generateClassName(string), context.getClassloader(), str);
                }
                throw new IllegalArgumentException("layoutId is invalid.");
            } catch (NotExistException unused) {
                throw new IllegalArgumentException("layoutId is not exist");
            } catch (WrongTypeException unused2) {
                throw new IllegalArgumentException("layoutId is wrong type.");
            }
        } else {
            throw new IllegalArgumentException("resource manager is null.");
        }
    }

    private static String generateClassName(String str) {
        String name = new File(str).getName();
        String[] split = name.substring(0, name.lastIndexOf(XML_SUFFIX)).split("\\_");
        String str2 = "";
        for (String str3 : split) {
            if (!str3.isEmpty()) {
                str2 = str2 + upperFirstLetter(str3);
            }
        }
        return str2;
    }
}
