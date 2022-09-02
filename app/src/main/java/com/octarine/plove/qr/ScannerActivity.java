package com.octarine.plove.qr;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.Result;
import com.octarine.plove.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сканирование");
        builder.setMessage("Для начисления баллов по заказу отсканируйте QR код на чеке.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //builder.setView(mScannerView);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.plove_red));
                       // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("QR", rawResult.getText()); // Prints scan results
        Log.v("QR", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", rawResult.getText());
        setResult(RESULT_OK, returnIntent);
        finish();
        //mScannerView.resumeCameraPreview(this);
    }
}