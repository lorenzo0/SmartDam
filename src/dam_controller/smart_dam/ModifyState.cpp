#include "ModifyState.h"
#include "Arduino.h"

extern double newOpeningDAM;

ModifyState::ModifyState(int pinLed1, int pinServoMotor){
  this -> pinLed1 = pinLed1;
}

void ModifyState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
  
  Task::setFirstRun(false);
}

void ModifyState::tick(){

  led1 -> switchOn();

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
