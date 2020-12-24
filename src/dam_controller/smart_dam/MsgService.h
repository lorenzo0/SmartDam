#ifndef __MSGSERVICE__
#define __MSGSERVICE__

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
  bool isMsgAvailable(Pattern& pattern);
  Msg* receiveMsg();
  Msg* receiveMsg(Pattern& pattern);
};

/* condivisione variabile all'esterno della classe stessa */
extern MsgServiceClass MsgService;

#endif
