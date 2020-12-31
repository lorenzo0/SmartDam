#ifndef __MODIFYSTATE__
#define __MODIFYSTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"

class ModifyState: public Task{  

public: 

  ModifyState(int pinLed1);
  void init();  
  void tick();

private:

  int pinLed1;
  
  LedImpl* led1;

};

#endif
