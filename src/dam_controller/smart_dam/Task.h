#ifndef __TASK__
#define __TASK__
#include "Arduino.h"

class Task {

private: 

  int myPeriod;
  int timeElapsed;
  boolean completed;
  
public:

  /*
   * Ts0 è il tempo di rilascio (da cui si può ricavare il Jitter.
   * currentTs viene aggiornato ad ogni tick
   * firstRun è la variabile necessaria per distinguere il primo tick fatto 
   * dal task in esecuzione. In questo caso, viene gestita l'accensione o spegnimento dei led che
   * identicanano il processo (richiesti nelle specifiche).
   * nameNextTask è la variabile int che permette allo scheduler di mandare in esecuzione la task richiesta.
  */
  
  long ts0, currentTs;
  boolean firstRun;
  int nameNextTask;
  
  
  virtual void init(int period){
    myPeriod = period;
    completed = false;
    timeElapsed = 0;
  }


  void setCompleted(boolean setState){
    this -> completed = setState;
  }

  void setNextTask(int nameNextTask){
    this -> nameNextTask = nameNextTask;
  }

  int getNextTask(){
    return nameNextTask;
  }
  
  bool isCompleted(){
    return completed;
  }

  bool isFirstRun(){
    return firstRun;
  }

  void setFirstRun(boolean first){
    firstRun = first;
  }

  //deve essere definita da ogni classe che estende Task.
  virtual void tick() = 0;

  bool updateAndCheckTime(int basePeriod){
    timeElapsed += basePeriod;
    if (timeElapsed >= myPeriod ){
      timeElapsed = 0;
      return true;
    } else 
      return false; 
  }
  
};

#endif
