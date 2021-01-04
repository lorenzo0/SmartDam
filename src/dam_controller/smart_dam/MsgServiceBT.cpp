#include "Arduino.h"
#include "MsgServiceBT.h"

/*
 * Il canale bluetooth come inotrodotto nella classe smart_dam
 * è necessario per comunicare con il sottosistema SMART_DAM.
 * 
 * Una volta modificato il suo nome, attraverso il comando AT 
 * (comando che è utilizzato per cambiare dei parametri
 * a livello hardware come il nome, il pin oppure verificare alcuni
 * dati come la versione), il canale è pronto per essere utilizzato.
 * 
 * La luce rossa lampeggiante caratterizza lo stato di ricerca 
 * (discovery) di nuovi dispositivi.
 * La luce rossa fissa indica una connessione istaurata tra il 
 * dispositivo embedded ed un dispositivo esterno (smarthphone, pc, etc..)
*/

MsgServiceBT::MsgServiceBT(int rxPin, int txPin){
  channel = new SoftwareSerial(rxPin, txPin);
}

void MsgServiceBT::init(){
  content.reserve(256);
  channel->begin(9600);
  availableMsg = NULL;

  channel -> print("AT+NAMESmartDamConn");
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

String MsgServiceBT::receiveMsg(){
  if (availableMsg != NULL){
    MsgBT* MsgBT = availableMsg;
    availableMsg = NULL;
    return MsgBT -> getContent();  
  } else {
    return "";
  }
}
