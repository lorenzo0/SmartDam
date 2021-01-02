#include "NormalPreState.h"
#include "Arduino.h"

NormalPreState::NormalPreState(int pinLed1){
  this -> pinLed1 = pinLed1;
}

void NormalPreState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
}

void NormalPreState::tick(){
    led1 -> blinking();
}
