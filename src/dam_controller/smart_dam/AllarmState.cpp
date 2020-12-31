#include "AllarmState.h"
#include "Arduino.h"

extern double newOpeningDAM;

AllarmState::AllarmState(int pinLed1, int pinServo){
  this -> pinLed1 = pinLed1;
  this -> pinServo = pinServo;
}

void AllarmState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
  pMotor = new ServoMotorImpl(pinServo);
  
  Task::setFirstRun(false);
}

void AllarmState::tick(){
  led1 -> blinking();

  //Serial.println("New opening on Allarm State: "+ (String) newOpeningDAM); lo vede correttamente

  if(newOpeningDAM != -1){
    oldValue = newOpeningDAM;
    pMotor -> setPosition(oldValue);
  }else if(newOpeningDAM != -1 && newOpeningDAM != oldValue){
    oldValue = newOpeningDAM;
    pMotor -> setPosition(oldValue);
  }
}
