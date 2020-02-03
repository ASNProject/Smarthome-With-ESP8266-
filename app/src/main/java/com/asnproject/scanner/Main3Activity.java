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
import android.widget.Button;
import android.widget.EditText;
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

public class Main3Activity extends AppCompatActivity {
    private Button scan;
    private EditText user;
     EditText pass1;
    private TextView port;
    private Button next;
    private String hasil;
    private String username;
    private String password;
    private String ports;
    private String KEY_BARCODE = "BARCODE";
    private String KEY_USERNAME = "USERNAME";
    private String KEY_PASSWORD = "PASSWORD";
    private String KEY_PORT = "PORT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = findViewById(R.id.button3);
        user = findViewById(R.id.editText3);
        //pass = findViewById(R.id.textView7);
       // pass1 = (EditText) findViewById(R.id.editText);
        //port = findViewById(R.id.textView6);
        next = findViewById(R.id.button4);

        if(getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            user.setText(bundle.getString("barcode"));

            //port.setText(bundle.getString("port"));
        }


        //Bundle extras = getIntent().getExtras();
       // Intent intent =getIntent();
        //String UN = intent.getStringExtra("KEY_USERNAME");
        //username = extras.getString(KEY_USERNAME);
        //user.setText(UN);
        //password = extras.getString(KEY_PASSWORD);
        //pass.setText(password);
        //ports = extras.getString(KEY_PORT);
        //port.setText(ports);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

    }


}
