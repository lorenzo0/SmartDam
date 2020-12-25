#include "Arduino.h"
#include "MsgServiceHTTP.h"

MsgServiceHTTP::MsgServiceHTTP(char* nameWIFI, char* pwdWIFI, char* addressWIFI){
  this -> nameWIFI = nameWIFI;
  this -> pwdWIFI = pwdWIFI;
  this -> addressWIFI = addressWIFI;
}

void MsgServiceHTTP::init(){
  WiFi.begin(nameWIFI, pwdWIFI);
  Serial.print("Connecting to WIFI" + String(nameWIFI));
  
  while (WiFi.status() != WL_CONNECTED) {  
    delay(500);
    Serial.print(".");
  } 
  delay(100);
  Serial.println("Connected to WIFI: "+ String(nameWIFI));
}


void MsgServiceHTTP::sendMsg(String msgToSend){
  if (WiFi.status()== WL_CONNECTED){ 
     http.begin(String(addressWIFI) + "/api/data");      
     http.addHeader("Content-Type", "application/json");     
     String msg = 
      String("{ \"value\": ") + String(msgToSend) + ", \"place\": home\" }";
     int retCode = http.POST(msg);   
     http.end();  

     if (retCode == 200){
       Serial.println("Message-Sent!");   
     } else {
       Serial.println("Message-Not-Sent!");
     }
  }else{
    Serial.println("Error in WiFi connection");
  }
}

void MsgServiceHTTP::checkConnection(){
  if (WiFi.status()== WL_CONNECTED)
    Serial.println("true");
  else
    Serial.println("false");
}
