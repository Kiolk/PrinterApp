package com.example.eugeneslizh.printerapp;

import android.hardware.usb.UsbInterface;

interface IUsbPrinter extends IPrinter{

    void setInterface(UsbInterface usbInterface);

}
