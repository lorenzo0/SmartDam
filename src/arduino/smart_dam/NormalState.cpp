#include "NormalState.h"
#include "Arduino.h"
#include "MsgService.h"

NormalState::NormalState(int pinLed1){
  this -> pinLed1 = pinLed1;
}

void NormalState::init(int period){
  Task::init(period);
  
  led1 = new Led(pinLed1);
  Task::setFirstRun(false);
}

void NormalState::tick(){

  if(!(Task::firstRun)){
    led1 -> switchOff();
    Task::setFirstRun(true);
    /*Task::setNextTask(3);
    Task::ts0 = millis();*/
  }

  /*Task::currentTs = millis();
  
  if(Task::currentTs - Task::ts0 > SLEEP_TIME){
    Task::setCompleted(true); 
    Task::setNextTask(2);
    Task::setFirstRun(false);
  }*/

  // qui bisogna fare il get del sonar, se supera i parametri, allora cambia stato
}
