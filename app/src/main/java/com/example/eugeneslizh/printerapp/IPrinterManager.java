package com.example.eugeneslizh.printerapp;

import java.util.List;

public interface IPrinterManager {

    List<String> getListOfDevice();

    IPrinter getPrinter(String name);
}
