#include "AllarmState.h"
#include "Arduino.h"

extern int newOpeningDAM;

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
  
  if(newOpeningDAM != -1 && newOpeningDAM != oldValue){
    oldValue = newOpeningDAM;
    Serial.println("OldValue = "+ String(newOpeningDAM));
    int newOpeningToServo = sharingMethods -> calculateOpeningServo(newOpeningDAM);
    Serial.println("newOpening = "+ String(oldValue));
    
    if(newOpeningToServo > oldOpeningServo){
      pMotor->on();  
      for (int i = oldOpeningServo; i < newOpeningToServo; i++) {
        pMotor->setPosition(i);         
        delay(15);
      }
     pMotor->off();
    }else{
      pMotor->on();  
      for (int i = newOpeningToServo; i < oldOpeningServo; i++) {
        pMotor->setPosition(i);         
        delay(15);
      }
     pMotor->off();
    }
      delay(500);
  }
}
