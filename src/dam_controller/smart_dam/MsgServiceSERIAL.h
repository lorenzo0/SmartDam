#ifndef __MSGSERVICESERIAL__
#define __MSGSERVICESERIAL__

#include "Arduino.h"


/* Classe Messaggio, contiene il suo costruttore ed un getter per ricavare il messaggio */
class Msg {
String content;

public:
  Msg(String content){
    this->content = content;
  }
  
  String getContent(){
    return content;
  }
};

class Pattern {
public:
  virtual boolean match(const Msg& m) = 0;  
};

class MsgServiceClass {
    
public: 
  
  Msg* currentMsg;
  bool msgAvailable;

  void init();  
  void sendMsg(const String& msg);
  bool isMsgAvailable();
  String receiveMsg();
};

/* condivisione variabile all'esterno della classe stessa */
extern MsgServiceClass MsgServiceSERIAL;

#endif
