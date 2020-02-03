package com.asnproject.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    private Button play;
    private TextView textView;
    private Button next;
    private String hasil;
    private String KEY_BARCODE = "BARCODE";
    Preferences session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new Preferences(MainActivity.this.getApplicationContext());
        play = findViewById(R.id.button3);
        next = findViewById(R.id.button4);
        textView = findViewById(R.id.label);


        String user = session.getPort();
        textView.setText(user);
        if (textView.getText().toString().length()==0) {
            showDialog();

        }

           // Intent i = new Intent(MainActivity.this, Main2Activity.class);
           // startActivity(i);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Control1.class);
                startActivity(intent);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, webview.class);
                startActivity(i);
            }
        });

    }
    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Let Get Started");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Hallo, Welcome to VENERGY SMARTHOME\n\nPlease Connect Your Android to WiFi Device")
                .setIcon(R.drawable.logo)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        Intent i = new Intent(MainActivity.this, webview.class);
                        startActivity(i);
                        finish();

                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

}
