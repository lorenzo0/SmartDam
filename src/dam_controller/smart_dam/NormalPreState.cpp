#include "NormalPreState.h"
#include "Arduino.h"

NormalPreState::NormalPreState(int pinLed1){
  this -> pinLed1 = pinLed1;
}

void NormalPreState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
  Task::setFirstRun(false);
}

void NormalPreState::tick(){

  if(!(Task::firstRun)){
    led1 -> switchOff();
    Task::setFirstRun(true);
  }
}
