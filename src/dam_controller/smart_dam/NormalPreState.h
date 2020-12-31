#ifndef __NORMALPRESTATE__
#define __NORMALPRESTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"

class NormalPreState: public Task{  

public: 

  NormalPreState(int pinLed1);
  void init();  
  void tick();
  void checkDistance();

private:

  int pinLed1;
  double currentDistance;
  
  LedImpl* led1;

};

#endif
