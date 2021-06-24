package ohos.sysappcomponents.contact;

import java.util.ArrayList;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityResult;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.sysappcomponents.contact.Attribute;
import ohos.sysappcomponents.contact.ContactsCollection;
import ohos.sysappcomponents.contact.chain.Insertor;
import ohos.sysappcomponents.contact.entity.Contact;
import ohos.sysappcomponents.contact.entity.Holder;
import ohos.telephony.TelephoneNumberUtils;
import ohos.utils.net.Uri;

public class ContactDataHelper {
    private static final String DEFAULT_DIRECTORY = "1";
    private static final String EMPTY_STRING = "";
    private static final String FIELD_CONNECTOR = "=?";
    private static final String HOLDER = "directory";
    private static final long[] LOCAL_HOLDER_ID = {0, 1, Attribute.Holder.ENTERPRISE_DEFAULT, Attribute.Holder.ENTERPRISE_LOCAL_INVISIBLE};
    private static final String LOOKUP_PROFILE = "profile";
    private static final String NON_DEFAULT_DIRECTORY = "0";
    private static final String RAW_DEFAULT_DIRECTORY = "in_default_directory=?";
    private static final String RAW_RROFILE_FILTER = "contact_id>=?";
    private static final String TAG = ContactDataHelper.class.getSimpleName();
    private Context mContext;
    private Insertor mInsertor = Insertor.initChain();

    public boolean isMyCard(long j) {
        return j >= Attribute.Profile.MIN_ID;
    }

    public ContactDataHelper(Context context) {
        this.mContext = context;
    }

    public ContactsCollection queryContactsByPhoneNumber(String str, Holder holder, ContactAttributes contactAttributes) {
        String formatPhoneNumber = TelephoneNumberUtils.formatPhoneNumber(str);
        if (formatPhoneNumber == null || "".equals(formatPhoneNumber)) {
            return null;
        }
        return queryContactsByUri(Uri.appendEncodedPathToUri(Attribute.PhoneFinder.CONTENT_FILTER_URI, formatPhoneNumber).makeBuilder().appendDecodedQueryParam(HOLDER, String.valueOf(getHolderId(holder))).build(), null, contactAttributes, ContactsCollection.Type.PHONE_LOOKUP);
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.sysappcomponents.contact.ContactsCollection queryContactsByUri(ohos.utils.net.Uri r3, ohos.data.dataability.DataAbilityPredicates r4, ohos.sysappcomponents.contact.ContactAttributes r5, ohos.sysappcomponents.contact.ContactsCollection.Type r6) {
        /*
            r2 = this;
            ohos.app.Context r0 = r2.mContext
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            ohos.aafwk.ability.DataAbilityHelper r0 = ohos.aafwk.ability.DataAbilityHelper.creator(r0)
            ohos.data.resultset.ResultSet r3 = r0.query(r3, r1, r4)     // Catch:{ DataAbilityRemoteException -> 0x0027, IllegalArgumentException -> 0x001e }
            ohos.app.Context r4 = r2.mContext     // Catch:{ DataAbilityRemoteException -> 0x0027, IllegalArgumentException -> 0x001e }
            ohos.sysappcomponents.contact.ContactsCollection r3 = ohos.sysappcomponents.contact.ContactsCollection.createContactsCollection(r3, r5, r4, r6)     // Catch:{ DataAbilityRemoteException -> 0x0027, IllegalArgumentException -> 0x001e }
            java.lang.String r4 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ DataAbilityRemoteException -> 0x0028, IllegalArgumentException -> 0x001f }
            java.lang.String r5 = "ContactDataHelper fetchContactsByUri end"
            ohos.sysappcomponents.contact.LogUtil.info(r4, r5)     // Catch:{ DataAbilityRemoteException -> 0x0028, IllegalArgumentException -> 0x001f }
            goto L_0x002f
        L_0x001c:
            r3 = move-exception
            goto L_0x0033
        L_0x001e:
            r3 = r1
        L_0x001f:
            java.lang.String r4 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ all -> 0x001c }
            java.lang.String r5 = "fetchContactsByUri IllegalArgumentException error"
            ohos.sysappcomponents.contact.LogUtil.error(r4, r5)     // Catch:{ all -> 0x001c }
            goto L_0x002f
        L_0x0027:
            r3 = r1
        L_0x0028:
            java.lang.String r4 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ all -> 0x001c }
            java.lang.String r5 = "fetchContactsByUri error"
            ohos.sysappcomponents.contact.LogUtil.error(r4, r5)     // Catch:{ all -> 0x001c }
        L_0x002f:
            r2.clearEnvironment(r0, r1)
            return r3
        L_0x0033:
            r2.clearEnvironment(r0, r1)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactDataHelper.queryContactsByUri(ohos.utils.net.Uri, ohos.data.dataability.DataAbilityPredicates, ohos.sysappcomponents.contact.ContactAttributes, ohos.sysappcomponents.contact.ContactsCollection$Type):ohos.sysappcomponents.contact.ContactsCollection");
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0020 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.sysappcomponents.contact.HoldersCollection queryHolders() {
        /*
            r5 = this;
            ohos.app.Context r0 = r5.mContext
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            ohos.aafwk.ability.DataAbilityHelper r0 = ohos.aafwk.ability.DataAbilityHelper.creator(r0)
            ohos.utils.net.Uri r2 = ohos.sysappcomponents.contact.Attribute.Holder.CONTENT_URI     // Catch:{ DataAbilityRemoteException -> 0x001f }
            ohos.data.resultset.ResultSet r2 = r0.query(r2, r1, r1)     // Catch:{ DataAbilityRemoteException -> 0x001f }
            ohos.sysappcomponents.contact.HoldersCollection r3 = new ohos.sysappcomponents.contact.HoldersCollection     // Catch:{ DataAbilityRemoteException -> 0x001f }
            r3.<init>(r2)     // Catch:{ DataAbilityRemoteException -> 0x001f }
            java.lang.String r2 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ DataAbilityRemoteException -> 0x0020 }
            java.lang.String r4 = "ContactDataHelper fetchHoldersByUri end"
            ohos.sysappcomponents.contact.LogUtil.info(r2, r4)     // Catch:{ DataAbilityRemoteException -> 0x0020 }
            goto L_0x0027
        L_0x001d:
            r2 = move-exception
            goto L_0x002b
        L_0x001f:
            r3 = r1
        L_0x0020:
            java.lang.String r2 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ all -> 0x001d }
            java.lang.String r4 = "fetchHoldersByUri error"
            ohos.sysappcomponents.contact.LogUtil.error(r2, r4)     // Catch:{ all -> 0x001d }
        L_0x0027:
            r5.clearEnvironment(r0, r1)
            return r3
        L_0x002b:
            r5.clearEnvironment(r0, r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactDataHelper.queryHolders():ohos.sysappcomponents.contact.HoldersCollection");
    }

    public ContactsCollection queryContactsByEmail(String str, Holder holder, ContactAttributes contactAttributes) {
        if (str == null || this.mContext == null) {
            return null;
        }
        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates("raw_contact_id in (select raw_contact_id from data where mimetype_id = ? and data1 = ?)");
        ArrayList arrayList = new ArrayList();
        arrayList.add("1");
        arrayList.add(str);
        dataAbilityPredicates.setWhereArgs(arrayList);
        dataAbilityPredicates.setOrder("contact_id");
        return queryContactsByUri(Attribute.Data.CONTENT_URI.makeBuilder().appendDecodedQueryParam(HOLDER, String.valueOf(getHolderId(holder))).build(), dataAbilityPredicates, contactAttributes, ContactsCollection.Type.DATA_CONTACT);
    }

    public ContactsCollection queryContacts(Holder holder, ContactAttributes contactAttributes) {
        long holderId = getHolderId(holder);
        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates(RAW_DEFAULT_DIRECTORY);
        ArrayList arrayList = new ArrayList();
        if (Attribute.Holder.isRemoteHolderId(holderId)) {
            arrayList.add("0");
        } else {
            arrayList.add("1");
        }
        dataAbilityPredicates.setWhereArgs(arrayList);
        dataAbilityPredicates.setOrder("contact_id");
        return queryContactsByUri(Attribute.Data.CONTENT_URI.makeBuilder().appendDecodedQueryParam(HOLDER, String.valueOf(holderId)).build(), dataAbilityPredicates, contactAttributes, ContactsCollection.Type.DATA_CONTACT);
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:8|9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x003a, code lost:
        clearEnvironment(r1, null);
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0070, code lost:
        clearEnvironment(r1, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0073, code lost:
        throw r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0031, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        ohos.sysappcomponents.contact.LogUtil.error(ohos.sysappcomponents.contact.ContactDataHelper.TAG, "createContact error");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0033 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long addContact(ohos.sysappcomponents.contact.entity.Contact r8) {
        /*
        // Method dump skipped, instructions count: 116
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactDataHelper.addContact(ohos.sysappcomponents.contact.entity.Contact):long");
    }

    public Contact queryMyCard(ContactAttributes contactAttributes) {
        DataAbilityPredicates dataAbilityPredicates = new DataAbilityPredicates(RAW_RROFILE_FILTER);
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.valueOf((long) Attribute.Profile.MIN_ID));
        dataAbilityPredicates.setWhereArgs(arrayList);
        dataAbilityPredicates.orderByAsc("contact_id");
        ContactsCollection queryContactsByUri = queryContactsByUri(Uri.appendEncodedPathToUri(Attribute.Profile.CONTENT_URI, "data"), dataAbilityPredicates, contactAttributes, ContactsCollection.Type.DATA_CONTACT);
        if (queryContactsByUri != null) {
            return queryContactsByUri.next();
        }
        return null;
    }

    public Contact queryContact(String str, Holder holder, ContactAttributes contactAttributes) {
        if (str == null) {
            return null;
        }
        if ("profile".equals(str)) {
            return queryMyCard(contactAttributes);
        }
        ContactsCollection queryContactsByUri = queryContactsByUri(Uri.appendEncodedPathToUri(Uri.appendEncodedPathToUri(Attribute.Contacts.CONTENT_LOOKUP_URI, str), "data").makeBuilder().appendDecodedQueryParam(HOLDER, String.valueOf(getHolderId(holder))).build(), null, contactAttributes, ContactsCollection.Type.DATA_CONTACT);
        if (queryContactsByUri != null) {
            return queryContactsByUri.next();
        }
        return null;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0046 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean deleteContact(java.lang.String r7) {
        /*
            r6 = this;
            r0 = 0
            if (r7 != 0) goto L_0x0004
            return r0
        L_0x0004:
            ohos.app.Context r1 = r6.mContext
            if (r1 != 0) goto L_0x0009
            return r0
        L_0x0009:
            ohos.utils.net.Uri r1 = ohos.sysappcomponents.contact.Attribute.Contacts.CONTENT_LOOKUP_URI
            ohos.utils.net.Uri r1 = ohos.utils.net.Uri.appendEncodedPathToUri(r1, r7)
            ohos.app.Context r2 = r6.mContext
            ohos.aafwk.ability.DataAbilityHelper r2 = ohos.aafwk.ability.DataAbilityHelper.creator(r2)
            ohos.data.dataability.DataAbilityPredicates r3 = new ohos.data.dataability.DataAbilityPredicates
            java.lang.String r4 = "lookup=?"
            r3.<init>(r4)
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r4.add(r7)
            r3.setWhereArgs(r4)
            r7 = 0
            int r1 = r2.delete(r1, r3)     // Catch:{ DataAbilityRemoteException -> 0x0045 }
            java.lang.String r3 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ DataAbilityRemoteException -> 0x0046 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ DataAbilityRemoteException -> 0x0046 }
            r4.<init>()     // Catch:{ DataAbilityRemoteException -> 0x0046 }
            java.lang.String r5 = "ContactDataHelper deleteContactByKey end, result deleteNumber = "
            r4.append(r5)     // Catch:{ DataAbilityRemoteException -> 0x0046 }
            r4.append(r1)     // Catch:{ DataAbilityRemoteException -> 0x0046 }
            java.lang.String r4 = r4.toString()     // Catch:{ DataAbilityRemoteException -> 0x0046 }
            ohos.sysappcomponents.contact.LogUtil.info(r3, r4)     // Catch:{ DataAbilityRemoteException -> 0x0046 }
            goto L_0x004d
        L_0x0043:
            r0 = move-exception
            goto L_0x0055
        L_0x0045:
            r1 = r0
        L_0x0046:
            java.lang.String r3 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ all -> 0x0043 }
            java.lang.String r4 = "deleteContactByKey error"
            ohos.sysappcomponents.contact.LogUtil.error(r3, r4)     // Catch:{ all -> 0x0043 }
        L_0x004d:
            r6.clearEnvironment(r2, r7)
            if (r1 != 0) goto L_0x0053
            return r0
        L_0x0053:
            r6 = 1
            return r6
        L_0x0055:
            r6.clearEnvironment(r2, r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactDataHelper.deleteContact(java.lang.String):boolean");
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: ohos.sysappcomponents.contact.ContactDataHelper */
    /* JADX DEBUG: Multi-variable search result rejected for r5v2, resolved type: ohos.aafwk.ability.DataAbilityResult[] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1, types: [ohos.data.resultset.ResultSet] */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.sysappcomponents.contact.LogUtil.error(ohos.sysappcomponents.contact.ContactDataHelper.TAG, "updateContact error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004d, code lost:
        clearEnvironment(r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0050, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x003e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean updateContact(ohos.sysappcomponents.contact.entity.Contact r4, ohos.sysappcomponents.contact.ContactAttributes r5) {
        /*
            r3 = this;
            if (r4 != 0) goto L_0x0004
            r3 = 0
            return r3
        L_0x0004:
            ohos.sysappcomponents.contact.chain.Insertor r0 = ohos.sysappcomponents.contact.chain.Insertor.createChain(r5)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            if (r0 == 0) goto L_0x0014
            ohos.sysappcomponents.contact.chain.Insertor$OperationType r2 = ohos.sysappcomponents.contact.chain.Insertor.OperationType.UPDATE
            r0.fillOperation(r4, r1, r2)
        L_0x0014:
            if (r5 == 0) goto L_0x001e
            ohos.sysappcomponents.contact.ContactAttributes$Attribute r0 = ohos.sysappcomponents.contact.ContactAttributes.Attribute.ATTR_PORTRAIT
            boolean r5 = r5.isValid(r0)
            if (r5 == 0) goto L_0x002a
        L_0x001e:
            ohos.sysappcomponents.contact.chain.PortraitChain r5 = new ohos.sysappcomponents.contact.chain.PortraitChain
            ohos.app.Context r0 = r3.mContext
            r5.<init>(r0)
            ohos.sysappcomponents.contact.chain.Insertor$OperationType r0 = ohos.sysappcomponents.contact.chain.Insertor.OperationType.UPDATE
            r5.fillOperation(r4, r1, r0)
        L_0x002a:
            ohos.app.Context r4 = r3.mContext
            ohos.aafwk.ability.DataAbilityHelper r4 = ohos.aafwk.ability.DataAbilityHelper.creator(r4)
            r5 = 0
            ohos.utils.net.Uri r0 = ohos.sysappcomponents.contact.Attribute.CommonDataKinds.AUTHORITY     // Catch:{ DataAbilityRemoteException | OperationExecuteException -> 0x003e }
            ohos.aafwk.ability.DataAbilityResult[] r0 = r4.executeBatch(r0, r1)     // Catch:{ DataAbilityRemoteException | OperationExecuteException -> 0x003e }
            r3.clearEnvironment(r4, r5)
            r5 = r0
            goto L_0x0048
        L_0x003c:
            r0 = move-exception
            goto L_0x004d
        L_0x003e:
            java.lang.String r0 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ all -> 0x003c }
            java.lang.String r1 = "updateContact error"
            ohos.sysappcomponents.contact.LogUtil.error(r0, r1)     // Catch:{ all -> 0x003c }
            r3.clearEnvironment(r4, r5)
        L_0x0048:
            boolean r3 = r3.isSucceed(r5)
            return r3
        L_0x004d:
            r3.clearEnvironment(r4, r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactDataHelper.updateContact(ohos.sysappcomponents.contact.entity.Contact, ohos.sysappcomponents.contact.ContactAttributes):boolean");
    }

    private boolean isSucceed(DataAbilityResult[] dataAbilityResultArr) {
        if (dataAbilityResultArr == null || dataAbilityResultArr.length <= 0) {
            return false;
        }
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= dataAbilityResultArr.length) {
                return true;
            }
            if (dataAbilityResultArr[i] == null || dataAbilityResultArr[i].getCount().intValue() <= 0) {
                z = false;
            }
            if (!z) {
                return false;
            }
            i++;
        }
    }

    public static boolean isEnterpriseContactId(long j) {
        return Attribute.Contacts.isEnterpriseContactId(j);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.sysappcomponents.contact.LogUtil.error(ohos.sysappcomponents.contact.ContactDataHelper.TAG, "queryGroups error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005e, code lost:
        clearEnvironment(r0, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0061, code lost:
        if (r2 == null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0065, code lost:
        r7 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0057 */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x006b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.sysappcomponents.contact.entity.Group> queryGroups(ohos.sysappcomponents.contact.entity.Holder r7) {
        /*
        // Method dump skipped, instructions count: 111
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactDataHelper.queryGroups(ohos.sysappcomponents.contact.entity.Holder):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(12:3|4|5|6|7|8|9|10|18|19|20|21) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0057, code lost:
        r8 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0058, code lost:
        r1 = r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x004c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String queryKey(long r7, ohos.sysappcomponents.contact.entity.Holder r9) {
        /*
            r6 = this;
            ohos.app.Context r0 = r6.mContext
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            ohos.utils.net.Uri r0 = ohos.sysappcomponents.contact.Attribute.Contacts.CONTENT_URI
            ohos.utils.net.Uri$Builder r0 = r0.makeBuilder()
            ohos.app.Context r2 = r6.mContext     // Catch:{ DataAbilityRemoteException -> 0x004a, all -> 0x0047 }
            ohos.aafwk.ability.DataAbilityHelper r2 = ohos.aafwk.ability.DataAbilityHelper.creator(r2)     // Catch:{ DataAbilityRemoteException -> 0x004a, all -> 0x0047 }
            java.lang.String r3 = "directory"
            long r4 = r6.getHolderId(r9)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            java.lang.String r9 = java.lang.String.valueOf(r4)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            ohos.utils.net.Uri$Builder r9 = r0.appendDecodedQueryParam(r3, r9)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            ohos.utils.net.Uri r9 = r9.build()     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            ohos.data.dataability.DataAbilityPredicates r0 = new ohos.data.dataability.DataAbilityPredicates     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            java.lang.String r3 = "name_raw_contact_id=?"
            r0.<init>(r3)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            r3.<init>()     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            r3.add(r7)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            r0.setWhereArgs(r3)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            ohos.data.resultset.ResultSet r7 = r2.query(r9, r1, r0)     // Catch:{ DataAbilityRemoteException -> 0x0045, all -> 0x0043 }
            java.lang.String r1 = ohos.sysappcomponents.contact.creator.ContactCreator.getKey(r7)     // Catch:{ DataAbilityRemoteException -> 0x004c }
            goto L_0x0053
        L_0x0043:
            r8 = move-exception
            goto L_0x0059
        L_0x0045:
            r7 = r1
            goto L_0x004c
        L_0x0047:
            r8 = move-exception
            r2 = r1
            goto L_0x0059
        L_0x004a:
            r7 = r1
            r2 = r7
        L_0x004c:
            java.lang.String r8 = ohos.sysappcomponents.contact.ContactDataHelper.TAG     // Catch:{ all -> 0x0057 }
            java.lang.String r9 = "queryKey error"
            ohos.sysappcomponents.contact.LogUtil.error(r8, r9)     // Catch:{ all -> 0x0057 }
        L_0x0053:
            r6.clearEnvironment(r2, r7)
            return r1
        L_0x0057:
            r8 = move-exception
            r1 = r7
        L_0x0059:
            r6.clearEnvironment(r2, r1)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.ContactDataHelper.queryKey(long, ohos.sysappcomponents.contact.entity.Holder):java.lang.String");
    }

    public boolean isLocalContact(long j) {
        for (long j2 : LOCAL_HOLDER_ID) {
            if (queryKey(j, new Holder(j2)) != null) {
                return true;
            }
        }
        return false;
    }

    private void clearEnvironment(DataAbilityHelper dataAbilityHelper, ResultSet resultSet) {
        if (dataAbilityHelper != null) {
            dataAbilityHelper.release();
        }
        if (resultSet != null) {
            resultSet.close();
        }
    }

    private long getHolderId(Holder holder) {
        if (holder != null) {
            return holder.getHolderId();
        }
        return 0;
    }
}
