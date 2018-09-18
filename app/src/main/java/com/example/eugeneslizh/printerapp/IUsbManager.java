package com.example.eugeneslizh.printerapp;

import android.hardware.usb.UsbDevice;

import java.util.List;
import java.util.Set;

interface IUsbManager extends IPrinterManager {

    List<UsbDevice> getListUsbDevices();

    Set<String> getKeyListUsbDevices();

    UsbDevice getUsbDevice(String name);

    List<String> getListNameDevices();

    IUsbPrinter prepareUsbPrinter(IUsbPrinter printer);

    IUsbPrinter getAvailablePrinter();
}
