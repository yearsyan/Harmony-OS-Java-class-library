package ohos.bluetooth;

import java.io.IOException;
import java.util.UUID;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class DataListenSocket {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "DataListenSocket");

    public SppServerSocket dataListenWithInsecureL2capChannel() throws IOException {
        SppServerSocket sppServerSocket = new SppServerSocket("BLE", 4, null, 0);
        HiLog.info(TAG, "dataListenWithInsecureL2capChannel", new Object[0]);
        sppServerSocket.bindListenSppServer();
        return sppServerSocket;
    }

    public SppServerSocket dataListenInsecureRfcommByServiceRecord(String str, UUID uuid) throws IOException {
        SppServerSocket sppServerSocket = new SppServerSocket(str, 1, uuid, 0);
        HiLog.info(TAG, "dataListenInsecureRfcommByServiceRecord", new Object[0]);
        sppServerSocket.bindListenSppServer();
        return sppServerSocket;
    }

    public SppServerSocket dataListenWithL2capChannel() throws IOException {
        SppServerSocket sppServerSocket = new SppServerSocket("BLE", 4, null, 0);
        sppServerSocket.setAuth(true);
        sppServerSocket.setEncrypt(true);
        sppServerSocket.bindListenSppServer();
        return sppServerSocket;
    }

    public SppServerSocket dataListenRfcommByServiceRecord(String str, UUID uuid) throws IOException {
        SppServerSocket sppServerSocket = new SppServerSocket(str, 1, uuid, 0);
        HiLog.info(TAG, "dataListenRfcommByServiceRecord", new Object[0]);
        sppServerSocket.setAuth(true);
        sppServerSocket.setEncrypt(true);
        sppServerSocket.bindListenSppServer();
        return sppServerSocket;
    }

    public SppClientSocket buildInsecureRfcommDataSocketByServiceRecord(BluetoothRemoteDevice bluetoothRemoteDevice, UUID uuid) throws IOException {
        SppClientSocket sppClientSocket = new SppClientSocket(bluetoothRemoteDevice, 1, uuid, 0);
        HiLog.info(TAG, "buildInsecureRfcommDataSocketByServiceRecord", new Object[0]);
        sppClientSocket.setAuth(false);
        sppClientSocket.setEncrypt(false);
        return sppClientSocket;
    }

    public SppClientSocket buildRfcommDataSocketByServiceRecord(BluetoothRemoteDevice bluetoothRemoteDevice, UUID uuid) throws IOException {
        SppClientSocket sppClientSocket = new SppClientSocket(bluetoothRemoteDevice, 1, uuid, 0);
        HiLog.info(TAG, "buildRfcommDataSocketByServiceRecord", new Object[0]);
        sppClientSocket.setAuth(true);
        sppClientSocket.setEncrypt(true);
        return sppClientSocket;
    }
}
