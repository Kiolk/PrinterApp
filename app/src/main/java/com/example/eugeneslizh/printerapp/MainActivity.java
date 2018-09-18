package com.example.eugeneslizh.printerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mPrintButton;
    private TextView mDescription;
    USBAdapter adapter = new USBAdapter();
    private PrinterUsbManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrintButton = findViewById(R.id.print_button);
        mDescription = findViewById(R.id.description_text);
        mPrintButton.setOnClickListener(this);
        manager = new PrinterUsbManager(this);

    }

    @Override
    public void onClick(View v) {
        IUsbPrinter printer = manager.getAvailablePrinter();
        if(printer != null) {
            printer.prepare();
            int result = printer.print("Test");
            Toast.makeText(this, "Printer try work " + result, Toast.LENGTH_SHORT).show();
            printer.release();
        }
        mDescription.setText(manager.getListNameDevices().toString());
    }
}
