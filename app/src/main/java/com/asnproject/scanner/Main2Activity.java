package com.asnproject.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Main2Activity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;

    private EditText USERNAME;
    private EditText PASSWORD;
    private EditText PORT;


    Preferences session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        USERNAME = findViewById(R.id.User);
        PASSWORD = findViewById(R.id.Pass);
        PORT = findViewById(R.id.Port);

        session = new Preferences(Main2Activity.this.getApplicationContext());

        scannerView = findViewById(R.id.scanner);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = result.getText();
                        String barcode = message.toString();
                        String part1,part2,part3;
                        String[] part = barcode.split("-");

                        part1 = part[0];
                        part2 = part[1];
                        part3 = part[2];

                        USERNAME.setText(part1);
                        PASSWORD.setText(part2);
                        PORT.setText(part3);

                        showAlertDialog(message);
                    }
                });
            }
        });
        checkCameraPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCameraPermission();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void checkCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


    private void showAlertDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save Your Code Device");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "SAVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                        String user = String.valueOf(USERNAME.getText());
                        String pass = String.valueOf(PASSWORD.getText());
                        String port = String.valueOf(PORT.getText());
                        session.setUser(user);
                        session.setPass(pass);
                        session.setPort(port);

                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                        startActivity(intent);
                        //mCodeScanner.startPreview();
                        finish();

                                Toast.makeText(getApplication(), "SAVED",Toast.LENGTH_SHORT);
                    }
                }
        );
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
