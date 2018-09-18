package com.example.eugeneslizh.printerapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class BluetoothPrinter implements IPrinter {

    private final BluetoothDevice mDevice;
    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private String value;
    private byte[] readBuffer;
    private int mBufferPosition;
    private Thread mWorkerThred;
    private boolean mStopWork;

    BluetoothPrinter(BluetoothDevice device) {
        this.mDevice = device;
    }

    @Override
    public void prepare() {
        initPrinter();
    }

    @Override
    public int print(String msg) {
        IntentPrinter(msg);
        return 0;
    }

    @Override
    public void release() {

    }

    private void IntentPrinter(String text) {
        byte[] textArray = text.getBytes();
        byte[] hader = new byte[]{(byte) 0xAA, 0x55, 2, 0};
        hader[3] = (byte) textArray.length;
        initPrinter();

        if (hader.length > 128) {
            value += "header length more than 128";
        } else {
            try {
                mOutputStream.write(text.getBytes());
                mOutputStream.close();
                mSocket.close();
            } catch (IOException pE) {
                pE.printStackTrace();
                value += " Ixception in IntentPrinter";
            }
        }
    }

    private void initPrinter() {
        try {

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            Method method = mDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            mSocket = (BluetoothSocket) method.invoke(mDevice, 1);
//                mBluetoothAdapter.cancelDiscovery();
            mSocket.connect();
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();
            beginListenForData();
        } catch (Exception e) {
            e.printStackTrace();
            value += e.getMessage();
        }
    }

    private void beginListenForData() {
        try {

            final Handler handler = new Handler();
            final byte delimeter = 10;
            mStopWork = false;
            mBufferPosition = 0;
            readBuffer = new byte[1024];
            mWorkerThred = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !mStopWork) {

                        try {
                            int byteAvailable = mInputStream.available();

                            if (byteAvailable > 0) {
                                byte[] bytePacket = new byte[byteAvailable];
                                mInputStream.read(bytePacket);
                                for (int index = 0; index < byteAvailable; ++index) {
                                    byte someByte = bytePacket[index];

                                    if (someByte == delimeter) {
                                        byte[] encodedeBytes = new byte[mBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedeBytes, 0,
                                                encodedeBytes.length
                                        );
                                        final String data = new String(encodedeBytes, "US-ASCII");
                                        mBufferPosition = 0;
                                        handler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                Log.d("MyLogs", data);
                                            }
                                        });
                                    } else {
                                        readBuffer[mBufferPosition++] = someByte;
                                    }
                                }
                            }
                        } catch (IOException pE) {
                            pE.printStackTrace();
                            mStopWork = true;
                        }
                    }
                }
            });
            mWorkerThred.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
