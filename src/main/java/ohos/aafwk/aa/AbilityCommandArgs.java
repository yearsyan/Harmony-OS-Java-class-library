package ohos.aafwk.aa;

import java.util.Optional;

/* access modifiers changed from: package-private */
public class AbilityCommandArgs {
    private static final String OPTION_EXTRA = "--";
    private static final String OPTION_INTENT = "-";
    private int mArgPos;
    private String[] mArgs;
    private String mCurArg = null;

    public AbilityCommandArgs(String[] strArr, int i) {
        this.mArgs = strArr;
        this.mArgPos = i;
    }

    public Optional<String> getNextAbilityOption() {
        int i;
        String[] strArr = this.mArgs;
        if (strArr == null || (i = this.mArgPos) < 1) {
            return Optional.empty();
        }
        if (this.mCurArg != null) {
            String str = strArr[i - 1];
            AbilityToolUtils.printMessageToConsole("Error:no arg expected after option:" + str);
            throw new IllegalArgumentException("no arg expected after option:" + str);
        } else if (i >= strArr.length) {
            return Optional.empty();
        } else {
            String str2 = strArr[i];
            if (!str2.startsWith("-")) {
                return Optional.empty();
            }
            this.mArgPos++;
            if (OPTION_EXTRA.equals(str2)) {
                return Optional.empty();
            }
            Optional<String> baseOption = getBaseOption(str2);
            if (baseOption.isPresent()) {
                return baseOption;
            }
            this.mCurArg = null;
            return Optional.of(str2);
        }
    }

    private Optional<String> getBaseOption(String str) {
        if (str.length() <= 1 || str.charAt(1) == '-') {
            return Optional.empty();
        }
        if (str.length() > 2) {
            this.mCurArg = str.substring(2);
            return Optional.of(str.substring(0, 2));
        }
        this.mCurArg = null;
        return Optional.of(str);
    }

    public Optional<String> getNextAbilityArg() {
        String str = this.mCurArg;
        if (str != null) {
            this.mCurArg = null;
            return Optional.of(str);
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i >= strArr.length) {
            return Optional.empty();
        }
        this.mArgPos = i + 1;
        return Optional.of(strArr[i]);
    }

    public String getNextArgRequired() {
        if (this.mArgPos >= 1) {
            Optional<String> nextAbilityArg = getNextAbilityArg();
            if (nextAbilityArg.isPresent()) {
                return nextAbilityArg.get();
            }
            String str = this.mArgs[this.mArgPos - 1];
            AbilityToolUtils.printMessageToConsole("Error:no arg expected after option:" + str);
            throw new IllegalArgumentException("no arg expected after option:" + str);
        }
        AbilityToolUtils.printMessageToConsole("Error: arg init position error");
        throw new IllegalArgumentException("arg init position error");
    }
}
