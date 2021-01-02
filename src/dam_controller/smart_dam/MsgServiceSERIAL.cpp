#include "Arduino.h"
#include "MsgServiceSERIAL.h"

String content;
MsgServiceClass MsgServiceSERIAL;

bool MsgServiceClass::isMsgAvailable(){
  return msgAvailable;
}

String MsgServiceClass::receiveMsg(){
  if (msgAvailable){
    Msg* msg = currentMsg;
    msgAvailable = false;
    currentMsg = NULL;
    content =  msg -> getContent();
    return content;  
  } else {
    return ""; 
  }
}

void MsgServiceClass::init(){
  Serial.begin(9600);
  content.reserve(256);
  content = "";
  currentMsg = NULL;
  msgAvailable = false;  
}

void MsgServiceClass::sendMsg(const String& msg){
  Serial.println(msg);  
}
