/*  This program allows user to control an LED through WiFi network
 *  Written by:- Pranav Ghatigar
 *  Completed on:- 29-07-2020
 */
#include "WiFi.h"
#include <WiFiClient.h>
#include <WiFiAP.h>
#include "string.h"
#include "AsyncUDP.h"
#define LED_BUILTIN 2
                                                 // Variables used throughout the program
const char *ssid = "ESP32_AP";
const char *password = "1234567890";
char c;char array[50];
int i=0;
const char* Ssid;
const char* Password;
char* ss;
char* ps;
bool configflag=true;
String ipaddress="";
unsigned long preMillis=0;
const long interval=2000;
AsyncUDP udp;
                                                 // Set server Port Number
WiFiServer server(80);
                                                 //Function to perform a task
void proceedvalue(char a)
{
  if(a=='1')
  {digitalWrite(LED_BUILTIN, HIGH);}
  else if(a=='0')
  {digitalWrite(LED_BUILTIN, LOW);}
  else if(a=='r')
  {
    configflag=true;
     
  }
  return; 
}
                                                 //Runs only once when the system is Reset
void setup() 
{
  Serial.begin(115200);
  Serial.println();
  Serial.println("Configuring access point...");
  WiFi.mode(WIFI_AP);                            //Set ESP32 as Access Point
  WiFi.softAP(ssid, password);
  IPAddress myIP = WiFi.softAPIP();              //Obtaining IPAddress
  Serial.print("AP IP address: ");
  Serial.println(myIP);
  server.begin();                                //Start Server
  Serial.println("Server started");
  pinMode(LED_BUILTIN, OUTPUT);                  //Set LED pin to output mode
  digitalWrite(LED_BUILTIN, LOW);
}

void loop() 
{
  WiFiClient client = server.available();       //Listen for incoming clients
  if (client)                                   //If you get a client
  {                             
    Serial.println("New Client.");              //Print a message out  serialPort
    while (client.connected())                  //Loop while client is connected
    {            
      if(configflag)                            //Executes only the first time
      {
        while(client.available())               //While there are bytes to read from client
        {             
          char c = client.read();               //Read the byte
          Serial.write(c);                      //Print the byte on SerialPort
          array[i]=c;                           //Store the byte in an character array
          i++;
        }
      }
      else                                      //Executes from second time
      {
        while(client.available())               //Loop while client is connected
        {           
          char b = client.read();               //Read the byte
          proceedvalue(b);                      //Call the function with parameter
          Serial.write(b);                      //Print the byte on SerialPort
        }
      }
    }
                                                //Seperate the SSID and Password from character array
    char* temp=strtok(array,"~");
    ss= temp;                                     
    while(temp !=NULL)
    {
      ps=temp;
      temp=strtok(NULL,"~");
    }
    Ssid =ss;
    Password=ps;
    if(configflag)                              //If its the First time
    {
      WiFi.mode(WIFI_STA);                      //Change the WiFi mode to Station
      WiFi.disconnect();                        //Disconnect the WiFi shield from current network.
      delay(100);
      WiFi.begin(Ssid, Password);               //Connects to the specified Router
      Serial.print("Connecting to WiFi");  
      while(WiFi.status() != WL_CONNECTED)
      {
        Serial.print(".");
        delay(1000);
      }
      Serial.print("Connected to WiFi network: ");
      Serial.println(Ssid);
      ipaddress="ESP32~"+ WiFi.localIP().toString();
      delay(5000);
      Serial.println(ipaddress);                //Find and print the IP address of board
      udp.connect(IPAddress(192,168,1,255),9001); //IP Address connects in Network ID
      delay(10);                                  
      unsigned long curMillis=millis();
      if(curMillis-preMillis>=interval)
      {
        preMillis=curMillis;
        udp.broadcastTo(ipaddress.c_str(),9001);  // Broadcast IP Address every 2000 ms
      }
      configflag=false;                         //So that it does not disconnect from the current WiFi network
     }
      Serial.println("Client Disconnected.");  
  }
                                                // Broadcast even though client is disconnected
  udp.connect(IPAddress(192,168,1,255),9001);
  delay(10);
  unsigned long curMillis=millis();
  if(curMillis-preMillis>=interval)
  {
     preMillis=curMillis;
     udp.broadcastTo(ipaddress.c_str(),9001);
  }
}
