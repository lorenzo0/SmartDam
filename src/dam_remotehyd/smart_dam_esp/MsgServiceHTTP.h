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
  IPAddress ip;

  char* nameWIFI = "xxx";
  char* pwdWIFI = "xxx";
  char* addressWIFI = "xxx";
  String place = "home";
  
};

extern MsgServiceHTTP MsgService;

#endif
