#ifndef __ALLARMSTATE__
#define __ALLARMSTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"
#include "servo_motor_impl.h"
#include "SharingMethods.h"

class AllarmState: public Task{  

public: 

  AllarmState(int pinLed1, int pinServoMotor);
  void init();  
  void tick();

private:

  int pinLed1;
  int pinServo;
  double oldValue, oldOpeningServo;
  
  LedImpl* led1;
  ServoMotor* pMotor;
  SharingMethods* sharingMethods;

};

#endif
