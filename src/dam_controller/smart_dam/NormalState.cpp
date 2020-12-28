#include "NormalState.h"
#include "Arduino.h"

NormalState::NormalState(int pinLed1){
  this -> pinLed1 = pinLed1;
}

void NormalState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
  Task::setFirstRun(false);
}

void NormalState::tick(){

  if(!(Task::firstRun)){
    led1 -> switchOff();
    Task::setFirstRun(true);
  }

  checkDistance();
}

/* 
 *  Se il livello dell'acqua è <L1 allora lo stato continua ad essere normale.
 *  Se il livello invece è >L1 allora lo stato cambierà alla conclusione del tick. 
 *  Viene dunque settata la task a completed.
 *  Se il livello è >L1 && >=L2 lo stato passa da uno stato di normalità ad uno stato di allarme.
*/

/*
void NormalState::checkDistance(){
  if(currentDistance > min_level){
    Task::setNextTask(1);
    if(currentDistance > max_level)
      Task::setNextTask(2);
    Task::setCompleted(true); 
    Task::setFirstRun(false);
  } 
}*/
