/*
 *               ==            ============    ====            ====  ================
 *            ========        ===============  ======          ====  ==================
 *          ============     ====        ====  =======         ====  ====           ====
 *       ====          ====  ====              ========        ====  ====           ====
 *       ====          ====  ====              ==== ====       ====  ====           ====
 *       ==================  ===============   ====  ====      ====  ==================   ===  ========       ======     ============     ======         ======         ====         ======
 *       ==================  ================  ====   ====     ====  ===============      ==== =========   ============  ============  ============   ============  =============  ====   ====
 *       ====          ====              ====  ====    ====    ====  ====                 ====       ====  ====    ====  ==      ====  ====     ====  ====    ====  =============  ====
 *       ====          ====              ====  ====     =====  ====  ====                 ====             ====    ====          ====  =============  ====              ====       ===========
 *       ====          ====  ====        ====  ====      ===== ====  ====                 ====             ====    ====          ====  ====           ====    ====      ====              ====
 *       ====          ====   ==============   ====       =========  ====                 ====             ============          ====  =============  ============      ====       ====   ====       
 *       ====          ====    ============    ====        ========  ====                 ====                ======             ====     ======         ======          ======      ======
 *                                                                                                                       ==      ====
 *                                                                                                                       =========== 
 *                                                                                                                         ========
 *                                                                                                                           
 * ESP8266 Connect WIFI via Smartphone                                                                                                                        
 * Original Program "https://how2electronics.com/esp8266-manual-wifi-configuration-without-hard-code-with-eeprom/"
 * Developed by Arief Setyo Nugroho
 * Email:asnprojet02@gmial.com
 * Github:https://github.com/ASNProject 
 * 14 Desember 2019
 * Yogyakarta
 */

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>
#include <PubSubClient.h>

//Variables
int i = 0;
int statusCode;
const char* ssid = "text";
const char* passphrase = "text";

//MQTTT Variable
const char* mqttServer = "tailor.cloudmqtt.com"; 
const int mqttPort = 13522; 
const char* mqttUser = "dncrdumf"; 
const char* mqttPassword = "JLxl6L3MGMed"; 

String st;
String content;

int ledpin = 5; // D1(gpio5)
int button = 4; //D2(gpio4)
int buttonState=0;
String rst;

//Konfigurasi Server MQTT
WiFiClient espClient;
PubSubClient client(espClient);

const int LED2 = 0;

void mqtt_callback(char* topic, byte* dados_tcp, unsigned int length);


//Function Decalration
bool testWifi(void);
void launchWeb(void);
void setupAP(void);

//Establishing Local server at port 80 whenever required
ESP8266WebServer server(80);

void(* reset) (void) =0;


void setup()
{
  pinMode(ledpin, OUTPUT);
  pinMode(button, INPUT);
  digitalWrite(ledpin, HIGH);
  delay(1000);
  digitalWrite(ledpin, LOW);
  Serial.begin(115200); //Initialising if(DEBUG)Serial Monitor
  Serial.println();
  Serial.println("Disconnecting previously connected WiFi");
  WiFi.disconnect();
  EEPROM.begin(512); //Initialasing EEPROM
  delay(10);
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.println();
  Serial.println();
  Serial.println("Startup");

  //---------------------------------------- Read eeprom for ssid and pass
  Serial.println("Reading EEPROM ssid");

  String esid;
  for (int i = 0; i < 32; ++i)
  {
    esid += char(EEPROM.read(i));
  }
  Serial.println();
  Serial.print("SSID: ");
  Serial.println(esid);
  Serial.println("Reading EEPROM pass");

  String epass = "";
  for (int i = 32; i < 96; ++i)
  {
    epass += char(EEPROM.read(i));
  }
  Serial.print("PASS: ");
  Serial.println(epass);


  WiFi.begin(esid.c_str(), epass.c_str());
  if (testWifi())
  {
    Serial.println("Succesfully Connected!!!");
    return;

  }else
  {
    Serial.println("Turning the HotSpot On");
    launchWeb();
    setupAP();// Setup HotSpot
  }

  Serial.println();
  Serial.println("Waiting.");
  
  while ((WiFi.status() != WL_CONNECTED))
  {
    Serial.print(".");
    digitalWrite(ledpin, HIGH); 
    delay(100);
    digitalWrite(ledpin, LOW);
    delay(100);
    server.handleClient();

  }

}

void callback(char* topic, byte* dados_tcp, unsigned int length) {
    for (int a = 0; a < length; a++) {
      }
  if ((char)dados_tcp[0] == 'L' && (char)dados_tcp[1] == '1') {
    digitalWrite(ledpin, HIGH);   
  } else if((char)dados_tcp[0] == 'L' && (char)dados_tcp[1] == '2'){
    digitalWrite(ledpin, LOW);  
  }
  if ((char)dados_tcp[0] == 'L' && (char)dados_tcp[1] == '2') {
    digitalWrite(LED2, HIGH);   
  } else if((char)dados_tcp[0] == 'D' && (char)dados_tcp[1] == '2'){
    digitalWrite(LED2, LOW);  
  }
} 

void loop() {

  if ((WiFi.status() == WL_CONNECTED))
  {

    for (int i = 0; i < 10; i++)
    {
    mqtt();
    //client.publish("Status ","Reiniciado!");
    //client.publish("Placa","Em funcionamento!");
    client.subscribe("LED"); 
    client.loop();

       buttonState=digitalRead(button); // put your main code here, to run repeatedly:
        if (buttonState == HIGH)
       {
       digitalWrite(ledpin, HIGH); 
       Serial.println(buttonState);
         for (int i = 0; i < 512; i++) {
        EEPROM.write(i, 0);
     }
     EEPROM.end();
       reset();
         }
       }
    }
  else
  {

  }

}


//-------- Fuctions used for WiFi credentials saving and connecting to it which you do not need to change 
bool testWifi(void)
{
  int c = 0;
  Serial.println("Waiting for Wifi to connect");
  while ( c < 20 ) {
    if (WiFi.status() == WL_CONNECTED)
    {
      return true;
    }
    delay(500);
    Serial.print("*");
    c++;
    
    
  }

  Serial.println("");
  Serial.println("Connect timed out, opening AP");
  return false;
}

void launchWeb()
{
  Serial.println("");
  if (WiFi.status() == WL_CONNECTED)
    Serial.println("WiFi connected");
  Serial.print("Local IP: ");
  Serial.println(WiFi.localIP());
  Serial.print("SoftAP IP: ");
  Serial.println(WiFi.softAPIP());
  createWebServer();

  // Start the server
  server.begin();
  Serial.println("Server started");
}

void setupAP(void)
{
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  int n = WiFi.scanNetworks();
  Serial.println("scan done");
  if (n == 0)
    Serial.println("no networks found");
  else
  {

    Serial.print(n);
    Serial.println(" networks found");
    for (int i = 0; i < n; ++i)
    {
      // Print SSID and RSSI for each network found
      Serial.print(i + 1);
      Serial.print(": ");
      Serial.print(WiFi.SSID(i));
      Serial.print(" (");
      Serial.print(WiFi.RSSI(i));
      Serial.print(")");
      Serial.println((WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*");
      delay(10);
    }
  }
  Serial.println("");
  st = "<ol>";
  for (int i = 0; i < n; ++i)
  {
    // Print SSID and RSSI for each network found
    st += "<li>";
    st += WiFi.SSID(i);
    st += " (";
    st += WiFi.RSSI(i);

    st += ")";
    st += (WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*";
    st += "</li>";
  }
  st += "</ol>";
  delay(100);
  WiFi.softAP("how2electronics", "");
  Serial.println("softap");
  launchWeb();
  Serial.println("over");
}

void createWebServer()
{
 {
    server.on("/", []() {

      IPAddress ip = WiFi.softAPIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      content = "<!DOCTYPE HTML>\r\n<html>Hello from ESP8266 at ";
      content += "<form action=\"/scan\" method=\"POST\"><input type=\"submit\" value=\"scan\"></form>";
      content += ipStr;
      content += "<p>";
      content += st;
      content += "</p><form method='get' action='setting'><label>SSID: </label><input name='ssid' length=32><input name='pass' length=64><input type='submit'></form>";
      content += "</html>";
      server.send(200, "text/html", content);
    });
    server.on("/scan", []() {
      //setupAP();
      IPAddress ip = WiFi.softAPIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);

      content = "<!DOCTYPE HTML>\r\n<html>go back";
      server.send(200, "text/html", content);
    });

    server.on("/setting", []() {
      String qsid = server.arg("ssid");
      String qpass = server.arg("pass");
      if (qsid.length() > 0 && qpass.length() > 0) {
        Serial.println("clearing eeprom");
        for (int i = 0; i < 96; ++i) {
          EEPROM.write(i, 0);
        }
        Serial.println(qsid);
        Serial.println("");
        Serial.println(qpass);
        Serial.println("");

        Serial.println("writing eeprom ssid:");
        for (int i = 0; i < qsid.length(); ++i)
        {
          EEPROM.write(i, qsid[i]);
          Serial.print("Wrote: ");
          Serial.println(qsid[i]);
        }
        Serial.println("writing eeprom pass:");
        for (int i = 0; i < qpass.length(); ++i)
        {
          EEPROM.write(32 + i, qpass[i]);
          Serial.print("Wrote: ");
          Serial.println(qpass[i]);
        }
        EEPROM.commit();

        content = "{\"Success\":\"saved to eeprom... reset to boot into new wifi\"}";
        statusCode = 200;
        ESP.reset();
      } else {
        content = "{\"Error\":\"404 not found\"}";
        statusCode = 404;
        Serial.println("Sending 404");
      }
      server.sendHeader("Access-Control-Allow-Origin", "*");
      server.send(statusCode, "application/json", content);

    });
  } 
}

void mqtt(){
   client.setServer(mqttServer, mqttPort);
   client.setCallback(callback);
   while (!client.connected()) {
    Serial.println("Connect to Server MQTT...");
    
    if (client.connect("Projeto", mqttUser, mqttPassword ))
    {
 
      Serial.println("Connected to server MQTT Successfully!");  
 
    } else {
 
      Serial.print("Not Connected Server MQTT ");
      Serial.print(client.state());
      delay(1000);
 
    }
  }

}
