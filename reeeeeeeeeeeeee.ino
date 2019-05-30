#include <ESP8266WiFi.h>
#include "ThingSpeak.h"
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>

#define BME_SCK 13
#define BME_MISO 12
#define BME_MOSI 11
#define BME_CS 10

#define SEALEVELPRESSURE_HPA (1013.25)

Adafruit_BME280 bme;

WiFiClient client;

String ssid = "FMI-AIR-NEW";
String pass = "";
int statusCode = 0;

unsigned long counterChannelNumber = 722709;            // Channel ID
const char * myCounterReadAPIKey = "LUVMZFRKWNBSO3OQ"; // Read API Key
const char * writeAPIKey = "PQG13OXKRMFB23R7";

const int AutoModeField = 1; 
const int ShouldOpenField = 2; 
const int TempField = 3; 
const int IsOpenField = 4;

int isOpenNow = 1;
//-------------------------------//

int IN1 = D3;
int IN2 = D4;

void setup()
{
    Serial.begin(115200);
    //while (!Serial); // wait for serial attach
    pinMode(IN1, OUTPUT);
    pinMode(IN2, OUTPUT);
    
    bool status;
    Wire.begin(D2,D1);
    status = bme.begin(0x76);  
    if (!status) {
        Serial.println("Could not find a valid BME280 sensor, check wiring!");
        while (1);
    }    

    Serial.println();
    Serial.println("Initializing...");
    Serial.flush();

    //wifi stuff
    WiFi.mode(WIFI_STA);
    ThingSpeak.begin(client);
}

void loop(){

  if (WiFi.status() != WL_CONNECTED)
  {
    Serial.print("Connecting to ");
    Serial.print(ssid);
    Serial.println(" ....");
    while (WiFi.waitForConnectResult() != WL_CONNECTED)
    {
      Serial.print(WiFi.status());
      WiFi.begin(ssid, pass);
      delay(1000);
    }
    Serial.println("Connected to Wi-Fi Succesfully.");
  }

  //---------------- Channel 1 ----------------//
  long temp = ThingSpeak.readLongField(counterChannelNumber, TempField, myCounterReadAPIKey);  
  long isOpen = ThingSpeak.readLongField(counterChannelNumber, ShouldOpenField, myCounterReadAPIKey);
  long autoMode = ThingSpeak.readLongField(counterChannelNumber, AutoModeField, myCounterReadAPIKey);
  
  statusCode = ThingSpeak.getLastReadStatus();
  if (statusCode == 200)
  {
    Serial.print("Thingspeak temperature = ");
    Serial.println(temp);
    Serial.print("Current temp = ");
    Serial.println(bme.readTemperature());
    Serial.print("IsOpen = ");
    Serial.println(isOpen);
  }
  else
  {
    Serial.println("Unable to read channel / No internet connection");
  }

  if(autoMode == 1){
    if(bme.readTemperature() < temp){
      closeWindow();
    } else{
      openWindow();
    }
  }else{
    if(isOpen == 1) {
      openWindow();
    } else{
      closeWindow();
    }
  }
  delay(1000);
}

void closeWindow()
{
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);

  if(isOpenNow == 1)
  {
    //Wait for it to close
    delay(10000);
    SendToThingspeak(0);
    isOpenNow = 0;
  }
}

void openWindow()
{
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);

  if(isOpenNow == 0)
  {  
    //Wait for it to open
    delay(10000);
    SendToThingspeak(1);
    isOpenNow = 1l
  }
}

void SendToThingspeak(int isOpen)
{
//    String postStr = writeAPIKey;
//    postStr += "&field";
//    postStr += String(IsOpenField);
//    postStr += "=";
//    postStr += String(isOpen);
//    postStr += "\r\n\r\n";
//    client.print("POST /update HTTP/1.1\n");
//    client.print("Host: api.thingspeak.com\n");
//    client.print("Connection: close\n");
//    client.print("X-THINGSPEAKAPIKEY: " + writeAPIKey + "\n");
//    client.print("Content-Type: application/x-www-form-urlencoded\n");
//    client.print("Content-Length: ");
//    client.print(postStr.length());
//    client.print("\n\n");
//    client.print(postStr);
//    Serial.println(postStr);

      
      ThingSpeak.writeField(counterChannelNumber, IsOpenField, isOpen, writeAPIKey);
}

void printValues() {
    Serial.print("Temperature = ");
    Serial.print(bme.readTemperature());
    Serial.println(" *C");

    Serial.print("Pressure = ");

    Serial.print(bme.readPressure() / 100.0F);
    Serial.println(" hPa");

    Serial.print("Approx. Altitude = ");
    Serial.print(bme.readAltitude(SEALEVELPRESSURE_HPA));
    Serial.println(" m");

    Serial.print("Humidity = ");
    Serial.print(bme.readHumidity());
    Serial.println(" %");

    Serial.println();
}
