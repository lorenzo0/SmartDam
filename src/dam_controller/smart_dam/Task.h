#ifndef __TASK__
#define __TASK__
#include "Arduino.h"

class Task {

private: 
  
public:
  
  virtual void init(){
  }

  /*deve essere definita da ogni classe che estende Task.*/
  virtual void tick() = 0;
  
};

#endif
