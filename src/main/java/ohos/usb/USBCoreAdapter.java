package ohos.usb;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import java.util.HashMap;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class USBCoreAdapter {
    private static final String HILOG_TAG = "USBCoreAdapter";
    private static final HiLogLabel LABEL = USBLoggerFactory.getHilogLabel(HILOG_TAG);
    private static final Object LOCK = new Object();
    private static final String USB_PACKAGE_NAME = "ohos.usb";
    private static final String USB_SERVICE_NAME = "UsbService";
    private static UsbManager usbRemoteObject;
    private Context context;
    private HashMap<String, UsbDevice> deviceList = new HashMap<>();

    public USBCoreAdapter(ohos.app.Context context2) {
        if (context2 != null) {
            this.context = (Context) context2.getHostContext();
            Context context3 = this.context;
            if (context3 != null) {
                usbRemoteObject = (UsbManager) context3.getSystemService("usb");
            } else {
                HiLog.error(LABEL, "USBCoreAdapter: context is null.", new Object[0]);
            }
        } else {
            HiLog.error(LABEL, "USBCoreAdapter: abilityContext is null.", new Object[0]);
        }
    }

    private USBConfig converseFromUsbConfiguration(UsbConfiguration usbConfiguration) {
        if (usbConfiguration == null) {
            return null;
        }
        return new USBConfig(usbConfiguration.getId(), usbConfiguration.getMaxPower(), usbConfiguration.getName(), usbConfiguration.isRemoteWakeup(), usbConfiguration.isSelfPowered());
    }

    private USBInterface converseFromUsbInterface(UsbInterface usbInterface) {
        if (usbInterface == null) {
            return null;
        }
        return new USBInterface(usbInterface.getId(), usbInterface.getAlternateSetting(), usbInterface.getName(), usbInterface.getInterfaceClass(), usbInterface.getInterfaceSubclass(), usbInterface.getInterfaceProtocol());
    }

    private USBEndpoint converseFromUsbEndpoint(UsbEndpoint usbEndpoint) {
        if (usbEndpoint == null) {
            return null;
        }
        return new USBEndpoint(usbEndpoint.getAddress(), usbEndpoint.getAttributes(), usbEndpoint.getDirection(), usbEndpoint.getEndpointNumber(), usbEndpoint.getInterval(), usbEndpoint.getMaxPacketSize(), usbEndpoint.getType());
    }

    public void getDevices(HashMap<String, USBDevice> hashMap) {
        int i;
        int i2 = 0;
        HiLog.info(LABEL, "calling getDevices begin", new Object[0]);
        this.deviceList = usbRemoteObject.getDeviceList();
        HashMap<String, UsbDevice> hashMap2 = this.deviceList;
        if (hashMap2 != null) {
            for (UsbDevice usbDevice : hashMap2.values()) {
                USBDevice converseFromUsbDevice = converseFromUsbDevice(usbDevice);
                int configurationCount = usbDevice.getConfigurationCount();
                HiLogLabel hiLogLabel = LABEL;
                Object[] objArr = new Object[1];
                objArr[i2] = Integer.valueOf(configurationCount);
                HiLog.info(hiLogLabel, "calling getDevices usbDevice.getConfigurationCount() %{public}d", objArr);
                if (configurationCount > 0) {
                    USBConfig[] uSBConfigArr = new USBConfig[configurationCount];
                    int i3 = i2;
                    while (i3 < configurationCount) {
                        UsbConfiguration configuration = usbDevice.getConfiguration(i3);
                        if (configuration != null) {
                            uSBConfigArr[i3] = converseFromUsbConfiguration(configuration);
                            int interfaceCount = configuration.getInterfaceCount();
                            USBInterface[] uSBInterfaceArr = new USBInterface[interfaceCount];
                            int i4 = i2;
                            while (i4 < interfaceCount) {
                                UsbInterface usbInterface = configuration.getInterface(i4);
                                if (usbInterface != null) {
                                    uSBInterfaceArr[i4] = converseFromUsbInterface(usbInterface);
                                    int endpointCount = usbInterface.getEndpointCount();
                                    USBEndpoint[] uSBEndpointArr = new USBEndpoint[endpointCount];
                                    for (int i5 = i2; i5 < endpointCount; i5++) {
                                        UsbEndpoint endpoint = usbInterface.getEndpoint(i5);
                                        if (endpoint != null) {
                                            uSBEndpointArr[i4] = converseFromUsbEndpoint(endpoint);
                                        } else {
                                            return;
                                        }
                                    }
                                    uSBInterfaceArr[i3].setEndpoints(uSBEndpointArr);
                                    i4++;
                                    i2 = 0;
                                } else {
                                    return;
                                }
                            }
                            uSBConfigArr[i3].setInterfaces(uSBInterfaceArr);
                            i3++;
                            i2 = 0;
                        } else {
                            return;
                        }
                    }
                    converseFromUsbDevice.setConfigurations(uSBConfigArr);
                    hashMap.put(usbDevice.getDeviceName(), converseFromUsbDevice);
                    i = 0;
                    HiLog.info(LABEL, "calling getDevices put a device to usbDevices.", new Object[0]);
                } else {
                    i = i2;
                }
                i2 = i;
            }
        }
    }

    public USBDevicePipe connectDevice(USBDevice uSBDevice) {
        HiLog.info(LABEL, "calling connectDevice begin", new Object[0]);
        if (uSBDevice == null) {
            return null;
        }
        String deviceName = uSBDevice.getDeviceName();
        UsbDevice converseToUsbDevice = converseToUsbDevice(uSBDevice);
        if (converseToUsbDevice == null) {
            return null;
        }
        UsbDeviceConnection openDevice = usbRemoteObject.openDevice(converseToUsbDevice);
        if (openDevice == null) {
            HiLog.error(LABEL, "error,open the device %{public}s error.", deviceName);
            return null;
        }
        int fileDescriptor = openDevice.getFileDescriptor();
        if (fileDescriptor < 0) {
            return null;
        }
        USBDevicePipe uSBDevicePipe = new USBDevicePipe(uSBDevice);
        if (!uSBDevicePipe.openByInt(deviceName, fileDescriptor)) {
            return null;
        }
        return uSBDevicePipe;
    }

    private USBDevice converseFromUsbDevice(UsbDevice usbDevice) {
        if (usbDevice == null) {
            return null;
        }
        return new USBDevice(usbDevice.getDeviceName(), usbDevice.getVendorId(), usbDevice.getProductId(), usbDevice.getDeviceClass(), usbDevice.getDeviceSubclass(), usbDevice.getDeviceProtocol(), usbDevice.getManufacturerName(), usbDevice.getProductName(), usbDevice.getVersion(), "NULL_SerialNumber");
    }

    private UsbDevice converseToUsbDevice(USBDevice uSBDevice) {
        if (uSBDevice == null) {
            return null;
        }
        HiLog.info(LABEL, "calling getDevices begin", new Object[0]);
        for (UsbDevice usbDevice : this.deviceList.values()) {
            if (usbDevice.getDeviceName() == uSBDevice.getDeviceName()) {
                return usbDevice;
            }
        }
        return null;
    }

    public boolean hasRight(USBDevice uSBDevice) {
        if (uSBDevice != null) {
            return usbRemoteObject.hasPermission(converseToUsbDevice(uSBDevice));
        }
        HiLog.error(LABEL, "error, device is null in hasRight().", new Object[0]);
        return false;
    }

    public void requestRight(USBDevice uSBDevice, String str) {
        if (uSBDevice != null && str != null) {
            usbRemoteObject.requestPermission(converseToUsbDevice(uSBDevice), PendingIntent.getBroadcast(this.context, 0, new Intent(str), 1073741824));
        }
    }
}
