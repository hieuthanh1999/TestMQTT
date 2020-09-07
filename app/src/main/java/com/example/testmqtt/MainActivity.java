package com.example.testmqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    MqttAndroidClient client;
    String clientId;
    private String TAG = "Test";
    Button btn;
    private String topic;
    TextView view;
    String Text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnbutton);
        view = findViewById(R.id.txt);
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883", clientId);
         topic = "testtopic/Test";


      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              try {
                  IMqttToken token = client.connect();
                  token.setActionCallback(new IMqttActionListener() {
                      @Override
                      public void onSuccess(IMqttToken asyncActionToken) {
                          // We are connected
                          //Log.d(TAG, "onSuccess");
                          Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                          sub();
                      }
                      @Override
                      public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                          // Something went wrong e.g. connection timeout or firewall problems
                          Log.d(TAG, "onFailure");
                          Toast.makeText(MainActivity.this, "onFail", Toast.LENGTH_SHORT).show();
                          sub();
                      }
                  });
              } catch (MqttException e) {
                  e.printStackTrace();
              }
          }
      });
    }
    private void sub() {
        try {
            client.subscribe(topic, 0);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //log
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Toast.makeText(MainActivity.this, "topic:" + topic + "\n" , Toast.LENGTH_SHORT).show();
                    Text += "topic:" + topic + "\n" + "message:" + new String(message.getPayload());
                    view.setText(Text);

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //toast or log
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
}
