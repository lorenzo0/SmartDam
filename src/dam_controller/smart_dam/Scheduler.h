#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Task.h"
#include "MsgServiceBT.h"

#define MAX_TASKS 50

class Scheduler {
  
  int basePeriod;
  int nTasks;
  String nameTask;
  Task* taskList[MAX_TASKS];  
  Task* taskObject;

public:
  void init(int pinRX, int pinTX);  
  virtual bool addTask(Task* task);  
  virtual void schedule();
  void setIndexCurrentTaskActive(int index);
  void bluethoot_receiving();
  void setNewOpeningDAM(double value);

private:
  int indexCurrentTaskActive;
  long period;
  int pinRX, pinTX;
};

/*0 è idle
1 è error
2 sleep*/

#endif
