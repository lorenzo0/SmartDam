#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Task.h"

#define MAX_TASKS 50

class Scheduler {
  
  int basePeriod;
  int nTasks;
  String nameTask;
  Task* taskList[MAX_TASKS];  
  Task* taskObject;

public:
  void init();  
  virtual bool addTask(Task* task);  
  virtual void schedule();
  virtual void redirectTask(int nextState);
  void setIndexCurrentTaskActive(int index);

private:
  int indexCurrentTaskActive;
  int i;
  long period;
};

/*0 è idle
1 è error
2 sleep*/

#endif
