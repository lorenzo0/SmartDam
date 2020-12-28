#include "PreAllarmState.h"
#include "Arduino.h"

PreAllarmState::PreAllarmState(int pinLed1){
  this -> pinLed1 = pinLed1;
}

void PreAllarmState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
  Task::setFirstRun(false);

  Task::ts0 = millis();
}

void PreAllarmState::tick(){

  if(!(Task::firstRun)){
    led1 -> switchOff();
    Task::setFirstRun(true);
  }

  Task::currentTs = millis();
  
  while(Task::currentTs - Task::ts0 < min_freq){
      Task::currentTs = millis();
    }
    
    sendDataHTTP();
    Task::ts0 = millis();
}

/* 
 *  Se il livello dell'acqua è <L1 allora lo stato continua ad essere normale.
 *  Se il livello invece è >L1 allora lo stato cambierà alla conclusione del tick. 
 *  Viene dunque settata la task a completed.
 *  Se il livello è >L1 && >=L2 lo stato passa da uno stato di normalità ad uno stato di allarme.
*/
/*
void PreAllarmState::sendDataHTTP(){
  float value = 2.2;
  MsgService.sendMsg(value);
}*/
