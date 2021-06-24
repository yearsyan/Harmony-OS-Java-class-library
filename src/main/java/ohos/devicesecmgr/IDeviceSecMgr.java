package ohos.devicesecmgr;

import ohos.rpc.IRemoteBroker;

public interface IDeviceSecMgr extends IRemoteBroker {
    DeviceSecInfo requestDeviceSecInfo(String str, RequestOption requestOption);
}
