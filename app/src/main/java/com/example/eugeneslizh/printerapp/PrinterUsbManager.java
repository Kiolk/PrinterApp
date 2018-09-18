package com.example.eugeneslizh.printerapp;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrinterUsbManager implements IUsbManager {

    private UsbManager manager;

    public PrinterUsbManager(Context context){
       manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }


    @Override
    public List<UsbDevice> getListUsbDevices() {
        List<UsbDevice> result = new ArrayList<UsbDevice>();
        for(UsbDevice device : manager.getDeviceList().values()){
            result.add(device);
        }
        return result;
    }

    @Override
    public Set<String> getKeyListUsbDevices() {
        return manager.getDeviceList().keySet();
    }

    @Override
    public UsbDevice getUsbDevice(String name) {
        return manager.getDeviceList().get(name);
    }

    @Override
    public List<String> getListNameDevices() {
        List<String> names = new ArrayList<>();
        for(UsbDevice device : getListUsbDevices()){
            names.add(device.getDeviceName());
        }
        return names;
    }

    @Override
    public IUsbPrinter prepareUsbPrinter(IUsbPrinter printer) {
        return null;
    }

    @Override
    public IUsbPrinter getAvailablePrinter() {
        UsbEndpoint currentEndpoint = null;
        UsbInterface usbInterface = null;
        UsbDevice usbDevice = null;

        for(UsbDevice device : getListUsbDevices()){
            int numberInterfaceses = device.getInterfaceCount();
            for(int index = 0; index < numberInterfaceses; ++index){
                int numberEndpoints = device.getInterface(index).getEndpointCount();
                for(int endpointIndex = 0; endpointIndex < numberEndpoints; ++endpointIndex){
                    UsbEndpoint endpoint = device.getInterface(index).getEndpoint(endpointIndex);
                    if(endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK && endpoint.getDirection() == UsbConstants.USB_DIR_OUT){
                        currentEndpoint = endpoint;
                        usbInterface = device.getInterface(index);
                        usbDevice = device;
                    }
                }
            }
        }
        if(usbDevice != null && usbInterface != null) {
            UsbDeviceConnection connection = manager.openDevice(usbDevice);
            if(connection != null) {

                connection.claimInterface(usbInterface, true);
                return new UsbPrinter(manager.openDevice(usbDevice), currentEndpoint, usbInterface);
            }
        }

        return null;
    }

    @Override
    public List<String> getListOfDevice() {
        List<String> names = new ArrayList<>();
        for(UsbDevice device : getListUsbDevices()){
            names.add(device.getDeviceName());
        }
        return names;
    }

    @Override
    public IUsbPrinter getPrinter(String name) {
        return getAvailablePrinter();
    }
}
