package com.example.eugeneslizh.printerapp;

public interface IPrinter {
    void prepare();

    int print(String msg);

    void release();
}
