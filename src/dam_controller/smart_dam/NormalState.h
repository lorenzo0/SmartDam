#ifndef __NORMALSTATE__
#define __NORMALSTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"

class NormalState: public Task{  

public: 

  NormalState(int pinLed1);
  void init();  
  void tick();
  void checkDistance();

private:

  int pinLed1;
  double currentDistance;
  
  LedImpl* led1;

};

#endif
