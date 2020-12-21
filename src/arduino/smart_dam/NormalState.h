#ifndef __NORMALSTATE__
#define __NORMALSTATE__

#include "Task.h"
#include "Led.h"
#include "Sonar.h"
#include "Globals.h"

class NormalState: public Task{  

public: 

  NormalState(int pinLed1, int pinSonarEcho, int pinSonarTrig);
  void init(int freq);  
  void tick();
  void checkDistance();

private:

  int pinLed1;
  int pinSonar;
  int pinSonarEcho;
  int pinSonarTrig;
  
  LedImpl* led1;
  Sonar* sonar;

};

#endif
