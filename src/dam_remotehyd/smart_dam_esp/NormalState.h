#ifndef __NORMALSTATE__
#define __NORMALSTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"
#include "Sonar.h"

class NormalState: public Task{  

public: 

  NormalState(int pinLed1, int pinSonarEcho, int pinSonarTrig);
  void init();  
  void tick();
  void checkDistance();

private:

  int pinLed1;
  int pinSonar;
  int pinSonarEcho;
  int pinSonarTrig;
  double currentDistance;
  
  LedImpl* led1;
  Sonar* sonar;

};

#endif
