package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class ContactItem extends Schema<ContactItem> {
    public static final String ADDRESS = "address";
    public static final String BIRTH_DATE = "birthDate";
    public static final String EMAIL = "email";
    public static final String GENDER = "gender";
    public static final String PHONE = "phone";
    public static final String WORK_LOCATION = "workLocation";

    public static List<IndexForm> getContactSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.ContactItem.AnonymousClass1 */

            {
                add(new IndexForm(ContactItem.PHONE, IndexType.ANALYZED, false, true, true));
                add(new IndexForm("email", IndexType.ANALYZED, false, true, true));
                add(new IndexForm(ContactItem.BIRTH_DATE, "long", false, true, false));
                add(new IndexForm(ContactItem.GENDER, IndexType.SORTED_NO_ANALYZED, false, true, false));
                add(new IndexForm(ContactItem.ADDRESS, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(ContactItem.WORK_LOCATION, IndexType.ANALYZED, false, true, true));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public ContactItem() {
        super.set(this);
    }

    public ContactItem setPhone(String str) {
        super.put(PHONE, str);
        return this;
    }

    public ContactItem setEmail(String str) {
        super.put("email", str);
        return this;
    }

    public ContactItem setBirthDate(Long l) {
        super.put(BIRTH_DATE, l);
        return this;
    }

    public ContactItem setGender(String str) {
        super.put(GENDER, str);
        return this;
    }

    public ContactItem setAddress(String str) {
        super.put(ADDRESS, str);
        return this;
    }

    public ContactItem setWorkLocation(String str) {
        super.put(WORK_LOCATION, str);
        return this;
    }

    public String getPhone() {
        return super.getAsString(PHONE);
    }

    public String getEmail() {
        return super.getAsString("email");
    }

    public Long getBirthDate() {
        return super.getAsLong(BIRTH_DATE);
    }

    public String getGender() {
        return super.getAsString(GENDER);
    }

    public String getAddress() {
        return super.getAsString(ADDRESS);
    }

    public String getWorkLocation() {
        return super.getAsString(WORK_LOCATION);
    }
}
