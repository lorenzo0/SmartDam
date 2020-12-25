#include "Arduino.h"
#include "MsgServiceBT.h"


MsgServiceBT::MsgServiceBT(int rxPin, int txPin){
  channel = new SoftwareSerial(rxPin, txPin);
}

void MsgServiceBT::init(){
  content.reserve(256);
  channel->begin(9600);
  availableMsg = NULL;
}

bool MsgServiceBT::sendMsg(MsgBT MsgBT){
  channel->println(MsgBT.getContent());  
}

bool MsgServiceBT::isMsgAvailable(){
  while (channel->available()) {
    char ch = (char) channel->read();
    if (ch == '\n'){
      availableMsg = new MsgBT(content); 
      content = "";
      return true;    
    } else {
      content += ch;      
    }
  }
  return false;  
}

MsgBT* MsgServiceBT::receiveMsg(){
  if (availableMsg != NULL){
    MsgBT* MsgBT = availableMsg;
    availableMsg = NULL;
    return MsgBT;  
  } else {
    return NULL;
  }
}
