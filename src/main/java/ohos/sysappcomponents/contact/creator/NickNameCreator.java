package ohos.sysappcomponents.contact.creator;

import ohos.data.resultset.ResultSet;
import ohos.sysappcomponents.contact.entity.NickName;

public class NickNameCreator {
    private NickNameCreator() {
    }

    public static NickName createImFromDataContact(ResultSet resultSet) {
        int columnIndexForName;
        if (resultSet == null || (columnIndexForName = resultSet.getColumnIndexForName("_id")) == -1) {
            return null;
        }
        int i = resultSet.getInt(columnIndexForName);
        int columnIndexForName2 = resultSet.getColumnIndexForName("data1");
        if (columnIndexForName2 == -1) {
            return null;
        }
        String string = resultSet.getString(columnIndexForName2);
        NickName nickName = new NickName();
        nickName.setNickName(string);
        nickName.setId(i);
        return nickName;
    }
}
