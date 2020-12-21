#include "NormalState.h"
#include "Arduino.h"
#include "MsgService.h"

NormalState::NormalState(int pinLed1, int pinSonarEcho, int pinSonarTrig){
  this -> pinLed1 = pinLed1;
  this -> pinSonarEcho = pinSonarEcho;
  this -> pinSonarTrig = pinSonarTrig;
}

void NormalState::init(int freq){
  Task::init(freq);
  
  led1 = new Led(pinLed1);
  sonar = new Sonar(pinSonarEcho, pinSonarTrig);
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
void NormalState::checkDistance(){
  currentDistance = sonar -> getDistance();
  if(currentDistance > min_level){
    if(currentDistance > max_level)
      Task::setNextTask(2);
    Task::setNextTask(1);
    Task::setCompleted(true); 
    Task::setFirstRun(false);
  }
}
