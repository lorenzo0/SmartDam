#ifndef __SLEEPMODETASK__
#define __SLEEPMODETASK__

#include "Task.h"
#include "Led.h"
#include <avr/sleep.h>

class SleepModeTask: public Task{
  
  int pinLed1;
  LedImpl* led1;

public: 

  SleepModeTask(int pinLed1);
  void init(int period);  
  void tick();
  void sleep();
  void static wakeUp();
};

#endif
