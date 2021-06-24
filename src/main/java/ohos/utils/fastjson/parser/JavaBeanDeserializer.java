package ohos.utils.fastjson.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.JSONObject;
import ohos.utils.fastjson.parser.deserializer.ExtraProcessable;
import ohos.utils.fastjson.parser.deserializer.ExtraProcessor;
import ohos.utils.fastjson.parser.deserializer.ExtraTypeProvider;
import ohos.utils.fastjson.parser.deserializer.FieldDeserializer;
import ohos.utils.fastjson.parser.deserializer.ObjectDeserializer;
import ohos.utils.fastjson.util.FieldInfo;
import ohos.utils.fastjson.util.TypeUtils;

public class JavaBeanDeserializer implements ObjectDeserializer {
    private final Map<String, FieldDeserializer> alterNameFieldDeserializers;
    public final JavaBeanInfo beanInfo;
    protected final Class<?> clazz;
    private ConcurrentMap<String, Object> extraFieldDeserializers;
    private final FieldDeserializer[] fieldDeserializers;
    private transient long[] smartMatchHashArray;
    private transient int[] smartMatchHashArrayMapping;
    private final FieldDeserializer[] sortedFieldDeserializers;

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type) {
        this(parserConfig, cls, type, JavaBeanInfo.build(cls, cls.getModifiers(), type, false, true, true, true, parserConfig.propertyNamingStrategy));
    }

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type, JavaBeanInfo javaBeanInfo) {
        this.clazz = cls;
        this.beanInfo = javaBeanInfo;
        this.sortedFieldDeserializers = new FieldDeserializer[javaBeanInfo.sortedFields.length];
        int length = javaBeanInfo.sortedFields.length;
        HashMap hashMap = null;
        int i = 0;
        while (i < length) {
            FieldInfo fieldInfo = javaBeanInfo.sortedFields[i];
            FieldDeserializer createFieldDeserializer = parserConfig.createFieldDeserializer(parserConfig, cls, fieldInfo);
            this.sortedFieldDeserializers[i] = createFieldDeserializer;
            String[] strArr = fieldInfo.alternateNames;
            HashMap hashMap2 = hashMap;
            for (String str : strArr) {
                if (hashMap2 == null) {
                    hashMap2 = new HashMap();
                }
                hashMap2.put(str, createFieldDeserializer);
            }
            i++;
            hashMap = hashMap2;
        }
        this.alterNameFieldDeserializers = hashMap;
        this.fieldDeserializers = new FieldDeserializer[javaBeanInfo.fields.length];
        int length2 = javaBeanInfo.fields.length;
        for (int i2 = 0; i2 < length2; i2++) {
            this.fieldDeserializers[i2] = getFieldDeserializer(javaBeanInfo.fields[i2].name);
        }
    }

    /* access modifiers changed from: protected */
    public Object createInstance(DefaultJSONParser defaultJSONParser, Type type) {
        Object obj;
        if ((type instanceof Class) && this.clazz.isInterface()) {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{(Class) type}, new JSONObject((defaultJSONParser.lexer.features & Feature.OrderedField.mask) != 0));
        } else if (this.beanInfo.defaultConstructor == null && this.beanInfo.factoryMethod == null) {
            return null;
        } else {
            if (this.beanInfo.factoryMethod != null && this.beanInfo.defaultConstructorParameterSize > 0) {
                return null;
            }
            try {
                Constructor<?> constructor = this.beanInfo.defaultConstructor;
                if (this.beanInfo.defaultConstructorParameterSize != 0) {
                    obj = constructor.newInstance(defaultJSONParser.contex.object);
                } else if (constructor != null) {
                    obj = constructor.newInstance(new Object[0]);
                } else {
                    obj = this.beanInfo.factoryMethod.invoke(null, new Object[0]);
                }
                if (!(defaultJSONParser == null || (defaultJSONParser.lexer.features & Feature.InitStringFieldAsEmpty.mask) == 0)) {
                    FieldInfo[] fieldInfoArr = this.beanInfo.fields;
                    for (FieldInfo fieldInfo : fieldInfoArr) {
                        if (fieldInfo.fieldClass == String.class) {
                            fieldInfo.set(obj, "");
                        }
                    }
                }
                return obj;
            } catch (Exception e) {
                throw new JSONException("create instance error, class " + this.clazz.getName(), e);
            }
        }
    }

    @Override // ohos.utils.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) deserialze(defaultJSONParser, type, obj, null);
    }

    private <T> T deserialzeArrayMapping(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        Enum r8;
        char c6;
        char c7;
        char c8;
        char c9;
        char c10;
        char c11;
        String str;
        char c12;
        char c13;
        char c14;
        char c15;
        char c16;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        T t = (T) createInstance(defaultJSONParser, type);
        int length = this.sortedFieldDeserializers.length;
        int i = 0;
        while (i < length) {
            boolean z = i == length + -1 ? true : true;
            FieldDeserializer fieldDeserializer = this.sortedFieldDeserializers[i];
            FieldInfo fieldInfo = fieldDeserializer.fieldInfo;
            Class<?> cls = fieldInfo.fieldClass;
            try {
                if (cls == Integer.TYPE) {
                    int scanLongValue = (int) jSONLexer.scanLongValue();
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.setInt(t, scanLongValue);
                    } else {
                        fieldDeserializer.setValue(t, new Integer(scanLongValue));
                    }
                    if (jSONLexer.ch == ',') {
                        int i2 = jSONLexer.bp + 1;
                        jSONLexer.bp = i2;
                        if (i2 >= jSONLexer.len) {
                            c16 = JSONLexer.EOI;
                        } else {
                            c16 = jSONLexer.text.charAt(i2);
                        }
                        jSONLexer.ch = c16;
                        jSONLexer.token = 16;
                    } else if (jSONLexer.ch == ']') {
                        int i3 = jSONLexer.bp + 1;
                        jSONLexer.bp = i3;
                        if (i3 >= jSONLexer.len) {
                            c15 = JSONLexer.EOI;
                        } else {
                            c15 = jSONLexer.text.charAt(i3);
                        }
                        jSONLexer.ch = c15;
                        jSONLexer.token = 15;
                    } else {
                        jSONLexer.nextToken();
                    }
                } else if (cls == String.class) {
                    if (jSONLexer.ch == '\"') {
                        str = jSONLexer.scanStringValue('\"');
                    } else if (jSONLexer.ch != 'n' || !jSONLexer.text.startsWith("null", jSONLexer.bp)) {
                        throw new JSONException("not match string. feild : " + obj);
                    } else {
                        jSONLexer.bp += 4;
                        int i4 = jSONLexer.bp;
                        if (jSONLexer.bp >= jSONLexer.len) {
                            c14 = JSONLexer.EOI;
                        } else {
                            c14 = jSONLexer.text.charAt(i4);
                        }
                        jSONLexer.ch = c14;
                        str = null;
                    }
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.set(t, str);
                    } else {
                        fieldDeserializer.setValue(t, str);
                    }
                    if (jSONLexer.ch == ',') {
                        int i5 = jSONLexer.bp + 1;
                        jSONLexer.bp = i5;
                        if (i5 >= jSONLexer.len) {
                            c13 = JSONLexer.EOI;
                        } else {
                            c13 = jSONLexer.text.charAt(i5);
                        }
                        jSONLexer.ch = c13;
                        jSONLexer.token = 16;
                    } else if (jSONLexer.ch == ']') {
                        int i6 = jSONLexer.bp + 1;
                        jSONLexer.bp = i6;
                        if (i6 >= jSONLexer.len) {
                            c12 = JSONLexer.EOI;
                        } else {
                            c12 = jSONLexer.text.charAt(i6);
                        }
                        jSONLexer.ch = c12;
                        jSONLexer.token = 15;
                    } else {
                        jSONLexer.nextToken();
                    }
                } else {
                    if (cls == Long.TYPE) {
                        long scanLongValue2 = jSONLexer.scanLongValue();
                        if (fieldInfo.fieldAccess) {
                            fieldInfo.field.setLong(t, scanLongValue2);
                        } else {
                            fieldDeserializer.setValue(t, new Long(scanLongValue2));
                        }
                        if (jSONLexer.ch == ',') {
                            int i7 = jSONLexer.bp + 1;
                            jSONLexer.bp = i7;
                            if (i7 >= jSONLexer.len) {
                                c11 = JSONLexer.EOI;
                            } else {
                                c11 = jSONLexer.text.charAt(i7);
                            }
                            jSONLexer.ch = c11;
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i8 = jSONLexer.bp + 1;
                            jSONLexer.bp = i8;
                            if (i8 >= jSONLexer.len) {
                                c10 = JSONLexer.EOI;
                            } else {
                                c10 = jSONLexer.text.charAt(i8);
                            }
                            jSONLexer.ch = c10;
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else if (cls == Boolean.TYPE) {
                        boolean scanBoolean = jSONLexer.scanBoolean();
                        if (fieldInfo.fieldAccess) {
                            fieldInfo.field.setBoolean(t, scanBoolean);
                        } else {
                            fieldDeserializer.setValue(t, Boolean.valueOf(scanBoolean));
                        }
                        if (jSONLexer.ch == ',') {
                            int i9 = jSONLexer.bp + 1;
                            jSONLexer.bp = i9;
                            if (i9 >= jSONLexer.len) {
                                c9 = JSONLexer.EOI;
                            } else {
                                c9 = jSONLexer.text.charAt(i9);
                            }
                            jSONLexer.ch = c9;
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i10 = jSONLexer.bp + 1;
                            jSONLexer.bp = i10;
                            if (i10 >= jSONLexer.len) {
                                c8 = JSONLexer.EOI;
                            } else {
                                c8 = jSONLexer.text.charAt(i10);
                            }
                            jSONLexer.ch = c8;
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else if (cls.isEnum()) {
                        char c17 = jSONLexer.ch;
                        if (c17 == '\"') {
                            String scanSymbol = jSONLexer.scanSymbol(defaultJSONParser.symbolTable);
                            if (scanSymbol == null) {
                                r8 = null;
                            } else {
                                r8 = Enum.valueOf(cls, scanSymbol);
                            }
                        } else if (c17 < '0' || c17 > '9') {
                            throw new JSONException("illegal enum." + jSONLexer.info());
                        } else {
                            r8 = ((EnumDeserializer) ((DefaultFieldDeserializer) fieldDeserializer).getFieldValueDeserilizer(defaultJSONParser.config)).ordinalEnums[(int) jSONLexer.scanLongValue()];
                        }
                        fieldDeserializer.setValue(t, r8);
                        if (jSONLexer.ch == ',') {
                            int i11 = jSONLexer.bp + 1;
                            jSONLexer.bp = i11;
                            if (i11 >= jSONLexer.len) {
                                c7 = JSONLexer.EOI;
                            } else {
                                c7 = jSONLexer.text.charAt(i11);
                            }
                            jSONLexer.ch = c7;
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i12 = jSONLexer.bp + 1;
                            jSONLexer.bp = i12;
                            if (i12 >= jSONLexer.len) {
                                c6 = JSONLexer.EOI;
                            } else {
                                c6 = jSONLexer.text.charAt(i12);
                            }
                            jSONLexer.ch = c6;
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else if (cls == Date.class && jSONLexer.ch == '1') {
                        fieldDeserializer.setValue(t, new Date(jSONLexer.scanLongValue()));
                        if (jSONLexer.ch == ',') {
                            int i13 = jSONLexer.bp + 1;
                            jSONLexer.bp = i13;
                            if (i13 >= jSONLexer.len) {
                                c5 = JSONLexer.EOI;
                            } else {
                                c5 = jSONLexer.text.charAt(i13);
                            }
                            jSONLexer.ch = c5;
                            jSONLexer.token = 16;
                        } else if (jSONLexer.ch == ']') {
                            int i14 = jSONLexer.bp + 1;
                            jSONLexer.bp = i14;
                            if (i14 >= jSONLexer.len) {
                                c4 = JSONLexer.EOI;
                            } else {
                                c4 = jSONLexer.text.charAt(i14);
                            }
                            jSONLexer.ch = c4;
                            jSONLexer.token = 15;
                        } else {
                            jSONLexer.nextToken();
                        }
                    } else {
                        if (jSONLexer.ch == '[') {
                            int i15 = jSONLexer.bp + 1;
                            jSONLexer.bp = i15;
                            if (i15 >= jSONLexer.len) {
                                c3 = JSONLexer.EOI;
                            } else {
                                c3 = jSONLexer.text.charAt(i15);
                            }
                            jSONLexer.ch = c3;
                            jSONLexer.token = 14;
                        } else if (jSONLexer.ch == '{') {
                            int i16 = jSONLexer.bp + 1;
                            jSONLexer.bp = i16;
                            if (i16 >= jSONLexer.len) {
                                c2 = JSONLexer.EOI;
                            } else {
                                c2 = jSONLexer.text.charAt(i16);
                            }
                            jSONLexer.ch = c2;
                            jSONLexer.token = 12;
                        } else {
                            jSONLexer.nextToken();
                        }
                        fieldDeserializer.parseField(defaultJSONParser, t, fieldInfo.fieldType, null);
                        if (z) {
                            if (jSONLexer.token != 15) {
                                throw new JSONException("syntax error");
                            }
                        } else if (z && jSONLexer.token != 16) {
                            throw new JSONException("syntax error");
                        }
                    }
                    i++;
                }
                i++;
            } catch (IllegalAccessException e) {
                throw new JSONException("set " + fieldInfo.name + "error", e);
            }
        }
        if (jSONLexer.ch == ',') {
            int i17 = jSONLexer.bp + 1;
            jSONLexer.bp = i17;
            if (i17 >= jSONLexer.len) {
                c = JSONLexer.EOI;
            } else {
                c = jSONLexer.text.charAt(i17);
            }
            jSONLexer.ch = c;
            jSONLexer.token = 16;
        } else {
            jSONLexer.nextToken();
        }
        return t;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:222:0x02d0, code lost:
        if (r1 == 16) goto L_0x02d2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:228:0x02e2, code lost:
        r10.nextTokenWithChar(':');
        r0 = r10.token;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:229:0x02e8, code lost:
        if (r0 != 4) goto L_0x036a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:230:0x02ea, code lost:
        r0 = r10.stringVal();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:231:0x02f4, code lost:
        if ("@".equals(r0) == false) goto L_0x02fa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x02f6, code lost:
        r6 = (T) r2.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:0x0300, code lost:
        if (ohos.com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_PARENT.equals(r0) == false) goto L_0x0317;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:235:0x0302, code lost:
        r1 = r2.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:236:0x0306, code lost:
        if (r1.object == null) goto L_0x030b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x0308, code lost:
        r6 = (T) r1.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:238:0x030b, code lost:
        r35.addResolveTask(new ohos.utils.fastjson.parser.DefaultJSONParser.ResolveTask(r1, r0));
        r35.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x031d, code lost:
        if ("$".equals(r0) == false) goto L_0x033a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:241:0x031f, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:243:0x0322, code lost:
        if (r1.parent == null) goto L_0x0327;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:244:0x0324, code lost:
        r1 = r1.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:246:0x0329, code lost:
        if (r1.object == null) goto L_0x032e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:247:0x032b, code lost:
        r6 = (T) r1.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:248:0x032e, code lost:
        r35.addResolveTask(new ohos.utils.fastjson.parser.DefaultJSONParser.ResolveTask(r1, r0));
        r35.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:249:0x033a, code lost:
        r35.addResolveTask(new ohos.utils.fastjson.parser.DefaultJSONParser.ResolveTask(r2, r0));
        r35.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:250:0x0345, code lost:
        r10.nextToken(13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:251:0x034c, code lost:
        if (r10.token != 13) goto L_0x0360;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:252:0x034e, code lost:
        r10.nextToken(16);
        r35.setContext(r2, r6, r37);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:253:0x0358, code lost:
        if (r20 == null) goto L_0x035c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:254:0x035a, code lost:
        r20.object = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:0x035c, code lost:
        r35.setContext(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:256:0x035f, code lost:
        return (T) r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:257:0x0360, code lost:
        r1 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:260:0x0369, code lost:
        throw new ohos.utils.fastjson.JSONException("illegal ref");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:262:0x0386, code lost:
        throw new ohos.utils.fastjson.JSONException("illegal ref, " + ohos.utils.fastjson.parser.JSONToken.name(r0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:281:0x03c5, code lost:
        r12 = r1;
        r13 = (T) r6;
        r1 = r21;
        r19 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:284:0x03e4, code lost:
        if (r4 != null) goto L_0x040e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:285:0x03e6, code lost:
        r12 = r35.config.checkAutoType(r2, r34.clazz, r10.features);
        r0 = ohos.utils.fastjson.util.TypeUtils.getClass(r36);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:286:0x03f4, code lost:
        if (r0 == null) goto L_0x0407;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:287:0x03f6, code lost:
        if (r12 == null) goto L_0x03ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:289:0x03fc, code lost:
        if (r0.isAssignableFrom(r12) == false) goto L_0x03ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:292:0x0406, code lost:
        throw new ohos.utils.fastjson.JSONException("type not match");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:293:0x0407, code lost:
        r4 = r35.config.getDeserializer(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:294:0x040e, code lost:
        r12 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:296:0x0411, code lost:
        if ((r4 instanceof ohos.utils.fastjson.parser.JavaBeanDeserializer) == false) goto L_0x0426;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:297:0x0413, code lost:
        r4 = (ohos.utils.fastjson.parser.JavaBeanDeserializer) r4;
        r0 = (T) r4.deserialze(r35, r12, r37, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:298:0x041a, code lost:
        if (r3 == null) goto L_0x042a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:299:0x041c, code lost:
        r3 = r4.getFieldDeserializer(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:300:0x0420, code lost:
        if (r3 == null) goto L_0x042a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:301:0x0422, code lost:
        r3.setValue(r0, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:302:0x0426, code lost:
        r0 = (T) r4.deserialze(r35, r12, r37);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:303:0x042a, code lost:
        if (r1 == null) goto L_0x042e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:304:0x042c, code lost:
        r1.object = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:305:0x042e, code lost:
        r35.setContext(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:306:0x0431, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:309:0x043a, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:495:0x0700, code lost:
        throw new ohos.utils.fastjson.JSONException("syntax error, unexpect token " + ohos.utils.fastjson.parser.JSONToken.name(r10.token));
     */
    /* JADX WARNING: Removed duplicated region for block: B:215:0x02b3 A[SYNTHETIC, Splitter:B:215:0x02b3] */
    /* JADX WARNING: Removed duplicated region for block: B:274:0x03a2 A[Catch:{ all -> 0x043a }] */
    /* JADX WARNING: Removed duplicated region for block: B:313:0x0442  */
    /* JADX WARNING: Removed duplicated region for block: B:315:0x044b A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:329:0x047c  */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x0599  */
    /* JADX WARNING: Removed duplicated region for block: B:427:0x05d1  */
    /* JADX WARNING: Removed duplicated region for block: B:428:0x05d6  */
    /* JADX WARNING: Removed duplicated region for block: B:503:0x0713  */
    /* JADX WARNING: Removed duplicated region for block: B:514:0x0432 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private <T> T deserialze(ohos.utils.fastjson.parser.DefaultJSONParser r35, java.lang.reflect.Type r36, java.lang.Object r37, java.lang.Object r38) {
        /*
        // Method dump skipped, instructions count: 1822
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.fastjson.parser.JavaBeanDeserializer.deserialze(ohos.utils.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public FieldDeserializer getFieldDeserializerByHash(long j) {
        int i = 0;
        while (true) {
            FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
            if (i >= fieldDeserializerArr.length) {
                return null;
            }
            FieldDeserializer fieldDeserializer = fieldDeserializerArr[i];
            if (fieldDeserializer.fieldInfo.nameHashCode == j) {
                return fieldDeserializer;
            }
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public FieldDeserializer getFieldDeserializer(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        if (this.beanInfo.ordered) {
            while (true) {
                FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
                if (i >= fieldDeserializerArr.length) {
                    return null;
                }
                FieldDeserializer fieldDeserializer = fieldDeserializerArr[i];
                if (fieldDeserializer.fieldInfo.name.equalsIgnoreCase(str)) {
                    return fieldDeserializer;
                }
                i++;
            }
        } else {
            int length = this.sortedFieldDeserializers.length - 1;
            while (i <= length) {
                int i2 = (i + length) >>> 1;
                int compareTo = this.sortedFieldDeserializers[i2].fieldInfo.name.compareTo(str);
                if (compareTo < 0) {
                    i = i2 + 1;
                } else if (compareTo <= 0) {
                    return this.sortedFieldDeserializers[i2];
                } else {
                    length = i2 - 1;
                }
            }
            Map<String, FieldDeserializer> map = this.alterNameFieldDeserializers;
            if (map != null) {
                return map.get(str);
            }
            return null;
        }
    }

    private boolean parseField(DefaultJSONParser defaultJSONParser, String str, Object obj, Type type, Map<String, Object> map) {
        boolean z;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        FieldDeserializer fieldDeserializer = getFieldDeserializer(str);
        if (fieldDeserializer == null) {
            long fnv_64_lower = TypeUtils.fnv_64_lower(str);
            if (this.smartMatchHashArray == null) {
                long[] jArr = new long[this.sortedFieldDeserializers.length];
                int i = 0;
                while (true) {
                    FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
                    if (i >= fieldDeserializerArr.length) {
                        break;
                    }
                    jArr[i] = TypeUtils.fnv_64_lower(fieldDeserializerArr[i].fieldInfo.name);
                    i++;
                }
                Arrays.sort(jArr);
                this.smartMatchHashArray = jArr;
            }
            int binarySearch = Arrays.binarySearch(this.smartMatchHashArray, fnv_64_lower);
            if (binarySearch < 0) {
                z = str.startsWith("is");
                if (z) {
                    binarySearch = Arrays.binarySearch(this.smartMatchHashArray, TypeUtils.fnv_64_lower(str.substring(2)));
                }
            } else {
                z = false;
            }
            if (binarySearch >= 0) {
                if (this.smartMatchHashArrayMapping == null) {
                    int[] iArr = new int[this.smartMatchHashArray.length];
                    Arrays.fill(iArr, -1);
                    int i2 = 0;
                    while (true) {
                        FieldDeserializer[] fieldDeserializerArr2 = this.sortedFieldDeserializers;
                        if (i2 >= fieldDeserializerArr2.length) {
                            break;
                        }
                        int binarySearch2 = Arrays.binarySearch(this.smartMatchHashArray, TypeUtils.fnv_64_lower(fieldDeserializerArr2[i2].fieldInfo.name));
                        if (binarySearch2 >= 0) {
                            iArr[binarySearch2] = i2;
                        }
                        i2++;
                    }
                    this.smartMatchHashArrayMapping = iArr;
                }
                int i3 = this.smartMatchHashArrayMapping[binarySearch];
                if (i3 != -1) {
                    fieldDeserializer = this.sortedFieldDeserializers[i3];
                    Class<?> cls = fieldDeserializer.fieldInfo.fieldClass;
                    if (!(!z || cls == Boolean.TYPE || cls == Boolean.class)) {
                        fieldDeserializer = null;
                    }
                }
            }
        }
        int i4 = Feature.SupportNonPublicField.mask;
        if (fieldDeserializer == null && !((defaultJSONParser.lexer.features & i4) == 0 && (i4 & this.beanInfo.parserFeatures) == 0)) {
            if (this.extraFieldDeserializers == null) {
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(1, 0.75f, 1);
                Class<?> cls2 = this.clazz;
                while (cls2 != null && cls2 != Object.class) {
                    Field[] declaredFields = cls2.getDeclaredFields();
                    for (Field field : declaredFields) {
                        String name = field.getName();
                        if (getFieldDeserializer(name) == null) {
                            int modifiers = field.getModifiers();
                            if ((modifiers & 16) == 0 && (modifiers & 8) == 0) {
                                concurrentHashMap.put(name, field);
                            }
                        }
                    }
                    cls2 = cls2.getSuperclass();
                }
                this.extraFieldDeserializers = concurrentHashMap;
            }
            Object obj2 = this.extraFieldDeserializers.get(str);
            if (obj2 != null) {
                if (obj2 instanceof FieldDeserializer) {
                    fieldDeserializer = (FieldDeserializer) obj2;
                } else {
                    Field field2 = (Field) obj2;
                    field2.setAccessible(true);
                    fieldDeserializer = new DefaultFieldDeserializer(defaultJSONParser.config, this.clazz, new FieldInfo(str, field2.getDeclaringClass(), field2.getType(), field2.getGenericType(), field2, 0, 0));
                    this.extraFieldDeserializers.put(str, fieldDeserializer);
                }
            }
        }
        if (fieldDeserializer == null) {
            parseExtra(defaultJSONParser, obj, str);
            return false;
        }
        jSONLexer.nextTokenWithChar(':');
        fieldDeserializer.parseField(defaultJSONParser, obj, type, map);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void parseExtra(DefaultJSONParser defaultJSONParser, Object obj, String str) {
        Object obj2;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if ((defaultJSONParser.lexer.features & Feature.IgnoreNotMatch.mask) != 0) {
            jSONLexer.nextTokenWithChar(':');
            Type type = null;
            List<ExtraTypeProvider> list = defaultJSONParser.extraTypeProviders;
            if (list != null) {
                for (ExtraTypeProvider extraTypeProvider : list) {
                    type = extraTypeProvider.getExtraType(obj, str);
                }
            }
            if (type == null) {
                obj2 = defaultJSONParser.parse();
            } else {
                obj2 = defaultJSONParser.parseObject(type);
            }
            if (obj instanceof ExtraProcessable) {
                ((ExtraProcessable) obj).processExtra(str, obj2);
                return;
            }
            List<ExtraProcessor> list2 = defaultJSONParser.extraProcessors;
            if (list2 != null) {
                for (ExtraProcessor extraProcessor : list2) {
                    extraProcessor.processExtra(obj, str, obj2);
                }
                return;
            }
            return;
        }
        throw new JSONException("setter not found, class " + this.clazz.getName() + ", property " + str);
    }

    public Object createInstance(Map<String, Object> map, ParserConfig parserConfig) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object obj;
        double d;
        float f;
        if (this.beanInfo.creatorConstructor == null) {
            Object createInstance = createInstance((DefaultJSONParser) null, this.clazz);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                FieldDeserializer fieldDeserializer = getFieldDeserializer(entry.getKey());
                if (fieldDeserializer != null) {
                    Object value = entry.getValue();
                    Method method = fieldDeserializer.fieldInfo.method;
                    if (method != null) {
                        method.invoke(createInstance, TypeUtils.cast(value, method.getGenericParameterTypes()[0], parserConfig));
                    } else {
                        Field field = fieldDeserializer.fieldInfo.field;
                        Type type = fieldDeserializer.fieldInfo.fieldType;
                        if (type == Boolean.TYPE) {
                            if (value == Boolean.FALSE) {
                                field.setBoolean(createInstance, false);
                            } else if (value == Boolean.TRUE) {
                                field.setBoolean(createInstance, true);
                            }
                        } else if (type == Integer.TYPE) {
                            if (value instanceof Number) {
                                field.setInt(createInstance, ((Number) value).intValue());
                            }
                        } else if (type == Long.TYPE) {
                            if (value instanceof Number) {
                                field.setLong(createInstance, ((Number) value).longValue());
                            }
                        } else if (type == Float.TYPE) {
                            if (value instanceof Number) {
                                field.setFloat(createInstance, ((Number) value).floatValue());
                            } else if (value instanceof String) {
                                String str = (String) value;
                                if (str.length() <= 10) {
                                    f = TypeUtils.parseFloat(str);
                                } else {
                                    f = Float.parseFloat(str);
                                }
                                field.setFloat(createInstance, f);
                            }
                        } else if (type == Double.TYPE) {
                            if (value instanceof Number) {
                                field.setDouble(createInstance, ((Number) value).doubleValue());
                            } else if (value instanceof String) {
                                String str2 = (String) value;
                                if (str2.length() <= 10) {
                                    d = TypeUtils.parseDouble(str2);
                                } else {
                                    d = Double.parseDouble(str2);
                                }
                                field.setDouble(createInstance, d);
                            }
                        } else if (value != null && type == value.getClass()) {
                            field.set(createInstance, value);
                        }
                        String str3 = fieldDeserializer.fieldInfo.format;
                        if (str3 == null || type != Date.class || !(value instanceof String)) {
                            obj = type instanceof ParameterizedType ? TypeUtils.cast(value, (ParameterizedType) type, parserConfig) : TypeUtils.cast(value, type, parserConfig);
                        } else {
                            try {
                                obj = new SimpleDateFormat(str3).parse((String) value);
                            } catch (ParseException unused) {
                                obj = null;
                            }
                        }
                        field.set(createInstance, obj);
                    }
                }
            }
            return createInstance;
        }
        FieldInfo[] fieldInfoArr = this.beanInfo.fields;
        int length = fieldInfoArr.length;
        Object[] objArr = new Object[length];
        for (int i = 0; i < length; i++) {
            FieldInfo fieldInfo = fieldInfoArr[i];
            Object obj2 = map.get(fieldInfo.name);
            if (obj2 == null) {
                obj2 = TypeUtils.defaultValue(fieldInfo.fieldClass);
            }
            objArr[i] = obj2;
        }
        if (this.beanInfo.creatorConstructor == null) {
            return null;
        }
        try {
            return this.beanInfo.creatorConstructor.newInstance(objArr);
        } catch (Exception e) {
            throw new JSONException("create instance error, " + this.beanInfo.creatorConstructor.toGenericString(), e);
        }
    }

    /* access modifiers changed from: protected */
    public JavaBeanDeserializer getSeeAlso(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, String str) {
        if (javaBeanInfo.jsonType == null) {
            return null;
        }
        for (Class<?> cls : javaBeanInfo.jsonType.seeAlso()) {
            ObjectDeserializer deserializer = parserConfig.getDeserializer(cls);
            if (deserializer instanceof JavaBeanDeserializer) {
                JavaBeanDeserializer javaBeanDeserializer = (JavaBeanDeserializer) deserializer;
                JavaBeanInfo javaBeanInfo2 = javaBeanDeserializer.beanInfo;
                if (javaBeanInfo2.typeName.equals(str)) {
                    return javaBeanDeserializer;
                }
                JavaBeanDeserializer seeAlso = getSeeAlso(parserConfig, javaBeanInfo2, str);
                if (seeAlso != null) {
                    return seeAlso;
                }
            }
        }
        return null;
    }
}
