package ohos.global.icu.impl.number;

import ohos.global.icu.impl.StandardPlural;
import ohos.global.icu.impl.number.Modifier;

public interface ModifierStore {
    Modifier getModifier(Modifier.Signum signum, StandardPlural standardPlural);
}
