package ohos.utils.fastjson.serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ohos.utils.fastjson.PropertyNamingStrategy;
import ohos.utils.fastjson.annotation.JSONType;
import ohos.utils.fastjson.util.FieldInfo;
import ohos.utils.fastjson.util.TypeUtils;

public class JavaBeanSerializer implements ObjectSerializer {
    private static final char[] false_chars = {'f', 'a', 'l', 's', 'e'};
    private static final char[] true_chars = {'t', 'r', 'u', 'e'};
    protected int features;
    private final FieldSerializer[] getters;
    private final FieldSerializer[] sortedGetters;
    protected final String typeKey;
    protected final String typeName;

    public JavaBeanSerializer(Class<?> cls) {
        this(cls, (PropertyNamingStrategy) null);
    }

    public JavaBeanSerializer(Class<?> cls, PropertyNamingStrategy propertyNamingStrategy) {
        this(cls, cls.getModifiers(), null, false, true, true, true, propertyNamingStrategy);
    }

    public JavaBeanSerializer(Class<?> cls, String... strArr) {
        this(cls, cls.getModifiers(), map(strArr), false, true, true, true, null);
    }

    private static Map<String, String> map(String... strArr) {
        HashMap hashMap = new HashMap();
        for (String str : strArr) {
            hashMap.put(str, str);
        }
        return hashMap;
    }

    public JavaBeanSerializer(Class<?> cls, int i, Map<String, String> map, boolean z, boolean z2, boolean z3, boolean z4, PropertyNamingStrategy propertyNamingStrategy) {
        PropertyNamingStrategy propertyNamingStrategy2;
        String str;
        String str2;
        PropertyNamingStrategy naming;
        this.features = 0;
        String[] strArr = null;
        JSONType jSONType = z2 ? (JSONType) cls.getAnnotation(JSONType.class) : null;
        if (jSONType != null) {
            this.features = SerializerFeature.of(jSONType.serialzeFeatures());
            str2 = jSONType.typeName();
            if (str2.length() == 0) {
                str2 = null;
                str = null;
            } else {
                Class<? super Object> superclass = cls.getSuperclass();
                String str3 = null;
                while (superclass != null && superclass != Object.class) {
                    JSONType jSONType2 = (JSONType) superclass.getAnnotation(JSONType.class);
                    if (jSONType2 == null) {
                        break;
                    }
                    str3 = jSONType2.typeKey();
                    if (str3.length() != 0) {
                        break;
                    }
                    superclass = superclass.getSuperclass();
                }
                str = str3;
                for (Class<?> cls2 : cls.getInterfaces()) {
                    JSONType jSONType3 = (JSONType) cls2.getAnnotation(JSONType.class);
                    if (jSONType3 != null) {
                        str = jSONType3.typeKey();
                        if (str.length() != 0) {
                            break;
                        }
                    }
                }
                if (str != null && str.length() == 0) {
                    str = null;
                }
            }
            propertyNamingStrategy2 = (propertyNamingStrategy != null || (naming = jSONType.naming()) == PropertyNamingStrategy.CamelCase) ? propertyNamingStrategy : naming;
        } else {
            propertyNamingStrategy2 = propertyNamingStrategy;
            str2 = null;
            str = null;
        }
        this.typeName = str2;
        this.typeKey = str;
        List<FieldInfo> computeGetters = TypeUtils.computeGetters(cls, i, z, jSONType, map, false, z3, z4, propertyNamingStrategy2);
        ArrayList arrayList = new ArrayList();
        for (FieldInfo fieldInfo : computeGetters) {
            arrayList.add(new FieldSerializer(fieldInfo));
        }
        this.getters = (FieldSerializer[]) arrayList.toArray(new FieldSerializer[arrayList.size()]);
        strArr = jSONType != null ? jSONType.orders() : strArr;
        if (strArr == null || strArr.length == 0) {
            FieldSerializer[] fieldSerializerArr = this.getters;
            FieldSerializer[] fieldSerializerArr2 = new FieldSerializer[fieldSerializerArr.length];
            System.arraycopy(fieldSerializerArr, 0, fieldSerializerArr2, 0, fieldSerializerArr.length);
            Arrays.sort(fieldSerializerArr2);
            if (Arrays.equals(fieldSerializerArr2, this.getters)) {
                this.sortedGetters = this.getters;
            } else {
                this.sortedGetters = fieldSerializerArr2;
            }
        } else {
            List<FieldInfo> computeGetters2 = TypeUtils.computeGetters(cls, i, z, jSONType, map, true, z3, z4, propertyNamingStrategy2);
            ArrayList arrayList2 = new ArrayList();
            for (FieldInfo fieldInfo2 : computeGetters2) {
                arrayList2.add(new FieldSerializer(fieldInfo2));
            }
            this.sortedGetters = (FieldSerializer[]) arrayList2.toArray(new FieldSerializer[arrayList2.size()]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:225:0x0337, code lost:
        if ((r7 & r6.features) == 0) goto L_0x0423;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:275:0x03db, code lost:
        if (r6.isEnabled(ohos.utils.fastjson.serializer.SerializerFeature.WriteMapNullValue) == false) goto L_0x0423;
     */
    /* JADX WARNING: Removed duplicated region for block: B:150:0x0223 A[Catch:{ Exception -> 0x05d6, all -> 0x05d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x0254 A[Catch:{ Exception -> 0x05d6, all -> 0x05d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x0270 A[Catch:{ Exception -> 0x05d6, all -> 0x05d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x0277 A[Catch:{ Exception -> 0x05d6, all -> 0x05d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:175:0x027d A[Catch:{ Exception -> 0x05d6, all -> 0x05d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x02af A[Catch:{ Exception -> 0x05d6, all -> 0x05d0 }, LOOP:4: B:190:0x02a9->B:192:0x02af, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:362:0x04fc  */
    /* JADX WARNING: Removed duplicated region for block: B:442:0x0651 A[SYNTHETIC, Splitter:B:442:0x0651] */
    @Override // ohos.utils.fastjson.serializer.ObjectSerializer
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void write(ohos.utils.fastjson.serializer.JSONSerializer r36, java.lang.Object r37, java.lang.Object r38, java.lang.reflect.Type r39) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 1648
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.serializer.JavaBeanSerializer.write(ohos.utils.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type):void");
    }

    public Map<String, Object> getFieldValuesMap(Object obj) throws Exception {
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.sortedGetters.length);
        FieldSerializer[] fieldSerializerArr = this.sortedGetters;
        for (FieldSerializer fieldSerializer : fieldSerializerArr) {
            linkedHashMap.put(fieldSerializer.fieldInfo.name, fieldSerializer.getPropertyValue(obj));
        }
        return linkedHashMap;
    }
}
