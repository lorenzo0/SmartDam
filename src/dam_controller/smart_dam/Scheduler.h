#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Task.h"
#include "MsgServiceBT.h"
#include "Led.h"

#define MAX_TASKS 50

class Scheduler {
  
  int basePeriod;
  int nTasks;
  String nameTask;
  Task* taskList[MAX_TASKS];  
  Task* taskObject;
  Led* led;

public:
  void init(Led* led_pin);  
  virtual bool addTask(Task* task);  
  virtual void schedule();
  void setIndexCurrentTaskActive(int index);
  void bluethoot_receiving();
  void setNewOpeningDAM(int value);
  void checkMessageReceivedSERIAL();

private:
  int indexCurrentTaskActive;
  long period;
};

/*0 è idle
1 è error
2 sleep*/

#endif
