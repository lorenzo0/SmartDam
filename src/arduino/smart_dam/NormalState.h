#ifndef __NORMALSTATE__
#define __NORMALSTATE__

#include "Task.h"
#include "Led.h"

class NormalState: public Task{  

public: 

  NormalState(int pinLed1);
  void init(int period);  
  void tick();

private:

  int pinLed1;
  
  LedImpl* led1;

};

#endif
