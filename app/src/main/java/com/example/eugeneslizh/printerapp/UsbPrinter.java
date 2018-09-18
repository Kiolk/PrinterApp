package com.example.eugeneslizh.printerapp;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.util.Log;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.List;

public class UsbPrinter implements IUsbPrinter {

    List<UsbInterface> mListUsbInterfaces = new ArrayList<>();
    UsbInterface mUsbInterface;
    UsbDeviceConnection mConnection;
    UsbEndpoint mUsbEndpoint;
    private final String TAG = "MyLogs";

    public UsbPrinter (UsbDeviceConnection connection, UsbEndpoint endpoint, UsbInterface usbInterface){
        mConnection = connection;
        mUsbEndpoint = endpoint;
        mUsbInterface = usbInterface;
        Log.d(TAG, "UsbPrinter() called with: connection = [" + connection + "], endpoint = [" + endpoint + "], usbInterface = [" + usbInterface + "]");
    }


    @Override
    public void prepare() {

    }

    @Override
    public int print(String msg) {
        if(mConnection != null) {
            int resultTransfare = mConnection.bulkTransfer(mUsbEndpoint, msg.getBytes(), msg.getBytes().length, 10000);
            Log.d(TAG, "print: " + resultTransfare);
            return resultTransfare;
        }
        return -2;
    }

    @Override
    public void release() {
        if(mConnection != null) {
            boolean result = mConnection.releaseInterface(mUsbInterface);
            Log.d(TAG, "release: " + result);

        }
    }

    @Override
    public void setInterface(UsbInterface usbInterface) {
        mListUsbInterfaces.add(usbInterface);
    }
}
