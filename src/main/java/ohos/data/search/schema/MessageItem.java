package ohos.data.search.schema;

import java.util.ArrayList;
import java.util.List;
import ohos.data.search.model.IndexForm;
import ohos.data.search.model.IndexType;

public final class MessageItem extends Schema<MessageItem> {
    public static final String ATTACHMENT = "attachment";
    public static final String CONTENT = "content";
    public static final String RECIPIENT_NAME = "recipientName";
    public static final String RECIPIENT_PHONE = "recipientPhone";
    public static final String SENDER_NAME = "senderName";
    public static final String SENDER_PHONE = "senderPhone";

    public static List<IndexForm> getMessageSchema() {
        AnonymousClass1 r0 = new ArrayList<IndexForm>() {
            /* class ohos.data.search.schema.MessageItem.AnonymousClass1 */

            {
                add(new IndexForm("senderName", IndexType.ANALYZED, false, true, true));
                add(new IndexForm(MessageItem.SENDER_PHONE, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(MessageItem.RECIPIENT_NAME, IndexType.ANALYZED, false, true, true));
                add(new IndexForm(MessageItem.RECIPIENT_PHONE, IndexType.ANALYZED, false, true, true));
                add(new IndexForm("content", IndexType.ANALYZED, false, true, true));
                add(new IndexForm("attachment", IndexType.ANALYZED, false, false, true));
            }
        };
        r0.addAll(CommonItem.getCommonSchema());
        return r0;
    }

    public MessageItem() {
        super.set(this);
    }

    public MessageItem setSenderName(String str) {
        super.put("senderName", str);
        return this;
    }

    public MessageItem setSenderPhone(String str) {
        super.put(SENDER_PHONE, str);
        return this;
    }

    public MessageItem setRecipientName(String str) {
        super.put(RECIPIENT_NAME, str);
        return this;
    }

    public MessageItem setRecipientPhone(String str) {
        super.put(RECIPIENT_PHONE, str);
        return this;
    }

    public MessageItem setContent(String str) {
        super.put("content", str);
        return this;
    }

    public MessageItem setAttachment(String str) {
        super.put("attachment", str);
        return this;
    }

    public String getSenderName() {
        return super.getAsString("senderName");
    }

    public String getSenderPhone() {
        return super.getAsString(SENDER_PHONE);
    }

    public String getRecipientName() {
        return super.getAsString(RECIPIENT_NAME);
    }

    public String getRecipientPhone() {
        return super.getAsString(RECIPIENT_PHONE);
    }

    public String getContent() {
        return super.getAsString("content");
    }

    public String getAttachment() {
        return super.getAsString("attachment");
    }
}
