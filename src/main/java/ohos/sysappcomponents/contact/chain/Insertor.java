package ohos.sysappcomponents.contact.chain;

import java.util.ArrayList;
import java.util.stream.Stream;
import ohos.aafwk.ability.DataAbilityOperation;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.sysappcomponents.contact.Attribute;
import ohos.sysappcomponents.contact.ContactAttributes;
import ohos.sysappcomponents.contact.entity.Contact;

public abstract class Insertor {
    private Insertor mNextInsertor;

    public enum OperationType {
        INSERT,
        UPDATE,
        DELETE,
        ASSERT
    }

    public abstract void fillOperation(Contact contact, ArrayList<DataAbilityOperation> arrayList, OperationType operationType);

    public final void setNextInsertor(Insertor insertor) {
        this.mNextInsertor = insertor;
    }

    /* access modifiers changed from: protected */
    public final void fillNextItem(Contact contact, ArrayList<DataAbilityOperation> arrayList, OperationType operationType) {
        Insertor insertor = this.mNextInsertor;
        if (insertor != null) {
            insertor.fillOperation(contact, arrayList, operationType);
        }
    }

    /* access modifiers changed from: protected */
    public final void fillStringContent(DataAbilityOperation.Builder builder, String str, String str2) {
        if (!Stream.of(str, str2, builder).anyMatch($$Lambda$Insertor$wLIh0GiBW9398cTP8uaTH8KoGwo.INSTANCE)) {
            ValuesBucket valuesBucket = new ValuesBucket();
            valuesBucket.putString(str, str2);
            builder.withValuesBucket(valuesBucket);
        }
    }

    /* access modifiers changed from: protected */
    public final void fillIntegerContent(DataAbilityOperation.Builder builder, String str, int i) {
        if (!Stream.of(str, builder).anyMatch($$Lambda$Insertor$wLIh0GiBW9398cTP8uaTH8KoGwo.INSTANCE)) {
            ValuesBucket valuesBucket = new ValuesBucket();
            valuesBucket.putInteger(str, Integer.valueOf(i));
            builder.withValuesBucket(valuesBucket);
        }
    }

    /* access modifiers changed from: protected */
    public final void fillBlobContent(DataAbilityOperation.Builder builder, String str, byte[] bArr) {
        if (!Stream.of(str, builder).anyMatch($$Lambda$Insertor$wLIh0GiBW9398cTP8uaTH8KoGwo.INSTANCE)) {
            ValuesBucket valuesBucket = new ValuesBucket();
            valuesBucket.putByteArray(str, bArr);
            builder.withValuesBucket(valuesBucket);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).isEmpty();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final DataAbilityOperation.Builder getOriginalDataBuilder(String str) {
        DataAbilityOperation.Builder newInsertBuilder = DataAbilityOperation.newInsertBuilder(Attribute.Data.CONTENT_URI);
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putInteger(Attribute.Data.RAW_CONTACT_ID, 0);
        newInsertBuilder.withValueBackReferences(valuesBucket);
        fillStringContent(newInsertBuilder, "mimetype", str);
        return newInsertBuilder;
    }

    /* access modifiers changed from: protected */
    public final DataAbilityOperation.Builder getUpdatePredicatesBuilder(int i) {
        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates("_id = ?");
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf(i));
        dataAbilityPredicates.setWhereArgs(arrayList);
        return DataAbilityOperation.newUpdateBuilder(Attribute.Data.CONTENT_URI).withPredicates(dataAbilityPredicates);
    }

    /* access modifiers changed from: protected */
    public final DataAbilityOperation.Builder getPredicatesBuilder(String str, OperationType operationType, int i) {
        int i2 = AnonymousClass1.$SwitchMap$ohos$sysappcomponents$contact$chain$Insertor$OperationType[operationType.ordinal()];
        if (i2 == 1) {
            return getUpdatePredicatesBuilder(i);
        }
        if (i2 != 2) {
            return null;
        }
        return getOriginalDataBuilder(str);
    }

    public static final Insertor initChain() {
        return createChain(getInsertContactAttributes());
    }

    private static ContactAttributes getInsertContactAttributes() {
        ContactAttributes contactAttributes = new ContactAttributes();
        contactAttributes.add(ContactAttributes.Attribute.ATTR_NAME);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_NICKNAME);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_PHONE);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_WEBSITE);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_IDENTITY);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_EMAIL);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_CONTACT_EVENT);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_IM);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_ORGANIZATION);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_POSTAL_ADDRESS);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_RELATION);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_SIP_ADDRESS);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_NOTE);
        contactAttributes.add(ContactAttributes.Attribute.ATTR_GROUP_MEMBERSHIP);
        return contactAttributes;
    }

    private static ContactAttributes getAllContactAttributes() {
        ContactAttributes insertContactAttributes = getInsertContactAttributes();
        insertContactAttributes.add(ContactAttributes.Attribute.ATTR_HICALL_DEVICE);
        return insertContactAttributes;
    }

    public static final Insertor createChain(ContactAttributes contactAttributes) {
        if (contactAttributes == null || contactAttributes.getAttributes().size() == 0) {
            contactAttributes = getAllContactAttributes();
        }
        boolean z = true;
        Insertor insertor = null;
        Insertor insertor2 = null;
        for (ContactAttributes.Attribute attribute : contactAttributes.getAttributes()) {
            Insertor createChainByAttribute = createChainByAttribute(attribute);
            if (createChainByAttribute != null) {
                if (z) {
                    z = false;
                    insertor = createChainByAttribute;
                } else {
                    insertor2.setNextInsertor(createChainByAttribute);
                }
                insertor2 = createChainByAttribute;
            }
        }
        return insertor;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.sysappcomponents.contact.chain.Insertor$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$sysappcomponents$contact$chain$Insertor$OperationType = new int[OperationType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(40:0|(2:1|2)|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|(2:37|38)|39|41|42|(3:43|44|46)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(41:0|(2:1|2)|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|41|42|(3:43|44|46)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(43:0|(2:1|2)|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|41|42|43|44|46) */
        /* JADX WARNING: Can't wrap try/catch for region: R(44:0|1|2|3|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|41|42|43|44|46) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0035 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x007a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0086 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0092 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x009e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00b6 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00c2 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00ce */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00ed */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x002a */
        static {
            /*
            // Method dump skipped, instructions count: 248
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.chain.Insertor.AnonymousClass1.<clinit>():void");
        }
    }

    private static final Insertor createChainByAttribute(ContactAttributes.Attribute attribute) {
        switch (attribute) {
            case ATTR_EMAIL:
                return new EmailInsertor();
            case ATTR_IM:
                return new ImAddressInsertor();
            case ATTR_NICKNAME:
                return new NicknameInsertor();
            case ATTR_ORGANIZATION:
                return new OrganizationInsertor();
            case ATTR_PHONE:
                return new PhoneNumberInsertor();
            case ATTR_SIP_ADDRESS:
                return new SipAddressInsertor();
            case ATTR_NAME:
                return new NameInsertor();
            case ATTR_POSTAL_ADDRESS:
                return new PostalAddressInsertor();
            case ATTR_IDENTITY:
                return new IdentityInsertor();
            case ATTR_GROUP_MEMBERSHIP:
                return new GroupInsertor();
            case ATTR_NOTE:
                return new NoteInsertor();
            case ATTR_CONTACT_EVENT:
                return new EventInsertor();
            case ATTR_WEBSITE:
                return new WebsiteInsertor();
            case ATTR_RELATION:
                return new RelationInsertor();
            case ATTR_HICALL_DEVICE:
                return new HiCallDeviceChain();
            default:
                return null;
        }
    }
}
