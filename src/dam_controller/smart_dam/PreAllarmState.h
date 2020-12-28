#ifndef __PREALLARMSTATE__
#define __PREALLARMSTATE__

#include "Task.h"
#include "Led.h"
#include "Globals.h"

class PreAllarmState: public Task{  

public: 

  PreAllarmState(int pinLed1);
  void init();  
  void tick();
  void sendDataHTTP();

private:

  int pinLed1;
  
  LedImpl* led1;

};

#endif
