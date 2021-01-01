#ifndef __MODIFYSTATE__
#define __MODIFYSTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"
#include "servo_motor_impl.h"
#include "SharingMethods.h"

class ModifyState: public Task{  

public: 

  ModifyState(int pinLed1, int pinServoMotor);
  void init();  
  void tick();

private:

  int pinLed1;
  int pinServoMotor;
  double oldValue, oldOpeningServo;
  
  LedImpl* led1;
  ServoMotor* pMotor;
  SharingMethods* sharingMethods;

};

#endif
