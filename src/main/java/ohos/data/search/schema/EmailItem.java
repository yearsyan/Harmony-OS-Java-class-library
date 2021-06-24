package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class EmailItem extends Schema<EmailItem> {
    public static final String ATTACHMENT = "attachment";
    public static final String BCC_EMAIL = "bccEmail";
    public static final String BCC_NAME = "bccName";
    public static final String CC_EMAIL = "ccEmail";
    public static final String CC_NAME = "ccName";
    public static final String CONTENT = "content";
    public static final String SENDER_EMAIL = "senderEmail";
    public static final String SENDER_NAME = "senderName";
    public static final String TO_EMAIL = "toEmail";
    public static final String TO_NAME = "toName";

    public static List<IndexForm> getEmailSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.EmailItem.AnonymousClass1 */

            {
                add(new IndexForm(EmailItem.BCC_EMAIL, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(EmailItem.BCC_NAME, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(EmailItem.CC_EMAIL, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(EmailItem.CC_NAME, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(EmailItem.TO_EMAIL, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(EmailItem.TO_NAME, IndexType.ANALYZED, false, true, true));
                add(new IndexForm("senderName", IndexType.ANALYZED, false, true, true));
                add(new IndexForm(EmailItem.SENDER_EMAIL, IndexType.ANALYZED, false, true, true));
                add(new IndexForm("content", IndexType.ANALYZED, false, true, true));
                add(new IndexForm("attachment", IndexType.ANALYZED, false, false, true));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public EmailItem() {
        super.set(this);
    }

    public EmailItem setBccEmail(String str) {
        super.put(BCC_EMAIL, str);
        return this;
    }

    public EmailItem setBccName(String str) {
        super.put(BCC_NAME, str);
        return this;
    }

    public EmailItem setCcEmail(String str) {
        super.put(CC_EMAIL, str);
        return this;
    }

    public EmailItem setCcName(String str) {
        super.put(CC_NAME, str);
        return this;
    }

    public EmailItem setToEmail(String str) {
        super.put(TO_EMAIL, str);
        return this;
    }

    public EmailItem setToName(String str) {
        super.put(TO_NAME, str);
        return this;
    }

    public EmailItem setSenderEmail(String str) {
        super.put(SENDER_EMAIL, str);
        return this;
    }

    public EmailItem setSenderName(String str) {
        super.put("senderName", str);
        return this;
    }

    public EmailItem setContent(String str) {
        super.put("content", str);
        return this;
    }

    public EmailItem setAttachment(String str) {
        super.put("attachment", str);
        return this;
    }

    public String getBccEmail() {
        return super.getAsString(BCC_EMAIL);
    }

    public String getBccName() {
        return super.getAsString(BCC_NAME);
    }

    public String getCcEmail() {
        return super.getAsString(CC_EMAIL);
    }

    public String getCcName() {
        return super.getAsString(CC_NAME);
    }

    public String getToEmail() {
        return super.getAsString(TO_EMAIL);
    }

    public String getToName() {
        return super.getAsString(TO_NAME);
    }

    public String getSenderEmail() {
        return super.getAsString(SENDER_EMAIL);
    }

    public String getSenderName() {
        return super.getAsString("senderName");
    }

    public String getContent() {
        return super.getAsString("content");
    }

    public String getAttachment() {
        return super.getAsString("attachment");
    }
}
