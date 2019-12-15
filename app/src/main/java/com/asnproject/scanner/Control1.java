package com.asnproject.scanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class Control1 extends AppCompatActivity {
    String topicStr = "LED";
    MqttAndroidClient client;
    ToggleButton toggleButton;
    Button scan;

    //memanggi preference
    Preferences session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control1);
        toggleButton = (ToggleButton) findViewById(R.id.btControl1);
        scan = (Button) findViewById(R.id.button2);

        session = new Preferences(Control1.this.getApplicationContext());


        String Host = session.getPort();
        String us = session.getUser();
        String pas = session.getPass();

        final String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), Host , clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(us);
        options.setPassword(pas.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Control1.this,"Connected MQTT",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(Control1.this,"Not Connected MQTT",Toast.LENGTH_LONG).show();
                    showAlertDialog(clientId);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    String topic = topicStr;
                    String message = "L1";
                    try {
                        client.publish(topic, message.getBytes(),0,false  );
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    String topic = topicStr;
                    String message = "L2";
                    try {
                        client.publish(topic, message.getBytes(),0,false  );
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Control1.this, Main2Activity.class);
                startActivity(i);
                session.setUser("setUsername");
                session.setPass("setPassword");
                session.setPort("setPort");

            }
        });
    }

    private void showAlertDialog(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Scan Your Barcode First\nTo Connect Control Your Device");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
