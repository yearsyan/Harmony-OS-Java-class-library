package com.huawei.ace.systemplugin.httpaccess;

import com.huawei.ace.systemplugin.httpaccess.data.ResponseData;

public interface HttpProbe {
    void onResponse(ResponseData responseData);
}
