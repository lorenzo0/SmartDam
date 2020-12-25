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

  MsgServiceHTTP(char* nameWIFI, char* pwdWIFI, char* addressWIFI);
  void init();
  void sendMsg(String msgToSend);
  void checkConnection();

private:

  HTTPClient http;
  IPAddress ip;
  char* nameWIFI;
  char* pwdWIFI;
  char* addressWIFI;
  
};

#endif
