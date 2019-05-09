//  Connect IN /Data to GPIO2 or D4 on Nodemcu / Wemos
//

#include <NeoPixelBus.h>
#include <ESP8266WiFi.h>
#include "ThingSpeak.h"

const uint16_t PixelCount = 6; // this example assumes 4 pixels, making it smaller will cause a failure
const uint8_t PixelPin = 2;  // make sure to set this to the correct pin, ignored for Esp8266

#define FLASH_BUTTON 0
#define GPIO_05 5   // D1 on NodeMCU 0.9



#define Cred     RgbColor(255, 0, 0)
#define Cpink    RgbColor(255, 0, 128)
#define Clila    RgbColor(255, 0, 255)
#define Cviolet  RgbColor(128, 0, 255)
#define Cblue    RgbColor(0, 0, 255)
#define Cmblue   RgbColor(0, 128, 255)
#define Ccyan    RgbColor(0, 255, 255)
#define Cgreen   RgbColor(0, 255, 0)
#define Cyellow  RgbColor(255, 255, 0)
#define Corange  RgbColor(255, 100, 0)
#define Cwhite   RgbColor(255, 255, 255)
#define Cblack   RgbColor(0) 
RgbColor allColors[] = {Cred, Clila, Cmblue, Cgreen, Cyellow, Corange}; 

// Uart method is good for the Esp-01 or other pin restricted modules
// NOTE: These will ignore the PIN and use GPI02 pin
NeoPixelBus<NeoGrbFeature, NeoEsp8266Uart1800KbpsMethod> strip(PixelCount, PixelPin);
//NeoPixelBus<NeoRgbFeature, NeoEsp8266Uart400KbpsMethod> strip(PixelCount, PixelPin);

WiFiClient client;

String ssid = "FMI-AIR-NEW";
String pass = "";
int statusCode = 1;
//String sendData = "GET https://api.thingspeak.com/channels/722709/fields/1.json?api_key=LUVMZFRKWNBSO3OQ&results=2";
//String output = ""; //Initialize a null string variable

unsigned long counterChannelNumber = 722709;            // Channel ID
const char * myCounterReadAPIKey = "LUVMZFRKWNBSO3OQ"; // Read API Key
const int FieldNumber1 = 2;  // The field you wish to read
const int FieldNumber2 = 2;  // The field you wish to read
//-------------------------------//


void setup()
{
    Serial.begin(115200);
    //while (!Serial); // wait for serial attach

    Serial.println();
    Serial.println("Initializing...");
    Serial.flush();

    // this resets all the neopixels to an off state
    strip.Begin();
    strip.Show();

    pinMode(FLASH_BUTTON,INPUT_PULLUP);
    pinMode(LED_BUILTIN, OUTPUT);

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
    while (/*WiFi.status() != WL_CONNECTED*/ WiFi.waitForConnectResult() != WL_CONNECTED)
    {
      Serial.print(WiFi.status());
      WiFi.begin(ssid, pass);
      delay(1000);
    }
    Serial.println("Connected to Wi-Fi Succesfully.");
  }

  //---------------- Channel 1 ----------------//
  long temp = ThingSpeak.readLongField(counterChannelNumber, FieldNumber1, myCounterReadAPIKey);
    statusCode = ThingSpeak.getLastReadStatus();
  if (statusCode == 200)
  {
    Serial.print("Light: ");
    Serial.println(temp);
  }
  else
  {
    Serial.println("Unable to read channel / No internet connection");
  }
  delay(100);


  //--------------------------------
  int value = digitalRead(FLASH_BUTTON);

  Serial.println((value == HIGH)?"HIGH":"LOW");
  digitalWrite(LED_BUILTIN, value);
  if(temp == 1) {
    turnOn();
  }else {
     turnOff();
  }
    delay(1000);
}

void turnOn(){
   for (int i=0; i < 6; i++) {
      RgbColor c = allColors[1];
      c = RgbColor::LinearBlend(c, Cblack, 0.9F); 
      strip.SetPixelColor(i, c);
      strip.Show();
    }
}

void turnOff(){
  for (int i=0; i < 6; i++) {
      RgbColor c = Cblack;
      c = RgbColor::LinearBlend(c, Cblack, 0.9F); 
      strip.SetPixelColor(i, c);
      strip.Show();
    }
}

