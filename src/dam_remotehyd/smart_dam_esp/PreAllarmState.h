#ifndef __PREALLARMSTATE__
#define __PREALLARMSTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"
#include "Sonar.h"

class PreAllarmState: public Task{  

public: 

  PreAllarmState(int pinLed1, int pinSonarEcho, int pinSonarTrig);
  void init();  
  void tick();
  void sendDataHTTP();

private:

  int pinLed1;
  int pinSonarEcho;
  int pinSonarTrig;
  
  LedImpl* led1;
  Sonar* sonar;

};

#endif
