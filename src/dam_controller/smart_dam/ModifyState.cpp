#include "ModifyState.h"
#include "Arduino.h"

ModifyState::ModifyState(int pinLed1){
  this -> pinLed1 = pinLed1;
}

void ModifyState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
  
  Task::setFirstRun(false);
}

void ModifyState::tick(){

  if(!(Task::firstRun)){
    Serial.println("ACCENDITI SESAMA");
    led1 -> switchOn();
    Task::setFirstRun(true);
  }
}
