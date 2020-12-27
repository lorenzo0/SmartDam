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
  //deve essere const String& value
  void sendMsg(const float& value);
  void checkConnection();

private:

char* nameWIFI = "Wind3 HUB-7C4FA6";
char* pwdWIFI = "CasaPisano!!";
char* addressWIFI = "http://daaab9786c3a.ngrok.io";

String state = "ALLARM";
};

extern MsgServiceHTTP MsgService;

#endif
