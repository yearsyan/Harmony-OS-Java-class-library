package ohos.sysappcomponents.contact;

import ohos.app.Context;
import ohos.data.resultset.ResultSet;
import ohos.sysappcomponents.contact.collection.Collection;
import ohos.sysappcomponents.contact.collection.DataContactsCollection;
import ohos.sysappcomponents.contact.collection.PhoneLookupCollection;
import ohos.sysappcomponents.contact.entity.Contact;

public abstract class ContactsCollection extends Collection {
    protected ContactAttributes mAttributes;
    protected Context mContext;

    public enum Type {
        PHONE_LOOKUP,
        DATA_CONTACT
    }

    public abstract Contact next();

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.sysappcomponents.contact.ContactsCollection$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$sysappcomponents$contact$ContactsCollection$Type = new int[Type.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                ohos.sysappcomponents.contact.ContactsCollection$Type[] r0 = ohos.sysappcomponents.contact.ContactsCollection.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.sysappcomponents.contact.ContactsCollection.AnonymousClass1.$SwitchMap$ohos$sysappcomponents$contact$ContactsCollection$Type = r0
                int[] r0 = ohos.sysappcomponents.contact.ContactsCollection.AnonymousClass1.$SwitchMap$ohos$sysappcomponents$contact$ContactsCollection$Type     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.sysappcomponents.contact.ContactsCollection$Type r1 = ohos.sysappcomponents.contact.ContactsCollection.Type.PHONE_LOOKUP     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.sysappcomponents.contact.ContactsCollection.AnonymousClass1.$SwitchMap$ohos$sysappcomponents$contact$ContactsCollection$Type     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.sysappcomponents.contact.ContactsCollection$Type r1 = ohos.sysappcomponents.contact.ContactsCollection.Type.DATA_CONTACT     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactsCollection.AnonymousClass1.<clinit>():void");
        }
    }

    public static ContactsCollection createContactsCollection(ResultSet resultSet, ContactAttributes contactAttributes, Context context, Type type) {
        int i = AnonymousClass1.$SwitchMap$ohos$sysappcomponents$contact$ContactsCollection$Type[type.ordinal()];
        if (i == 1) {
            return new PhoneLookupCollection(resultSet, contactAttributes, context);
        }
        if (i == 2) {
            return new DataContactsCollection(resultSet, contactAttributes, context);
        }
        LogUtil.error(ContactsCollection.class.getSimpleName(), "Wrong type for construct ContactsCollection.");
        return null;
    }
}
