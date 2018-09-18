package com.example.eugeneslizh.printerapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothManager implements IPrinterManager {


    @Override
    public List<String> getListOfDevice() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        List<String> bluetoseName = new ArrayList<>();
        try {

            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    bluetoseName.add(device.getName());
                }

                return bluetoseName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bluetoseName;
    }

    @Override
    public IPrinter getPrinter(String name) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice lookingDevice = null;
        for (BluetoothDevice device : adapter.getBondedDevices()) {
            if(device.getName().equals(name)){
                lookingDevice = device;
            }
        }

        return new BluetoothPrinter();
    }
}
