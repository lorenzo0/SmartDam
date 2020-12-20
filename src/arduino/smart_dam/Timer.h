#ifndef __TIMER__
#define __TIMER__


/* periodo restituito in ms */
class Timer {

public:  
  Timer();
  void setupPeriod(int period);  
  void waitForNextTick();
  void printCurrentTime();

};


#endif
