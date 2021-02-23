#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include "Potenziometro.h"
#define POT_PIN A0

// Wifi network name
char* ssidName = "Infostrada-0D5FB4";

// Wifi network password
char* pwd = "44UX7A7MP7";

// Ngrop IP address to contact
char* address = "http://1af8a63a.ngrok.io"; 

// Declare potenziometro
Potenziometro *potenziometro;

void setup(){
	Serial.begin(115200);
	WiFi.begin(ssidName, pwd);
	Serial.print("COnnecting...");
	while(WiFi.status() != WL_CONNECTED){
		delay(500);
		Serial.print(".");
	}
	Serial.print("Connected: \n local IP: "+ WiFi.localIP());
}

int sendData(String address, float value){
	HTTPClient http;
	http.begin(address + "/api/data");
    http.addHeader("Content-Type", "application/json");     
	
	String msg = 
		String("\"umidita\":") + String(value) + "}";
	
	int retCode = http.POST(msg);
	http.end();
	Serial.print(msg);
	return retCode;
}

void loop(){
	 /* Check if the connesction has been stabilished */ 
	 if (WiFi.status()== WL_CONNECTED){  
		  
		     /* Read potenziometro.*/
		  float val = potenziometro->getValue();
		 
		     /* Send the value to the server */
		  Serial.print("sending " + String(val) + "...");
		  int code = sendData(address, val);
		 
		 	 /* Log result */ 
		  if (code == 200){
     		  Serial.println(" OK ");   
   		   }else {
     		  Serial.println(" ERROR ");
   		   }
	 }else { 
   		Serial.println("Error in WiFi connection");   
 	 }
 
 	 delay(1000);  
 
}
