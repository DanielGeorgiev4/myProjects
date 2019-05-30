#include <Wire.h> 
#include <ESP8266WiFi.h>
#include "ThingSpeak.h"
#include <LiquidCrystal_I2C.h>

// Set the LCD address to 0x27 for a 16 chars and 2 line display
LiquidCrystal_I2C lcd(0x27, 16, 2);

WiFiClient client;

String ssid = "FMI-AIR-NEW";
String pass = "";
int statusCode = 0;
long oldInfo = -1;

unsigned long counterChannelNumber = 722709;            // Channel ID
const char * myCounterReadAPIKey = "LUVMZFRKWNBSO3OQ"; // Read API Key

const int IsClosedField = 4;

String line = " The window is:                              ";

void setup()
{ 
  Serial.begin(115200);
  
  Wire.begin(D6, D7);
	// initialize the LCD
	lcd.begin();

  WiFi.mode(WIFI_STA);
  ThingSpeak.begin(client);

	// Turn on the blacklight and print a message.
	lcd.backlight();
}

void loop()
{
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
  long isClosed = ThingSpeak.readLongField(counterChannelNumber, IsClosedField, myCounterReadAPIKey);
  isClosed = (isClosed + 1)%2;
  
  statusCode = ThingSpeak.getLastReadStatus();
  if (statusCode == 200)
  {
    Serial.println(isClosed);
  }
  else
  {
    Serial.println("Unable to read channel / No internet connection");
  }
  
  if(oldInfo != isClosed){
    display(isClosed);
    oldInfo = isClosed;
  }
}

void display(long isClosed){
  lcd.backlight();
  lcd.clear();
  if(isClosed == 1){
    lcd.print(line + "closed");
  }else{
    lcd.print(line + "opened");
  }
}

