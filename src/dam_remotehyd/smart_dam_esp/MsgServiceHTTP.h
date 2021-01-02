#ifndef __MSGSERVICEHTTP__
#define __MSGSERVICEHTTP__

#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>

/* 
 *  Connessione con wifi 
 *     - ssidName = Nome WIFI
 *     - pwd = WPA2 PSK password
 *     - address = service IP address
 */
 
class MsgServiceHTTP{  

public: 

  void init();
  void sendMsg(const float& value, const String& state);
  void checkConnection();

private:

char* nameWIFI = "Wind3 HUB-7C4FA6";
char* pwdWIFI = "CasaPisano!!";
char* addressWIFI = "http://30354619d486.ngrok.io";
String sender = "ESP";

};

extern MsgServiceHTTP MsgService;

#endif
